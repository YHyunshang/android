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
            str = MMKVUiManager.Companion.getCityCode();
            if (!TextUtils.isEmpty(str))
                builder.addHeader("cityId", str);
            str = MMKVUiManager.Companion.getCity();
            if (!TextUtils.isEmpty(str))
                builder.addHeader("cityName", URLEncoder.encode(str, "utf-8"));
            double dou = MMKVUiManager.Companion.getLatitude();
            if (dou != 0.0)
                builder.addHeader("latitude", String.valueOf(dou));
            dou = MMKVUiManager.Companion.getLongitude();
            if (dou != 0.0)
                builder.addHeader("longitude", String.valueOf(dou));
            Request request = builder.build();
            return chain.proceed(request);
        }
    }

    static final String[] cityNameHeader = new String[]{"cityName"};

    static final String[] cityNameTokenHeader = new String[]{"cityName", "sign"};

    static final LastAdapter lastAdapter = new LastAdapter();
    static final PageAdapter pageAdapter = new PageAdapter();
    static final TimedAdapter timedAdapter = new TimedAdapter(3 * 60 * 1000);
    static final LazyAdapter lazyAdapter = new LazyAdapter(3 * 60 * 1000);
    static final RefreshAdapter refreshNow = new RefreshAdapter(0);
    static final RefreshAdapter refreshNowToken = new RefreshAdapter(0);
    static final RefreshAdapter refreshAdapter = new RefreshAdapter(3 * 60 * 1000);

    static final RefreshAdapter refreshAdapterNoCity = new RefreshAdapter(3 * 60 * 1000);
    static final RefreshAdapter refreshNowNoCity = new RefreshAdapter(0);

    static {
        lastAdapter.setHeader(cityNameHeader);
        pageAdapter.setHeader(cityNameHeader);
        timedAdapter.setHeader(cityNameHeader);
        lazyAdapter.setHeader(cityNameHeader);
        refreshNow.setHeader(cityNameHeader);
        refreshAdapter.setHeader(cityNameHeader);
        refreshNowToken.setHeader(cityNameTokenHeader);
    }

    public static void init() {
        Https.addInterceptor(new TokenInterceptor());
        CacheManager.add(UrlUtil.getUrl() + "/item/detail", refreshNow);
        CacheManager.add(UrlUtil.getUrl() + "/shop/detail", refreshNow);
        CacheManager.add(UrlUtil.getUrl() + "/common/city/openingList", refreshNowNoCity);

        CacheManager.add(UrlUtil.getUrl() + "/home/mktbanners", refreshNow);
        CacheManager.add(UrlUtil.getUrl() + "/home/operationPositions", refreshNow);
        CacheManager.add(UrlUtil.getUrl() + "/category/tree", refreshNow);
        CacheManager.add(UrlUtil.getUrl() + "/home/favorite/items", refreshNow);
        CacheManager.add(UrlUtil.getUrl() + "/home/recommend/items", refreshNow);
        CacheManager.add(UrlUtil.getUrl() + "/home/recommend/shops", refreshNow);

        CacheManager.add(UrlUtil.getUrl() + "/category/getItemsByShopId", refreshNowToken);
//        CacheManager.add(UrlUtil.getUrl() + "/category/items", refreshNowToken);

        CacheManager.add(UrlUtil.getUrl() + "/home/tels", timedAdapter);


    }
}
