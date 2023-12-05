package com.neu.calander.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.neu.calander.Service.CustomAlarmManager;
import com.neu.calander.ui.MainActivity;
import com.neu.calander.Adapter.Bean.Daily_List;
import com.neu.calander.Pojo.Data_List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

public class Call_Util {
    private static Call_Util Instance;
    private int clock_flag;


    public int getClock_flag() {
        return clock_flag;
    }

    public void setClock_flag(int clock_flag) {
        this.clock_flag = clock_flag;
    }

    public  static Call_Util getInstance(){
        if(Instance == null){
            Instance=new Call_Util();
        }
        return Instance;
    }
    private Context context;
    public void SetMain(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    public void setContext(Context context){
        this.context = context;
    }
    private Call_Util(){}
    private MainActivity mainActivity;
    private Calendar mCalendar;

    public boolean check_daily(Data_List data_list){
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();
        //让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //时区改為8个小时
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置時間提醒
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            sdf.parse(data_list.get_format()+" " + data_list.getTarget_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mCalendar = sdf.getCalendar() ;
        Log.e(TAG,mCalendar.getTime().toString());
        //获取毫秒值
        long selectTime = mCalendar.getTimeInMillis();
        if(selectTime > systemTime){
            return true;
        }
        return false;
    }
    /**
     * 开启提醒
     * @param activity 是指主頁面 為了去調取BoardCast
     * @param data_list 得到要反應的數據
     */

    /**
     *
     * @ ELAPSED_REALTIME 不唤醒设备
     * @ ELAPSED_REALTIME_WAKEUP 唤醒设备(關機也执行operation所对应的组件)
     * @RTC 系统调用System.currentTimeMillis()方法返回的值与triggerAtTime相等时启动operation所对应的设备
     *
     */
    public void startRemind(Data_List data_list){
        //得到日历实例，主要是为了下面的获取时间
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();
        //让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //时区改為8个小时
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置時間提醒
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            sdf.parse(data_list.get_format()+" " + data_list.getTarget_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mCalendar = sdf.getCalendar() ;
        //获取毫秒值
        long selectTime = mCalendar.getTimeInMillis();
        //AlarmReceiver.class为广播接受者
        if(clock_flag==0) {
            Intent intent = new Intent(context, CustomAlarmManager.class);
            intent.putExtra("id", data_list.getId());
            PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(data_list.getId()), intent, 0);
            //得到AlarmManager实例
            AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            // 如果当前时间大于设置的时间，判斷是否重覆
            long time = 0;
            if (selectTime > systemTime) {

                if (data_list.isRepeat()) {
                    if(data_list.getRepeat_flag().equals("周")){
                        time = 604800000;
                    }
                    if(data_list.getRepeat_flag().equals("日"))time= 86400000;
                    if(data_list.getRepeat_flag().equals("月")){
                        am.setRepeating(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis(),86400000*30,pi);
                    }else{
                    am.setRepeating(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis(),time,pi);}
                } else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                        am.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
                    } else {
                        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
                    }
                }
            }
        }else{
            try {
                CalendarReminderUtils.addCalendarEvent(context, data_list.getDesp(), data_list.getType(), mCalendar.getTimeInMillis(), 0,data_list);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }

    /**

     * 关闭提醒

     */
    public void stopRemind(Data_List data_list,String title) {
        if(Call_Util.getInstance().getClock_flag()==0) {
            Intent intent = new Intent(mainActivity, CustomAlarmManager.class);
            PendingIntent pi = PendingIntent.getBroadcast(mainActivity, Integer.parseInt(data_list.getId()),
                    intent, 0);
            AlarmManager am = (AlarmManager) mainActivity.getSystemService(ALARM_SERVICE);
//取消警报
            am.cancel(pi);
        }else{
            CalendarReminderUtils.deleteCalendarEvent(mainActivity,title+data_list.getId(),data_list);
        }


    }

    public void OnStart(){
        for(Data_List d : Daily_List.getsignle_instance().getDaily_lists()){
            if(clock_flag==1){
                Log.d(TAG, "OnStart: ");
                startRemind(d);
            }else{
                if(check_daily(d)){
                    startRemind(d);
                }
            }
        }
    }
    public void removeall(){
        for(Data_List d:Daily_List.getsignle_instance().getDaily_lists()){
            if(clock_flag==0) {
                stopRemind(d,null );
            }else{
                stopRemind(d,d.getDesp());
            }
        }
    }

}
