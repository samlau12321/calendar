package com.neu.calander.Pojo;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class User {
    private final static int ac_correct_pw_correct = 1;
    private final static int ac_correct_pw_fall = 2;
    private final static int not_ac = 0;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //用戶密碼
    private String password;
    //用戶ID
    private String id;
    //用戶名字;
    private String name;
    //用戶頭像
    private String icon;
    //用戶學校學號
    private String school_id;
    //用戶學校登入網頁
    private String school_pw;
    //用戶性別
    private String sex;
    //用戶出生年份
    private String born;
    //用戶簽名
    private String sign;

    private int like = 0;
    private int focus = 0;
    private int fans = 0;
    private boolean is_login = false;
    //連接密碼
    private String connect_password;

    public boolean isIs_login() {
        return is_login;
    }

    public void setIs_login(boolean is_login) {
        this.is_login = is_login;
    }



    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    private static User single_instance = null;

    public static User getSingle_instance() {
        if (single_instance == null) {
            single_instance = new User();
        }
        return single_instance;
    }

    private User() {
        this.id = null;
        this.name = null;
        this.school_id = null;
        this.school_pw = null;
        this.born = "0000-00-00";
        this.sex = "未知";
        this.sign = null;

    }



    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchool_pw() {
        return school_pw;
    }

    public void setSchool_pw(String school_pw) {
        this.school_pw = school_pw;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getConnect_password() {
        return connect_password;
    }

    public void setConnect_password(String connect_password) {
        this.connect_password = connect_password;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", school_id='" + school_id + '\'' +
                ", school_pw='" + school_pw + '\'' +
                ", sex='" + sex + '\'' +
                ", born='" + born + '\'' +
                ", sign='" + sign + '\'' +
                ", like=" + like +
                ", focus=" + focus +
                ", fans=" + fans +
                ", is_login=" + is_login +
                ", connect_password='" + connect_password + '\'' +
                '}';
    }
    public void get_user(String ac,String pw,int type,String android_id){
        String scid= null;
        String scpw = null;
        if(User.getSingle_instance().getSchool_id()!=null){
            scid =User.getSingle_instance().getSchool_id();
        }
        if(User.getSingle_instance().getSchool_pw()!=null){
            scpw = User.getSingle_instance().getSchool_pw();
        }
        String get = ac + ":" + pw + ":"+this.getConnect_password()+":"+String.valueOf(type)+":"+android_id;
        URL url = null;
        HttpURLConnection connection = null;
        String result = null;
        try {
            url = new URL("https://lkcmacauweb.top/Server/android/get_user_information");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(200);
            connection.setDoOutput(true);
            connection.connect();
            OutputStream out = connection.getOutputStream();
            out.write(get.getBytes(),0,get.getBytes().length);
            out.flush();
            out.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String s;
            while ((s=bufferedReader.readLine())!=null) {
                result = s;
                Log.e("click",result );
            }
            bufferedReader.close();
            connection.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        if(result!=null){
            single_instance = JSON.parseObject(result,User.class);
            single_instance.setPassword(pw);
            if(scid!=null){
                single_instance.setSchool_pw(scid);
            }
            if(scpw!=null){
                single_instance.setSchool_pw(scpw);
            }
            single_instance.setIs_login(true);}
        else{
            single_instance.setIs_login(false);
        }

    }
    public void quit(){
        User.getSingle_instance().single_instance=new User();
    }

}
