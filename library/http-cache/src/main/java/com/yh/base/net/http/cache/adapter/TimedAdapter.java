package com.yh.base.net.http.cache.adapter;

import com.yh.base.net.http.cache.MemCacheManager;

public class TimedAdapter extends RootAdapter {

    public TimedAdapter() {
    }

    public TimedAdapter(long outTime) {
        this.outTime = outTime;
    }

    @Override
    public boolean onCache(CContext context) {
        return true;
    }

    @Override
    public void onResult(CContext context, String responseData) {
        MemCacheManager.getManager(context.getUrl()).put(context.id, responseData, System.currentTimeMillis(), -1);
    }

    @Override
    public void makeDirty(String url) {
        MemCacheManager.getManager(url).clear();
    }
}
