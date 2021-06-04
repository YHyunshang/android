package com.yh.base.net.bean;

public class Rsp<T> {

    private int code;
    private String message;
    private T result;
    private T page;
    private int c;

    public boolean isCache() {
        return c > 0;
    }

    public int getCache() {
        return c;
    }

    public void setCache(int cache) {
        this.c = cache;
    }

    public T getPage() {
        return page;
    }

    public void setPage(T page) {
        this.page = page;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result != null ? result : page;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

