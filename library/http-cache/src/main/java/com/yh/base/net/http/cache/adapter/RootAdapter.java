package com.yh.base.net.http.cache.adapter;

import com.yh.base.utils.SecurityUtil;

public abstract class RootAdapter {

    protected long outTime = 60 * 1000;
    String[] headers;

    public void setHeader(String[] headers) {
        this.headers = headers;
    }

    public long getCacheTime() {
        return outTime;
    }

    public String computeID(CContext context) {
        StringBuilder sb = new StringBuilder();
        if (headers != null) {
            for (String header : headers) {
                sb.append(context.request.header(header));
            }
        }
        return SecurityUtil.getMD5((context.request.method() + context.request.url().query() + sb + context.bodyData).getBytes());
    }

    public abstract boolean onCache(CContext context);

    public boolean onOutOfDate(CContext context) {
        return false;
    }

    public abstract void onResult(CContext context, String responseData);

    public abstract void makeDirty(String key);
}
