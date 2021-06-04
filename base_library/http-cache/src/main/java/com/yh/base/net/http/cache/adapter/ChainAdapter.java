package com.yh.base.net.http.cache.adapter;

import com.yh.base.net.http.cache.CacheManager;
import com.yh.base.net.http.cache.MemCacheManager;

import java.util.List;

public class ChainAdapter extends RootAdapter {
    public ChainAdapter() {
    }

    public ChainAdapter(long outTime) {
        this.outTime = outTime;
    }

    @Override
    public boolean onCache(CContext context) {
        return false;
    }

    @Override
    public void onResult(CContext context, String responseData) {
        for (String urlStr : urls) {
            RootAdapter adapter = CacheManager.getRuleAdapter(urlStr);
            adapter.makeDirty(urlStr);
        }
    }

    @Override
    public void makeDirty(String url) {
        MemCacheManager.getManager(url).clear();
    }


    List<String> urls;

    public ChainAdapter(List<String> urls) {
        this.urls = urls;
    }
}