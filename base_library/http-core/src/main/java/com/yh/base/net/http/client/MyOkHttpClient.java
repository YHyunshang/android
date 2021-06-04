package com.yh.base.net.http.client;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MyOkHttpClient extends OkHttpClient {
    @Override
    public Call newCall(Request request) {
        request = request.newBuilder().addHeader("isCache", "true").build();
        return super.newCall(request);
    }

     MyOkHttpClient(OkHttpClient.Builder builder) {
//        super(builder);
    }
}
