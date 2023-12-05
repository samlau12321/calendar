package com.neu.calander.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neu.calander.Adapter.Base_adapter;
import com.neu.calander.ui.Loading;
import com.neu.calander.Adapter.Bean.Bean;
import com.neu.calander.Pojo.User;
import com.neu.calander.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Thread_run {
    private final static int ac_correct_pw_correct = 1;
    private final static int ac_correct_pw_fall = 2;
    private final static int not_ac = 0;
    private List<Bean> beans = new ArrayList();
    private SQL_SERVICE SQLSERVICE = new SQL_SERVICE();
    protected User user = User.getSingle_instance();
    protected Context context;

    public Thread_run(Context context) {
        this.context = context;
    }

    public void frist_load(Activity activity) {
        new Thread(){
            @Override
            public void run() {
                Intent intent = new Intent(activity, Loading.class);
                activity.startActivity(intent);
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                SQLSERVICE.get_daily(context);
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                SQLSERVICE.get_information(context,activity);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

        }.start();

        new Thread() {
            @Override
            public void run() {
                connect_web("https://lkcmacauweb.top/Server/android/Word_List", activity);
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
            }
        }.start();
    }


    private void connect_web(String url_string, Activity activity) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(url_string);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000);
            connection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                Log.e("TAG", s);
                JSONArray jsonArray = JSON.parseArray(s);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    Bean bean = JSON.toJavaObject(obj, Bean.class);
                    beans.add(bean);
                }
            }

            bufferedReader.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (beans.size() <= 0) {
                Bean bean = new Bean();
                bean.setName("Updating");
                beans.add(bean);
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView list_view = activity.findViewById(R.id.lv);
                    list_view.setAdapter(new Base_adapter(beans, context));
                    //Toast.makeText(MainActivity.this, String.valueOf(beans.size()), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public  void connect_user(Activity activity,String ac,String pw,int type){
        new Thread() {
            @Override
            public void run() {
                User_Service user_service = new User_Service();
                int bool = user_service.check_user(ac,pw);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(bool == not_ac) {
                                Toast.makeText(activity, "用戶不存在", Toast.LENGTH_SHORT).show();
                            }
                            if(bool==ac_correct_pw_correct){
                                get_ac_information(ac,pw,activity,type);
                            }
                            if(bool==ac_correct_pw_fall){
                                Toast.makeText(activity, "密碼不存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }
        }.start();

    }

    private void get_ac_information(String ac, String pw,Activity activity,int type) {
        //type 1:buttom_click;
        //type 2:auto
        new Thread(new Runnable() {
            @Override
            public void run() {
                String android_id = Settings.Secure.getString(activity.getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                user.get_user(ac,pw,type,android_id);
                SQLSERVICE.login_save_sql(User.getSingle_instance(),activity.getApplicationContext());
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                    }
                });
            }
        }).start();
    }
}
