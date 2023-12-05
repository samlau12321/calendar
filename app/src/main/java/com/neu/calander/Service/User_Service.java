package com.neu.calander.Service;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.calander.Pojo.User;
import com.neu.calander.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class User_Service {
    User user = User.getSingle_instance();


    public int check_user(String ac, String pw) {
        URL url = null;
        int flag=0;
        HttpURLConnection connection = null;
        String string = ac+":"+pw;
        try {
            url = new URL("https://lkcmacauweb.top/Server/android/check_user");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(1000);
            connection.setDoOutput(true);
            connection.connect();
            OutputStream out = connection.getOutputStream();
            out.write(string.getBytes(),0,string.getBytes().length);
            out.flush();
            out.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String s;
            while ((s=bufferedReader.readLine())!=null) {
                String[] token = s.split(":");
                flag = Integer.valueOf(token[0]);
                user.setConnect_password(token[1]);
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
        return flag;
    }

    public void get_user(String ac,String pw,int type,String android_id){
        User.getSingle_instance().get_user(ac,pw,type,android_id);
    }

    public void register(String ac, String pw, Activity activity){
        String reg;
        reg = ac + ":" + pw;

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                HttpURLConnection connection = null;
                String s = null;
                try {
                    url = new URL("https://lkcmacauweb.top/Server/android/reg_user");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(1000);
                    connection.setDoOutput(true);
                    connection.connect();
                    OutputStream out = connection.getOutputStream();
                    out.write(reg.getBytes(),0,reg.getBytes().length);
                    out.flush();
                    out.close();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line=bufferedReader.readLine())!=null) {
                        s = line;
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

                String finalS = s;
                Log.e("checks", finalS);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(finalS.equals("true")){
                            Toast.makeText(activity, "注冊成功", Toast.LENGTH_SHORT).show();
                            activity.finish();return;
                        }
                        else{
                            Toast.makeText(activity, "用戶已存在", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

            }

        }).start();
    }

    public boolean update(String name,String method,Activity activity){
        String output= name+":" + user.getId() + ":" + method;
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                HttpURLConnection connection = null;
                String s = "";
                try {
                    url = new URL("https://lkcmacauweb.top/Server/android/update_user");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(1000);
                    connection.setDoOutput(true);
                    connection.connect();
                    OutputStream out = connection.getOutputStream();
                    out.write(output.getBytes(), 0, output.getBytes().length);
                    out.flush();
                    out.close();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (!line.equals("null")) {
                            s += line;
                        }
                    }
                    bufferedReader.close();
                    connection.disconnect();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                }
                if (s != null) {
                    Log.e("loge", s);
                    if (s.equals("success")) {
                        if (method.equals("name")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                                    TextView textView = activity.findViewById(R.id.name);
                                    textView.setText(User.getSingle_instance().getName());
                                }
                            });

                            User.getSingle_instance().setName(name);
                            SQL_SERVICE SQLSERVICE = new SQL_SERVICE();
                            SQLSERVICE.login_save_sql(User.getSingle_instance(), activity);
                        }
                        if (method.equals("sex")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                                    TextView textView = activity.findViewById(R.id.sex_2);
                                    textView.setText(name);
                                }
                            });
                            User.getSingle_instance().setSex(name);

                        }
                        if (method.equals("born")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                                    TextView textView = activity.findViewById(R.id.born);
                                    textView.setText(name);
                                }
                            });
                            User.getSingle_instance().setBorn(name);
                        }
                        if (method.equals("sign")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                                    TextView textView = activity.findViewById(R.id.sign);
                                    textView.setText(name);
                                }
                            });
                            User.getSingle_instance().setSign(name);
                        }
                    } else {
                        if (method.equals("name")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "用戶已存在", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "修改失敗", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        }).start();
        return false;
    }
}
