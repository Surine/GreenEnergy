package com.greenenergy.greenenergy.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by surine on 2017/8/18.
 */

public class HttpUtil {
    static String postback;
    public static Call post(FormBody body, String url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
        final Request request = new Request.Builder().post(body).url(url).build();
        return okHttpClient.newCall(request);
    }

    public static Call get(String url){
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.cookieJar(new JavaNetCookieJar()).build();
        final Request request = new Request.Builder().url(url).build();
        return okHttpClient.newCall(request);
    }
}
