package com.greenenergy.greenenergy.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greenenergy.greenenergy.Bean.UserInfo;
import com.greenenergy.greenenergy.MyData.FormData;
import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.Utils.GsonUtil;
import com.greenenergy.greenenergy.Utils.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText phone;
    private EditText pswd;
    private Button login;
    String phone_str;
    String pswd_str;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        phone = (EditText) findViewById(R.id.phone);
        pswd = (EditText) findViewById(R.id.pswd);
        login = (Button) findViewById(R.id.login);
        mTextView = (TextView) findViewById(R.id.textView4);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_str = phone.getText().toString();
                pswd_str = pswd.getText().toString();
                if(phone_str.equals("")||pswd_str.equals("")){
                    Toast.makeText(LoginActivity.this,"请填写完整信息",Toast.LENGTH_SHORT).show();
                }else{
                    mTextView.setText("正在登录……");
                    FormBody formBody = new FormBody.Builder()
                            .add(FormData.phone, phone_str)
                            .add(FormData.pswd, pswd_str)
                            .build();

                    //登陆的网络请求
                   startHttpConn(formBody);
                }
            }
        });

    }

    private void startHttpConn(FormBody formBody) {
        HttpUtil.post(formBody, NetWorkData.login_api).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                        Log.d("SSSS",e.getMessage());
                        mTextView.setText("登录");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String en_str = response.body().string();
                Log.d("SSSS",en_str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!en_str.equals("")) {
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    UserInfo userinfo = GsonUtil.parseJsonWithGson(en_str, UserInfo.class);
                    saveInfo(userinfo);
                    startIntent();
                    }else{
                        mTextView.setText("登录");
                        Toast.makeText(LoginActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
        });
    }

    private void startIntent() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }

    private void saveInfo(UserInfo userinfo) {
        // encodeToString will return string value
        String enToStr = Base64.encodeToString(pswd_str.getBytes(), Base64.DEFAULT);
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean("isfirstlogin",false);
        editor.putString("TOKEN_1",phone_str);
        editor.putString("TOKEN_2",enToStr);   //储存账号密码
        editor.putString("PHONE",userinfo.getPhoneNum());
        editor.putString("CATEGORY",userinfo.getCategory());
        editor.putString("SCORE",userinfo.getScore().getScore());
        editor.putString("ENERGY",userinfo.getScore().getEnergy());
        editor.putString("OTHER",userinfo.getScore().getOther());
        editor.apply();
    }
}
