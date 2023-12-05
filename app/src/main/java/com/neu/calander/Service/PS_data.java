package com.neu.calander.Service;

import com.alibaba.fastjson.JSONObject;
import com.neu.calander.Adapter.Bean.Post_Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PS_data {
    private static boolean is_loading =false;
    private static int loadtime = 0;

    public int getLoadtime() {
        return loadtime;
    }

    public void setLoadtime(int loadtime) {
        this.loadtime = loadtime;
    }

    public boolean isIs_loading() {
        return is_loading;
    }

    public void setIs_loading(boolean is_loading) {
        this.is_loading = is_loading;
    }

    static List<Post_Data> lists = new ArrayList();
    private static PS_data Instance = null;
    public static PS_data getInstance(){
        if(Instance==null){
            Instance = new PS_data();
        }
        return Instance;
    }

    private PS_data() {
    }

    public List<Post_Data> getLists() {
        if(lists==null){
            lists = new ArrayList();
        }
        return lists;
    }

    public void get_post(){
        PS_data.getInstance().setIs_loading(true);
        String get = String.valueOf(lists.size());
        URL url = null;
        HttpURLConnection connection = null;
        String result = "";
        try {
            url = new URL("https://lkcmacauweb.top/Server/android/get_post");
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
                result += s;
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
        if(result!=null) {
            if (!result.equals("null")) {
                List<Post_Data> post_datas = JSONObject.parseArray(result, Post_Data.class);
                if(post_datas!=null) {
                    for (Post_Data p : post_datas) {
                        PS_data.getInstance().getLists().add(0, p);
                    }
                }
            }
        }
        //Log.e("TAG",PS_data.getInstance().getLists().get(3).getUser_name());
        PS_data.getInstance().setIs_loading(false);
        PS_data.getInstance().setLoadtime(PS_data.getInstance().getLoadtime()+1);
    }
}
