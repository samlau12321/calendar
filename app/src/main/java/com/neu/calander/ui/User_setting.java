package com.neu.calander.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customdatedialogtest.CustomDateDialog;
import com.neu.calander.R;
import com.neu.calander.Util.Get_image;
import com.neu.calander.Util.ImageTobit;
import com.neu.calander.Service.User_Service;
import com.neu.calander.ui.dialog.MyDialog;
import com.neu.calander.Pojo.User;

import java.io.IOException;
import java.util.Calendar;

public class User_setting extends AppCompatActivity {
    private TextView tv1;
    private ImageView imageView;
    private TextView sex;
    private TextView born;
    private TextView sign;
    private TextView school;
    private Button exit;
    private static Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        tv1 = findViewById(R.id.name);
        tv1.setText(User.getSingle_instance().getName());
        tv1.setOnClickListener(new MyDialog(this));
        sex = findViewById(R.id.sex_2);
        born = findViewById(R.id.born);
        sign = findViewById(R.id.sign);
        school = findViewById(R.id.school_ID);
        imageView = findViewById(R.id.my_image_3);
        exit = findViewById(R.id.button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(User.getSingle_instance().getIcon()!=null){
            try {
                imageView.setImageBitmap(ImageTobit.StringToBitmap(User.getSingle_instance().getIcon()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(User.getSingle_instance().getSex()!=null){
            sex.setText(User.getSingle_instance().getSex());
        }
        if(User.getSingle_instance().getBorn()!=null){
            born.setText(User.getSingle_instance().getBorn());
        }
        if(User.getSingle_instance().getSign()!=null){
            sign.setText(User.getSingle_instance().getSign());
        }
        if(User.getSingle_instance().getSchool_id()!=null){
            school.setText(User.getSingle_instance().getSchool_id() + ":"+User.getSingle_instance().getSchool_pw());
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
        sex.setOnClickListener(new MyDialog(this));
        born.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDateDialog dialog = new CustomDateDialog(User_setting.this);
                Calendar calendar=Calendar.getInstance();
                dialog.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), new CustomDateDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month,int day) {
                        String born = year + "-" + month +"-"+day;
                        User_Service user_service = new User_Service();
                        user_service.update(born,"born",User_setting.this);
                    }
                });
                dialog.show();
            }
        });
        sign.setOnClickListener(new MyDialog(this));
        school.setOnClickListener(new MyDialog(this));

    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data!= null) {
                uri = data.getData();
                try {
                    imageView.setImageBitmap(Get_image.saveimage(this, uri));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}