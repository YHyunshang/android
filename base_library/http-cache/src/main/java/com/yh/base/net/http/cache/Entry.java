package com.yh.base.net.http.cache;

public class Entry {
    String value;
    long time;
    int pageNo;

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Entry(String value, long lastTime, int pageNo) {
        this.value = value;
        this.time = lastTime;
        this.pageNo = pageNo;
    }
}