package com.greenenergy.greenenergy.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.greenenergy.greenenergy.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    The_Intent();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void The_Intent() {
        SharedPreferences pre = getSharedPreferences("data",MODE_PRIVATE);
        if(pre.getBoolean("isfirstlogin",true)){
            //TODO 选择跳转（目前是跳转到Register页面，当注册成功，登录成功后下次就会跳转到主页面）
            startActivity(new Intent(SplashActivity.this,RegisterActivity.class));
          //  startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }
        finish();
    }
}
