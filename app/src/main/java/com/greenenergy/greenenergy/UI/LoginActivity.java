package com.greenenergy.greenenergy.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greenenergy.greenenergy.R;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = (EditText) findViewById(R.id.phone);
        pswd = (EditText) findViewById(R.id.pswd);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_str = phone.getText().toString();
                pswd_str = pswd.getText().toString();
                if(phone_str.equals("")||pswd_str.equals("")){
                    Toast.makeText(LoginActivity.this,"请填写完整信息",Toast.LENGTH_SHORT).show();
                }else{
                    FormBody formBody = new FormBody.Builder()
                            .add("user", phone_str)
                            .add("pswd", pswd_str)
                            .build();

                    //登陆的网络请求
                   startHttpConn(formBody);
                }
            }
        });

    }

    private void startHttpConn(FormBody formBody) {
        HttpUtil.post(formBody,"http://surine.cn/php/androidlogin/Login.php").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("SSSS",response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                    }
                });
                //记录已经登陆的逻辑
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putBoolean("isfirstlogin",false).commit();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
    }
}
