package com.yh.trading.http;

import android.text.TextUtils;

import com.yh.base.net.http.Https;
import com.yh.base.net.http.cache.CacheManager;
import com.yh.base.net.http.cache.adapter.LastAdapter;
import com.yh.base.net.http.cache.adapter.LazyAdapter;
import com.yh.base.net.http.cache.adapter.PageAdapter;
import com.yh.base.net.http.cache.adapter.RefreshAdapter;
import com.yh.base.net.http.cache.adapter.TimedAdapter;
import com.yh.base.utils.Util;
import com.yh.trading.constant.MMKVUiManager;
import com.yh.trading.utils.UrlUtil;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;

public class HttpUtils {
    private static class TokenInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            String softVersion = Util.getVersionName();
            Request.Builder builder = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "" + "application/json; charset=UTF-8")
                    .addHeader("platform", "android")
                    .addHeader("version", softVersion)
                    .addHeader("tenant", "601933")
                    .addHeader("channel", "APP")
                    .addHeader("timestamp", String.valueOf(System.currentTimeMillis()));

            String str = MMKVUiManager.Companion.getToken();
            if (!TextUtils.isEmpty(str))
                builder.addHeader("sign", str);
            Request request = builder.build();
            return chain.proceed(request);
        }
    }

    static final String[] signHeader = new String[]{"sign"};
    static final TimedAdapter timedAdapter = new TimedAdapter(20 * 1000);

    static {
        timedAdapter.setHeader(signHeader);//添加header识别（作为主键用于缓存）
    }

    public static void init() {
        Https.addInterceptor(new TokenInterceptor());
        CacheManager.add(UrlUtil.getUrl() + "/home/tels", timedAdapter);
    }
}
