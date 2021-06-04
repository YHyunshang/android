package com.yh.base.net.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yh.base.constant.Config;
import com.yh.base.net.http.factory.MyRxJava2CallAdapterFactory;
import com.yh.base.net.http.interceptor.HttpLogger;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
Https.serviceRx(UrlUtil.getLoginUrl(), LoginApi.class)
                .getCityList(Https.toPost(req))
                .subscribe(new CB<Rsp<String>>() {
                });
* */
public class Https {
//    static HttpLoggingInterceptor logInterceptor;

    static List<Interceptor> interceptors = new ArrayList<>();

    public static void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public static void init() {
        OkHttpClient.Builder okHttpClientBuilder = createOkHttpClientBuilder();
        if (Config.isIsDebug()) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(logInterceptor);
        }
        secondaryHttpClient = okHttpClientBuilder.build();
    }

    static ConcurrentHashMap<String, Object> serviceMap = new ConcurrentHashMap<>();

    public static <T> T service(String url, Class<T> cls, boolean useCache, boolean useRx) {
        StringBuilder sb = new StringBuilder(url + cls.getName());
        if (useRx) sb.append("!Rx");
        if (useCache) sb.append("!C");
        String key = sb.toString();
        Object obj = serviceMap.get(key);
        if (obj == null) {
            synchronized (serviceMap) {
                obj = serviceMap.get(key);
                if (obj == null) {
                    obj = retrofit(url, useRx, useCache).create(cls);
                    serviceMap.put(key, obj);
                }
            }
        }
        return (T) obj;
    }

    public static <T> T service(String url, Class<T> cls, boolean useCache) {
        return service(url, cls, useCache, false);
    }

    public static <T> T service(String url, Class<T> cls) {
        return service(url, cls, false);
    }

    static ConcurrentHashMap<String, Retrofit> retrofitMap = new ConcurrentHashMap<>();

    private static Retrofit retrofit(String url, boolean useRx, boolean useCache) {
        StringBuilder sb = new StringBuilder(url);
        if (useRx) sb.append("!Rx");
        if (useCache) sb.append("!C");
        String key = sb.toString();
        Retrofit obj = retrofitMap.get(key);
        if (obj == null) {
            synchronized (retrofitMap) {
                obj = retrofitMap.get(key);
                if (obj == null) {
                    obj = createRetrofit(url, useCache, useRx);
                    retrofitMap.put(key, obj);
                }
            }
        }
        return obj;
    }

    private static Retrofit createRetrofit(String url, boolean useCache, boolean useRxJava) {
        OkHttpClient.Builder okHttpClientBuilder = createOkHttpClientBuilder();
        try {
            Interceptor interceptor = (Interceptor) Class.forName("com.yh.base.net.http.interceptor.CachedInterceptor").getConstructor(boolean.class).newInstance(useCache);
            okHttpClientBuilder.addInterceptor(interceptor);
        } catch (Exception e) {
        }
        if (Config.isIsDebug()) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(logInterceptor);
        }
        OkHttpClient httpClient = okHttpClientBuilder.build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()))
                .client(httpClient);
        if (useRxJava) {
            builder.addCallAdapterFactory(new MyRxJava2CallAdapterFactory());
        }
        return builder.build();
    }

    private static OkHttpClient.Builder createOkHttpClientBuilder() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        for (Interceptor interceptor : interceptors) {
            httpClientBuilder.addInterceptor(interceptor);
        }
//        httpClientBuilder.retryOnConnectionFailure(true);
        httpClientBuilder.writeTimeout(20, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(20, TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(20, TimeUnit.SECONDS);

        try {
            TrustAllCerts trustManager = new TrustAllCerts();
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            httpClientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
        } catch (Exception e) {
        }
        httpClientBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
//        httpClientBuilder.cache(new Cache(Util.getApplication().getCacheDir(), 10240 * 1024));

        return httpClientBuilder;
    }

    static OkHttpClient secondaryHttpClient;

//    public static void request(String url, String method, RequestBody body, Callback callback) {
//        Request request = new Request.Builder()
//                .url(url)
//                .method(method, body)
//                .build();
//        secondaryHttpClient.newCall(request).enqueue(callback);
//    }

    public static void request(Request request, Callback callback) {
        secondaryHttpClient.newCall(request).enqueue(callback);
    }


    public static RequestBody toPost(Object obj) {
        return toPost(new Gson().toJson(obj));
    }

    public static RequestBody toPost(String json) {
        return RequestBody.create(MediaTypes.Json, json);
    }

    public static MultipartBody.Part toMultipart(String path) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaTypes.FormData, file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return body;
    }

    public static <T> void foreground(Observable<T> o, CB<T> s) {
        o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(s);
    }

    public static <T> ObservableTransformer<T, T> background() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 信任所有的服务器,返回true
     */
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
