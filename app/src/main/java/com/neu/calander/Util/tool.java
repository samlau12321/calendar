package com.neu.calander.Util;

public class tool {
    private   String Android_id;
    private int show;
    public String getAndroid_id() {
        return Android_id;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public void setAndroid_id(String android_id) {
        Android_id = android_id;
    }
    private static tool single_instance = null;

    public static tool getSingle_instance() {
        if (single_instance == null) {
            single_instance = new tool();
        }
        return single_instance;
    }

}
