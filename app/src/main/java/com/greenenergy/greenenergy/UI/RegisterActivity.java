package com.greenenergy.greenenergy.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greenenergy.greenenergy.Bean.UserInfo;
import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.MyData.FormData;
import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.Utils.GsonUtil;
import com.greenenergy.greenenergy.Utils.HttpUtil;

import java.io.IOException;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {
    private Button login;
    private Button register;
    private Button sendcode;
    private EditText phone;
    private EditText pswd;
    private EditText code;
    private int msid;
    private int  mycode;
    private String phoneNUmber;
    private String code_string;
    final String items[] = {"普通居民", "环卫工人"};
    private String pswd_String;
    private FormBody formBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusUI.StatusUISetting(this,"#50000000");
        //初始化短信服务
        BmobSMS.initialize(RegisterActivity.this, NetWorkData.APPID);
        //初始化view
        initView();
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        pswd = (EditText) findViewById(R.id.pswd);
        code = (EditText) findViewById(R.id.code);
       login = (Button) findViewById(R.id.button11);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
        register = (Button) findViewById(R.id.login);
        register.setClickable(false);
        sendcode = (Button) findViewById(R.id.button2);
        //发送验证码
        sendcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                phoneNUmber = phone.getText().toString();
                pswd_String = pswd.getText().toString();
                if (phoneNUmber.equals("")) {
                    Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                } else {
                    BmobSMS.requestSMSCode(RegisterActivity.this, phone.getText().toString(), NetWorkData.smsmodel, new RequestSMSCodeListener() {
                        @Override
                        public void done(Integer smsId, BmobException ex) {
                            if (ex == null) {//验证码发送成功
                                //启动计时器
                                new CountDownTimer(60000,1000){
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        sendcode.setClickable(false);
                                        sendcode.setText(millisUntilFinished / 1000 + "s");
                                    }
                                    @Override
                                    public void onFinish() {
                                        sendcode.setClickable(true);
                                        sendcode.setText("重新发送");
                                    }
                                }.start();

                                Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                                register.setClickable(true);
                            }else{
                                Toast.makeText(RegisterActivity.this, "验证码发送失败，请重试"+ex.getMessage()+ex.getErrorCode(), Toast.LENGTH_SHORT).show();
                                Log.d("EWE", "done: "+ex.getMessage());
                            }
                        }
                    });
                }
            }
        });

        //注册（包括验证部分）
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Register();
                //TODO 开发阶段 ，暂时不验证
                checkMsg(phoneNUmber);
            }
        });
    }

    //这里是验证的逻辑
    private void checkMsg(String phoneNUmber) {
        code_string = code.getText().toString();
        if (code_string.equals("")) {
            Toast.makeText(RegisterActivity.this, "验证码未填写", Toast.LENGTH_SHORT).show();
        } else {
            //检查验证码
            BmobSMS.verifySmsCode(RegisterActivity.this, phoneNUmber, code_string, new VerifySMSCodeListener() {

                @Override
                public void done(BmobException ex) {
                    // TODO Auto-generated method stub
                    if (ex == null) {//短信验证码已验证成功
                        Log.i("bmob", "验证通过");
                        Register();
                    } else {
                        Toast.makeText(RegisterActivity.this, "验证错误,请重试", Toast.LENGTH_SHORT).show();
                        Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                    }
                }
            });
        }
    }

    //注册逻辑
    private void Register() {
        phoneNUmber = phone.getText().toString();
        pswd_String = pswd.getText().toString();
       //注册
        if(pswd_String.equals("")||phoneNUmber.equals("")){
            Toast.makeText(RegisterActivity.this, "你怎么可以这么调皮,请填完整！", Toast.LENGTH_SHORT).show();
        }else{
            //取得category
            getCode();
        }
    }
    //这里可以获取身份（直接返回0，或1）
    private void getCode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        //设置标题
        builder.setTitle("请选择您的身份");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //这是服务器标准表单，具体的内容可以参照FormData这个类里的数据
                CreateForm(i);
            }
        });
        builder.create();
        builder.show();
    }

    private void CreateForm(int i) {
        formBody = new FormBody.Builder()
                .add(FormData.phone, phoneNUmber)
                .add(FormData.pswd, pswd.getText().toString())
                .add(FormData.name, phoneNUmber)
                .add(FormData.category, String.valueOf(i))
                .build();
        Log.d("SSSS", String.valueOf(i));
        startConn(formBody);
    }

    private void startConn(FormBody formBody) {
        HttpUtil.post(formBody,NetWorkData.register_api).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                final String e_str = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(e_str.contains("1000")){
                            Toast.makeText(RegisterActivity.this, "手机号已被注册！", Toast.LENGTH_SHORT).show();
                        }else if(e_str.contains("1001")){
                            Toast.makeText(RegisterActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                        }else if(e_str.contains("1002")){
                            Toast.makeText(RegisterActivity.this, "用户ID不存在", Toast.LENGTH_SHORT).show();
                        }else if(e_str.contains("1003")){
                            Toast.makeText(RegisterActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
                        }else if(e_str.contains("1004")){
                            Toast.makeText(RegisterActivity.this, "身份选择错误", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "服务器错误或者网络不通畅！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("MRSS",res);
                if(res!=null){
                    UserInfo userinfo = GsonUtil.parseJsonWithGson(res, UserInfo.class);

                    //是否成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            //注册成功之后就可以跳转到登录
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
