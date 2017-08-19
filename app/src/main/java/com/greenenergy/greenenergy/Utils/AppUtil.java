package com.greenenergy.greenenergy.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.greenenergy.greenenergy.MyData.NetWorkData;

/**
 * Created by surine on 2017/8/17.
 */

public class AppUtil {
    public static void ShareAPP(Context mc){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "欢迎使用GreenEnergy");
        intent.setType("text/plain");
        mc.startActivity(Intent.createChooser(intent, "你想把我分享到哪里呢？"));
    }

    public static String getIP(Context mc){
        SharedPreferences share = mc.getSharedPreferences("data",Context.MODE_PRIVATE);
        return share.getString("IP", NetWorkData.IP_port);
    }
}
