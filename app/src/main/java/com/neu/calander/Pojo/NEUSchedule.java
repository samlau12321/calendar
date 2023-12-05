package com.neu.calander.Pojo;

import java.util.List;

public class NEUSchedule {
    private Integer day; //星期几？
    private String name; //课程名字？
    private String position; //教室？ 体育课没有教室的哦~~~~
    private List<Integer> sections; //第几节？ 这是一个数组哦，数组里面有1和2说明这节课在第1节和第2节
    private String teacher; //授课老师？ 这里不是课程老师，而是这一次课的授课老师哦~ 两者不一样
    private List<Integer> weeks; //第几周有？ 一个数组，有1,3,5,7说明就是1,3,5,7周有

    public NEUSchedule(Integer day, String name, String position, List<Integer> sections, String teacher, List<Integer> weeks) {
        this.day = day;
        this.name = name;
        this.position = position;
        this.sections = sections;
        this.teacher = teacher;
        this.weeks = weeks;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<Integer> getSections() {
        return sections;
    }

    public void setSections(List<Integer> sections) {
        this.sections = sections;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public List<Integer> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Integer> weeks) {
        this.weeks = weeks;
    }

    public NEUSchedule() {
    }

    @Override
    public String toString() {
        return "NEUSchedule{" +
                "day=" + day +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", sections=" + sections +
                ", teacher='" + teacher + '\'' +
                ", weeks=" + weeks +
                '}';
    }
}
