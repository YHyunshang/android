package com.yh.base.net.http.interceptor;

import android.annotation.SuppressLint;

import com.yh.base.utils.JsonUtils;
import com.yh.base.utils.LogUtils;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {

    //    private StringBuilder mMessage = new StringBuilder();
    ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<>();

    @SuppressLint("CheckResult")
    @Override
    public void log(String message) {
        // 请求或者响应开始
//        if (message.startsWith("-->")&&message.length() ) {
//            mMessage.setLength(0);
//        }
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化

        StringBuilder mMessage = threadLocal.get();
        if (mMessage == null) {
            mMessage = new StringBuilder();
            threadLocal.set(mMessage);
        }
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            message = JsonUtils.formatJson(JsonUtils.decodeUnicode(message));
        }
        mMessage.append(message.concat("\n"));
        // 响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
//            LogUtils.e("==时间开始="+Thread.currentThread().getName()+"====="+System.currentTimeMillis()+"==len="+mMessage.toString().length());
            int maxLength = 50 * 1024;
            if (mMessage.toString().length() > maxLength) {
                LogUtils.d(mMessage.toString().substring(0, maxLength));
            } else {
                LogUtils.d(mMessage.toString());
            }
            mMessage.setLength(0);
//            LogUtils.e("==时间结束="+Thread.currentThread().getName()+"====="+System.currentTimeMillis());
        }
    }
}
