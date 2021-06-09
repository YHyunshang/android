package com.yh.base.net.http.cache.adapter;

import com.google.gson.Gson;
import com.yh.base.net.http.Code;
import com.yh.base.net.http.Https;
import com.yh.base.net.http.cache.HttpHelper;
import com.yh.base.net.http.cache.MemCacheManager;
import com.yh.base.net.http.cache.CachedInterceptor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LazyAdapter extends RootAdapter {

    public LazyAdapter() {
    }

    public LazyAdapter(long outTime) {
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
    public boolean onOutOfDate(CContext context) {
        Https.request(context.request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = HttpHelper.getBodyString(response.body());
                CachedInterceptor.Result rsp = new Gson().fromJson(responseData, CachedInterceptor.Result.class);
                if (rsp.code == Code.SUCCESS) {
                    MemCacheManager.getManager(context.url).put(context.id, responseData, System.currentTimeMillis(), -1);
                }
            }
        });
        return true;
    }

    @Override
    public void makeDirty(String url) {
        MemCacheManager.getManager(url).clear();
    }
}
