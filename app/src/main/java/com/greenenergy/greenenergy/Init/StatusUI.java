package com.greenenergy.greenenergy.Init;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

/**
 * Created by surine on 2017/8/3.
 */

public class StatusUI {
    public static void StatusUISetting(Activity context, String color){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = context.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            context.getWindow().setStatusBarColor(Color.parseColor(color));
        }
    }

}
