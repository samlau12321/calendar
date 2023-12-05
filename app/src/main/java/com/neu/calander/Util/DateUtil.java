package com.neu.calander.Util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1; //从0开始算的：一月份是0，12月份是11
    }

    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    //获取某年某月的1号是星期几
    public static int getFirstdayWeekOfMonth(int year,int month) {            //日 一 二 三 四 五 六
        Calendar calendar = Calendar.getInstance();                           //0  1  2  3  4  5 6
        calendar.set(year, month - 1, 1);

        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        weekDay = weekDay - 1;
        return weekDay;
    }

    //某年某月有多少天
    public static int getDaysInMonth(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

   //是否为同一天
    public static boolean isSameDay(Date date1,Date date2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(date1);
        String ds2 = sdf.format(date2);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }


   //是否为周末
    public static boolean isWeekend(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == 1 || week == 7){
            return true;
        }

        return false;
    }

    //星期天第一列，从1开始
    public static String stringOfWeek(int week){
        String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
        return weeks[week-1];
    }

    public static String get_today(){
        String [] weeks = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
        String today;
        Calendar calendar = Calendar.getInstance();
        int week_day;
        week_day = calendar.get(Calendar.DAY_OF_WEEK) -2;
        if(week_day<0){
            week_day+=7;
        }
        today = String.valueOf(calendar.get(Calendar.YEAR)) + "-" + String.valueOf(calendar.get(Calendar.MONTH) +1) + "-"
        + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " + weeks[week_day ];
        return today;
    }
    public static String get_week_of_date(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    public static int get_week_of_date_int(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }
}