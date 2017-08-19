package com.greenenergy.greenenergy.Utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by surine on 2017/3/28.
 * Cooies管理类
 */

public class JavaNetCookieJar implements CookieJar {
    private final List<Cookie> allCookies=new ArrayList<Cookie>();

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        allCookies.addAll(cookies);
        Log.d("cookies", "saveFromResponse: "+allCookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> result = new ArrayList<Cookie>();
        for (Cookie cookie : allCookies) {
            if (cookie.matches(url)) {
                result.add(cookie);
            }
        }
        Log.d("cookies", "loadForRequest: "+result);
        return result;
    }
}