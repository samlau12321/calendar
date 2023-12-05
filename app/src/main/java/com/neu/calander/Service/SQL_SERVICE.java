package com.neu.calander.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.calander.Adapter.Bean.Daily_List;
import com.neu.calander.Pojo.Data_List;
import com.neu.calander.Pojo.User;
import com.neu.calander.R;
import com.neu.calander.Util.Call_Util;
import com.neu.calander.Util.ImageTobit;
import com.neu.calander.Util.tool;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SQL_SERVICE {
    User_Service user_service = new User_Service();
    @SuppressLint("Range")
    public void get_daily(Context context) {
        Sql_Helper sql_helper = new Sql_Helper(context);
        SQLiteDatabase db = sql_helper.getWritableDatabase();
        if (!sql_helper.test_table("daily")) {
            String create_table = "create table daily(id varchar(20), days date,content varchar(255),type varchar(255),repeat boolean,repeat_time varchar(20),target_time varchar(25));";
            db.execSQL(create_table);
        } else {
            Cursor top = db.rawQuery("select * from daily;", null);
            List<Data_List> lists = new ArrayList();
            if (top.moveToFirst()) {
                do {
                   String id = top.getString(top.getColumnIndex("id"));
                     String days = top.getString(top.getColumnIndex("days"));
                     String desp = top.getString(top.getColumnIndex("content"));
                     String type = top.getString(top.getColumnIndex("type"));
                     String repeat = top.getString(top.getColumnIndex("repeat"));
                     String taget_time = top.getString(top.getColumnIndex("target_time"));
                    String[] token = days.split("-");
                    boolean re  = false;
                    String repeat_time = null;
                    if(repeat.equals("true")) {
                        re = true;
                        repeat_time = top.getString(top.getColumnIndex("repeat_time"));
                    }
                    Data_List daily = new Data_List(token[0],token[1],token[2],desp,id,type,re,taget_time,repeat_time);
                    lists.add(daily);
                } while (top.moveToNext());
                Daily_List.getsignle_instance().setDaily_lists(lists);
                if(Call_Util.getInstance().getClock_flag()==0){
                    Call_Util.getInstance().OnStart();
                }
            }
        }
    }
    //auto
    public void get_information(Context context, Activity activity) {
        Sql_Helper sql_helper = new Sql_Helper(context);
        SQLiteDatabase db = sql_helper.getWritableDatabase();
        if (!sql_helper.test_table("User")) {
            String create_table = "create table User(pw varchar(20),name varchar(20),login varchar(20),school_id varchar(20),school_pw varchar(20));";
            db.execSQL(create_table);
        }
            Cursor top = db.rawQuery("select * from User;", null);
            if (top.moveToFirst()) {
                do {
                    @SuppressLint("Range") String is_login = top.getString(top.getColumnIndex("login"));
                    if(is_login.equals("true")) {
                        @SuppressLint("Range") String name = top.getString(top.getColumnIndex("name"));
                        @SuppressLint("Range") String pw = top.getString(top.getColumnIndex("pw"));
                        @SuppressLint("Range") String school_id = top.getString(top.getColumnIndex("school_id"));
                        @SuppressLint("Range") String school_pw = top.getString(top.getColumnIndex("school_pw"));
                        User user = User.getSingle_instance();
                        int flag = user_service.check_user(name,pw);
                        if(flag==1) {
                            user.get_user(name, pw, 2, tool.getSingle_instance().getAndroid_id());
                            user = User.getSingle_instance();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(User.getSingle_instance().getId()!=null) {
                                        Toast.makeText(activity, "已自動登入", Toast.LENGTH_SHORT).show();
                                        TextView textView = activity.findViewById(R.id.user_name);
                                        textView.setText(User.getSingle_instance().getName());
                                        ImageView imageView = activity.findViewById(R.id.my_image);
                                        if (User.getSingle_instance().getIcon() != null) {
                                            try {
                                                imageView.setImageBitmap(ImageTobit.StringToBitmap(User.getSingle_instance().getIcon()));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        User.getSingle_instance().setIs_login(true);
                                    }
                                }
                            });
                        }
                            if(!school_id.equals("null")) user.setSchool_id(school_id);
                            if(!school_pw.equals("null")) user.setSchool_pw(school_pw);

                    }
                } while (top.moveToNext());
            }
        }

    public boolean Is_Use_school_Net() {
        URL url = null;
        Boolean is_connection = false;
        HttpURLConnection connection = null;
        try {
            url = new URL("http://219.216.96.4/eams");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(1000);
            connection.connect();
            is_connection = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is_connection;
    }

    public void login_save_sql(User user,Context context){
        Sql_Helper sql_helper = new Sql_Helper(context);
        SQLiteDatabase db = sql_helper.getWritableDatabase();
        if (sql_helper.test_table("User")) {
            String drop_table = "drop table User";
            db.execSQL(drop_table);
            String create_table = "create table User(pw varchar(20),name varchar(20),login varchar(20),school_id varchar(20),school_pw varchar(20));";
            db.execSQL(create_table);
            String inseet = "insert into User values('" +user.getPassword()+ "','" +user.getName()+ "','true'"+",'"+user.getSchool_id()+"','" +user.getSchool_pw()+"');";
            db.execSQL(inseet);
        }else{
            String create_table = "create table User(pw varchar(20),name varchar(20),login varchar(20),school_id varchar(20),school_pw varchar(20));";
            db.execSQL(create_table);
        }
    }

    public void quit_login(Context context){
        Sql_Helper sql_helper = new Sql_Helper(context);
        SQLiteDatabase db = sql_helper.getWritableDatabase();
        if (sql_helper.test_table("User")) {
            String drop_table = "drop table User";
            db.execSQL(drop_table);
            String create_table = "create table User(pw varchar(20),name varchar(20),login varchar(20),school_id varchar(20),school_pw varchar(20));";
            db.execSQL(create_table);
            if(User.getSingle_instance().getSchool_id()!=null && User.getSingle_instance().getSchool_pw()!=null){
                String inseet = "insert into User(school_id,school_pw) values('" +User.getSingle_instance().getSchool_id()+"','" +User.getSingle_instance().getSchool_pw()+"');";
                db.execSQL(inseet);
            }

        }
    }
}
