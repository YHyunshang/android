package com.yh.base.net.http.cache;

import com.google.gson.Gson;
import com.yh.base.constant.Config;
import com.yh.base.net.http.Code;
import com.yh.base.net.http.MediaTypes;
import com.yh.base.net.http.cache.CacheManager;
import com.yh.base.net.http.cache.Entry;
import com.yh.base.net.http.cache.MemCacheManager;
import com.yh.base.net.http.cache.adapter.CContext;
import com.yh.base.net.http.cache.adapter.RootAdapter;
import com.yh.base.utils.LogUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CachedInterceptor implements Interceptor {
    public boolean useCache;

    public CachedInterceptor(boolean useCache) {
        this.useCache = useCache;
    }

    private static String TAG = "cache-tob";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String fullUrl = request.url().toString();
        int idx = fullUrl.indexOf('?');
        String url;
        if (idx > 0) {
            url = fullUrl.substring(0, idx);
        } else {
            url = fullUrl;
        }
        if (Config.isIsDebug()) {
            LogUtils.tag(TAG).d("req:url=" + fullUrl);
            Headers headers = request.headers();
            for (String key : headers.names()) {
                String value = headers.get(key);
                LogUtils.tag(TAG).d("req:" + key + ":" + value);
            }
        }
        String bodyData = HttpHelper.getBodyString(request.body());
        if (Config.isIsDebug()) {
            String mockedData = CacheManager.getMocked(fullUrl, bodyData);
            if (mockedData != null) {
                return new Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("read from mock")
                        .body(ResponseBody.create(MediaTypes.Json, mockedData))
                        .build();
            }
        }
        if (Config.isIsDebug()) LogUtils.tag(TAG).d("req:" + bodyData);
        CContext context = new CContext();
        context.setRequest(request);
        context.setUrl(url);
        context.setBodyData(bodyData);
        RootAdapter adapter = CacheManager.getRuleAdapter(url);
        if (adapter != null) {
            context.setId(adapter.computeID(context));
            if (Config.isIsDebug())
                LogUtils.tag(TAG).d("adapter key:" + context.getUrl() + ":" + context.getId());
            if (useCache) {
                Entry entry = MemCacheManager.getManager(context.getUrl()).get(context.getId());
                if (entry != null) {
                    if (System.currentTimeMillis() - entry.getTime() < adapter.getCacheTime()) {
                        if (Config.isIsDebug())
                            LogUtils.tag(TAG).d("read from cache:" + (System.currentTimeMillis() - entry.getTime()));
                        boolean use = adapter.onCache(context);
                        if (use) {
                            return new Response.Builder()
                                    .request(request)
                                    .protocol(Protocol.HTTP_1_1)
                                    .code(200)
                                    .message("read from cache")
                                    .body(ResponseBody.create(MediaTypes.Json, "{\"c\":1," + entry.getValue().substring(1)))
                                    .build();
                        }
                    } else {
                        if (Config.isIsDebug())
                            LogUtils.tag(TAG).d("cache out of date:" + (System.currentTimeMillis() - entry.getTime()));
                        if (adapter.onOutOfDate(context)) {
                            LogUtils.tag(TAG).d("but...return local");
                            return new Response.Builder()
                                    .request(request)
                                    .protocol(Protocol.HTTP_1_1)
                                    .code(200)
                                    .message("read from cache(out of date)")
                                    .body(ResponseBody.create(MediaTypes.Json, "{\"c\":2," + entry.getValue().substring(1)))
                                    .build();
                        }
                    }
                } else {
                    if (Config.isIsDebug()) LogUtils.tag(TAG).d("there is no cache");
                }
            } else {
                if (Config.isIsDebug()) LogUtils.tag(TAG).d("cache is closed");
            }
        }
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Response response = chain.proceed(request);
        if (adapter != null) {
            try {
                String responseData = HttpHelper.getBodyString(response.body());
//            if (Config.isIsDebug()) LogUtils.tag(TAG).d("rsp:" + responseData);
                Result rsp = new Gson().fromJson(responseData, Result.class);
                if (rsp != null) {
                    if (rsp.code == Code.SUCCESS) {
                        if (Config.isIsDebug())
                            LogUtils.tag(TAG).d("success:" + rsp.code + " cache updated.");
                        adapter.onResult(context, responseData);
                    } else {
                        if (Config.isIsDebug()) LogUtils.tag(TAG).d("code fail:" + rsp.code);
                    }
                }
            } catch (Exception e) {
                MemCacheManager.getManager(context.getUrl()).remove(context.getId());
            }
        }
        return response;
    }

    public class Result {
        public int code;
//        public int page;
//
//        public class Page {
//            public int pageNo;
//            public int pageSize;
//            public int totalNum;
//        }
    }
}
