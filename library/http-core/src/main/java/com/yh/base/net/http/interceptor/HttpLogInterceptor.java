package com.yh.base.net.http.interceptor;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yh.base.utils.LogUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Http管理拦截器
 */
public class HttpLogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private Gson gson = new Gson();

    private boolean isOpenHeadersMode = true;

    @Override
    public Response intercept(Chain chain) throws IOException {
        //------> Request start
        Request request = chain.request();

        RequestBody requestBody    = request.body();
        boolean     hasRequestBody = requestBody != null;
        String      requestMethod  = request.method();

        Connection connection = chain.connection();

        LogUtils.e("--> 请求Start：");

        String requestStartMessage = "--> "
                + requestMethod
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (!isOpenHeadersMode && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }

        LogUtils.d(requestStartMessage);
        LogUtils.e("---------request------------>");

        if (isOpenHeadersMode) {

            //--> Headers
            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    if ("tokenString".equalsIgnoreCase(name)) {
                        onLogParameterData(headers.value(i));
                    }
                }
            }
            //--> RequestBody
            if (requestMethod.equalsIgnoreCase("POST")){
                HashMap<String, String> map = new HashMap<>();
                if (requestBody instanceof FormBody) {
                    FormBody body = (FormBody) requestBody;
                    for (int i = 0; i < body.size(); i++) {
                        map.put(body.encodedName(i), body.encodedValue(i));
                    }
                    LogUtils.obj("--->>> 接口参数",map);
                }
            }

//            Buffer buffer = new Buffer();
//            if (requestBody != null) {
//                requestBody.writeTo(buffer);
//            }
//
//            Charset   charset     = UTF8;
//            MediaType contentType = requestBody.contentType();
//            if (contentType != null) {
//                charset = contentType.charset(UTF8);
//            }
//
//            if (isPlaintext(buffer)) {
//                String requestStr = buffer.readString(charset);
//            }
        }
        //------> Request end

        //------> Response start
        long     startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);


        } catch (Exception e) {
            LogUtils.e("<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody  = response.body();
        long         contentLength = responseBody.contentLength();
        String       bodySize      = contentLength != -1 ? contentLength + "-byte" : "unknown-length";

        int    httpStatusCode = response.code();
        String httpUrl        = response.request().url().url().toString();
        //todo
        LogUtils.e("---------response------------>");
        //        LogUtils.obj("response = ", gson.toJson(response));
        String responseMessage = "<-- "
                + httpStatusCode
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + (!isOpenHeadersMode ? ", " + bodySize + " body" : "") + ')';
        LogUtils.e(responseMessage);

        if (isOpenHeadersMode) {

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset   charset     = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (!isPlaintext(buffer)) {
                //               LogUtils.e("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                return response;
            }

            String responseBodyJson = buffer.clone().readString(charset);

            if (contentLength != 0) {
                //todo
                try {
                    LogUtils.json("--->>> 接口返回:", responseBodyJson);

                } catch (Throwable e) {
                }
            }
        }
        //------> Response end

        return response;
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix    = new Buffer();
            long   byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private void onLogParameterData(String tokenString) {
        HashMap<String, String> map = new HashMap<>();
        if (TextUtils.isEmpty(tokenString)) return;
        try {
            String[] tokenStrings = tokenString.split("&");
            for (int i = 0; i < tokenStrings.length; i++) {
                String tokenStr = tokenStrings[i];
                if (tokenStr.contains("=")) {
                    String[] itemData = tokenStrings[i].split("=");
                    map.put(itemData[0], itemData.length == 2 ? itemData[1] : "");
                }
            }
        } catch (Exception e) {

        }
        LogUtils.obj("--->>>  Header请求参数：", map);
    }

}
