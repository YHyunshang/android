package com.yh.base.net.http.cache;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class HttpHelper {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static String getBodyString(RequestBody requestBody) throws IOException {
        if (requestBody == null) {
            return "";
        }
        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
            Charset tmpCharset = contentType.charset(UTF8);
            if (tmpCharset != null) {
                charset = tmpCharset;
            }
        }
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return buffer.readString(charset);
    }

    public static String getBodyString(ResponseBody responseBody) throws IOException {
        if (responseBody == null) {
            return "";
        }
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            Charset tmpCharset = contentType.charset(UTF8);
            if (tmpCharset != null) {
                charset = tmpCharset;
            }
        }
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        return source.getBuffer().clone().readString(charset);
    }
}
