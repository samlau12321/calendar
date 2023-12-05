package com.neu.calander.Adapter.Bean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.neu.calander.Service.Sql_Helper;
import com.neu.calander.Pojo.Data_List;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class Daily_List {
    private List<Data_List> daily_lists = new ArrayList();
    private static Daily_List single_instance = null;
    private Daily_List() {
    }

    private Daily_List(List<Data_List> daily_lists) {
        this.daily_lists = daily_lists;
    }

    public   static Daily_List getsignle_instance(){
        if(single_instance==null){
            single_instance = new Daily_List();
        }
        return single_instance;
    }

    public List<Data_List> getDaily_lists() {
        return daily_lists;
    }

    public void setDaily_lists(List<Data_List> daily_lists) {
        this.daily_lists = daily_lists;
    }

    public void save_daily(Context context,Data_List data_list){
        Sql_Helper sql_helper = new Sql_Helper(context);
        SQLiteDatabase db = sql_helper.getWritableDatabase();
        if (!sql_helper.test_table("daily")) {
            String create_table = "create table daily(id varchar(20), days date,content varchar(255),type varchar(255),repeat boolean,repeat_time varchar(20),target_time varchar(25));";
            db.execSQL(create_table);
        } else {
            String day = data_list.getYear()+"-"+data_list.getMonth()+"-"+data_list.getDay();
            db.execSQL("insert into daily values('"+data_list.getId()+"','"
                    + day+"','"
                    +data_list.getDesp() + "','"
                    + data_list.getType() + "','"
                    + data_list.isRepeat() + "','"
                    + data_list.getRepeat_flag()+"','"
                    + data_list.getTarget_time()
                    +"')");
        }

    }

    public void update_daily(Context context,Data_List data_list){
        Sql_Helper sql_helper = new Sql_Helper(context);
        SQLiteDatabase db = sql_helper.getWritableDatabase();

        String id = "' where id = '"+data_list.getId()+"';";
        String sql = "update daily set days='"+data_list.get_format()+id;
        db.execSQL(sql);
        String sql1= "update daily set content='" + data_list.getDesp()+id;
        db.execSQL(sql1);
        String sql2 = "update daily set type='" +data_list.getType()+id;
        db.equals(sql2);
        if(data_list.isRepeat()){
            String sql3 = "update daily set repeat = 'true"+id;
            db.execSQL(sql3);
            String sql3_1 = "update daily set repeat_time ='"+data_list.getRepeat_flag()+id;
            db.execSQL(sql3_1);
        }else{
            String sql3 = "update daily set repeat = 'false"+id;
            db.execSQL(sql3);
            String sql3_1 = "update daily set repeat_time = NULL where id = '"+data_list.getId()+"';";
            db.execSQL(sql3_1);
        }
        String sql4 = "update daily set target_time='"+data_list.getTarget_time()+id;
        db.execSQL(sql4);
    }

    public  boolean is_draw(LocalDate localDate){
        String year = String.valueOf(localDate.getYear());
        String month = String.valueOf(localDate.getMonthOfYear());
        String day = String.valueOf(localDate.getDayOfMonth());

        for(Data_List d:this.getDaily_lists()){
            if(d.getYear().equals(year)){
                if(d.getMonth().equals(month)){
                    if(d.getDay().equals(day)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void delete_daily(Context context,Data_List data_list){
        Sql_Helper sql_helper = new Sql_Helper(context);
        SQLiteDatabase db = sql_helper.getWritableDatabase();
        String sql = "delete from daily where id = '" + data_list.getId()+"';";
        db.execSQL(sql);
    }

    @SuppressLint("Range")
    public String get_id(Context context){
        Sql_Helper sql_helper = new Sql_Helper(context);
        SQLiteDatabase db = sql_helper.getWritableDatabase();
        String sql = "select max(cast(id as SIGNED)) as max from daily;";
        Cursor top = db.rawQuery(sql,null);
        String id = null;
        if (top.moveToFirst()) {
            id = top.getString(top.getColumnIndex("max"));
        }
        if(id!=null){
        return String.valueOf(Integer.parseInt(id)+1);}else{
            return "0";
        }
    }

}
