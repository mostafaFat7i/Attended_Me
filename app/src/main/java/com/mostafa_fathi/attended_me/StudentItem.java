package com.mostafa_fathi.attended_me;

public class StudentItem {
    private String name,status="";
    private long sid;
    private int roll;


    public StudentItem(long sid, int roll, String name) {
        this.roll = roll;
        this.name = name;
        this.sid=sid;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }
}
