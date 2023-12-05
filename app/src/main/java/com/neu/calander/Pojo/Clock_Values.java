package com.neu.calander.Pojo;

public class Clock_Values {
    // 周期性的闹钟
    public final static String TIMER_ACTION_REPEATING = "com.e_eduspace.TIMER_ACTION_REPEATING";
    // 定时闹钟
    public final static String TIMER_ACTION = "com.e_eduspace.TIMER_ACTION";

    private static int flag;

    public static int getFlag() {
        return flag;
    }

    public static void setFlag(int flag) {
        Clock_Values.flag = flag;
    }
}
