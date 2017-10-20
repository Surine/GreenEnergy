package com.greenenergy.greenenergy.UI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.greenenergy.greenenergy.Bean.UserInfo;
import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.MyData.FormData;
import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.Utils.GsonUtil;
import com.greenenergy.greenenergy.Utils.HttpUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class SplashActivity extends BaseActivity {

    private String phone_str;
    private String pswd_str;

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
                //直接调用父类的权限处理方法，不用判读是否有该权限，因为父类已经判断了
                permissionDispose(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CAMERA
                });
            }
        }).start();
    }


    @Override
    public void onAccreditSucceed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    SharedPreferences pre = getSharedPreferences("data", MODE_PRIVATE);
                    if (pre.getBoolean("isfirstlogin", true)) {
                        startActivity(new Intent(SplashActivity.this,RegisterActivity.class));
                        finish();
                    } else {
                        //检查登陆
                       CheckLogin();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onAccreditFailure() {
            Toast.makeText(this, "如果不授予全部权限，APP将无法正常运行", Toast.LENGTH_SHORT).show();
            finish();
    }

    private void CheckLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences share = getSharedPreferences("data",MODE_PRIVATE);
                phone_str = share.getString("TOKEN_1","#####");
                String base_str = share.getString("TOKEN_2","####");
                pswd_str =  new String
                        (Base64.decode(base_str.getBytes(), Base64.DEFAULT));
                FormBody formBody = new FormBody.Builder()
                        .add(FormData.phone, phone_str)
                        .add(FormData.pswd, pswd_str)
                        .build();
                //登陆的网络请求
                startHttpConn(formBody);
            }
        }).start();
    }

    private void startHttpConn(FormBody formBody) {
        HttpUtil.post(formBody, NetWorkData.login_api).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 Login_Error();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String en_str = response.body().string();
                Log.d("TES",en_str);
                UserInfo userinfo = null;
                try {
                    userinfo = GsonUtil.parseJsonWithGson(en_str, UserInfo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SplashActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                }
                if(!userinfo.getPhoneNum().equals("")) {
                    saveInfo(userinfo);
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }else{
                    Login_Error();
                }
            }
        });
    }

    private void saveInfo(UserInfo userinfo) {
        // encodeToString will return string value
        String enToStr = Base64.encodeToString(pswd_str.getBytes(), Base64.DEFAULT);
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean("isfirstlogin",false);
        editor.putString("TOKEN_1",phone_str);
        editor.putString("TOKEN_2",enToStr);   //储存账号密码
        editor.putString("PHONE",userinfo.getPhoneNum());
        editor.putString("USERID",userinfo.getID());
        editor.putString("CATEGORY",userinfo.getCategory());
        editor.putString("SCORE",userinfo.getScore().getScore());
        editor.putString("ENERGY",userinfo.getScore().getEnergy());
        editor.putString("OTHER",userinfo.getScore().getOther());
        editor.apply();
    }


    private void Login_Error() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("登录信息失效，请重新登录");
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            File file = new File("/data/data/com.greenenergy.greenenergy/shared_prefs/data.xml");
                            deletefile(file);
                            startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                            finish();
                        }
                    });
                    builder.show();
                }
            });
        }


    private void deletefile(File file) {
        if(file.exists()){
            if(file.isFile()){
                file.delete();   //delete the  SharedPreferences
            }
        }
    }

}
