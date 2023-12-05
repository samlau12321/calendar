package com.neu.calander.Pojo;

public class Data_List {
    private String year;
    private String month;
    private String day;
    private String desp;
    private String id;
    private String type;
    private boolean repeat;
    private String target_time;
    private String repeat_flag;





    public Data_List() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public Data_List(String year, String month, String day, String desp, String id, String type, boolean repeat) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.desp = desp;
        this.id = id;
        this.type = type;
        this.repeat = repeat;
    }

    public Data_List(String year, String month, String day, String desp, String id, String type, boolean repeat, String target_time, String repeat_flag) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.desp = desp;
        this.id = id;
        this.type = type;
        this.repeat = repeat;
        this.target_time = target_time;
        this.repeat_flag = repeat_flag;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTarget_time() {
        return target_time;
    }

    public void setTarget_time(String target_time) {
        this.target_time = target_time;
    }

    public String getRepeat_flag() {
        return repeat_flag;
    }

    public void setRepeat_flag(String repeat_flag) {
        this.repeat_flag = repeat_flag;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_format(){
        String re = String.valueOf(getYear()) + "-" + String.valueOf(getMonth()) + "-" + String.valueOf(getDay());
        return re;
    }
}
