package com.neu.calander.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.neu.calander.Adapter.Bean.Post_Data;
import com.neu.calander.Pojo.User;
import com.neu.calander.R;
import com.neu.calander.Util.Get_image;
import com.neu.calander.Util.ImageTobit;
import com.nex3z.flowlayout.FlowLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

public class NewPost extends AppCompatActivity {
    ArrayList<String> mSelected;
    private int screenWidth;
    private int imagewidth;
    private int screenHeigh;
    private int image_count = 0;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        DisplayMetrics dm = new DisplayMetrics();
//获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
        imagewidth  = screenWidth/3 - 20;

        button = findViewById(R.id.b10);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imagewidth,imagewidth);
        button.setLayoutParams(layoutParams);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MultiImageSelector.create(getApplicationContext())
                        .count(5)
                        .showCamera(false)
                        .multi()
                        .origin(mSelected)
                        .start(NewPost.this,9);
            }
        });


        //send_post
        Button button1 = findViewById(R.id.cancel_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button button2 = findViewById(R.id.post);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.content);
                if(editText.getText().length()!=0 || mSelected!=null) {
                    Post_Data post_data = new Post_Data();
                    post_data.setUser_id(User.getSingle_instance().getId());
                    if(editText.getText().length()!=0){
                        post_data.setContent(editText.getText().toString());
                    }
                    if(mSelected!=null) {
                        List<String> images = new ArrayList();
                        if (mSelected.size() != 0) {
                            for (String path : mSelected) {
                                try {
                                    Bitmap bitmap = ImageTobit.readStream(path);
                                    bitmap = Get_image.compressImage(bitmap);
                                    String image_str = ImageTobit.bitmapToString(bitmap);
                                    images.add(image_str);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        post_data.setImage(images);
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日,HH:mm");
                    Date now=new Date();
                    String sdfs = sdf.format(now);
                    post_data.setTime(sdfs);
                    sumbit_post(post_data);
                    finish();
                }else {
                    Toast.makeText(NewPost.this, "請來點內容啊,不然你想發什么呢", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==9&&resultCode==RESULT_OK){
            mSelected = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            Log.e("test",mSelected.toString() );
            FlowLayout flowLayout = findViewById(R.id.flowlayout);
            flowLayout.removeAllViews();
            flowLayout.addView(button);
            for(String path :mSelected){
                if(path!=null) {
                    ImageView imageView = new ImageView(getApplication());
                    try {
                        imageView.setImageBitmap(Get_image.select_image(getApplicationContext(), path));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imageView.setAdjustViewBounds(true);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MultiImageSelector.create(getApplicationContext())
                                    .count(5)
                                    .multi()
                                    .showCamera(true)
                                    .origin(mSelected)
                                    .start(NewPost.this,9);
                        }
                    });
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(imagewidth,imagewidth);
                    flowLayout.addView(imageView,parms);
                }
            }
        }
    }

    private void sumbit_post(Post_Data post_data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String outline = JSON.toJSONString(post_data);
                URL url = null;
                HttpURLConnection connection = null;
                String result = null;
                try {
                    url = new URL("https://lkcmacauweb.top/Server/android/send_post");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(1000);
                    connection.setDoOutput(true);
                    connection.connect();
                    OutputStream out = connection.getOutputStream();
                    out.write(outline.getBytes(),0,outline.getBytes().length);
                    out.flush();
                    out.close();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String s;
                    while ((s=bufferedReader.readLine())!=null) {
                        result+=s;
                    }
                    bufferedReader.close();
                    connection.disconnect();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                    return;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }finally {
                    connection.disconnect();
                }

                if(result.equals("success")){
                    Toast.makeText(NewPost.this, "發表成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NewPost.this, "發表失敗", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();;
    }
}