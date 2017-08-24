package com.greenenergy.greenenergy.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.greenenergy.greenenergy.Bean.SimpleBusBean;
import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.MyData.FormData;
import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.Utils.HttpUtil;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CheckActivity extends BaseActivity {

    private String user_id;
    private String pswd_str;
    private ProgressBar pg;
    private TextView check_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        StatusUI.StatusUISetting(this,"#50000000");
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("扫描结果"+);
        setTitle("扫描结果");

        check_info = (TextView) findViewById(R.id.score_Info);
        pg = (ProgressBar) findViewById(R.id.progressBar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        SharedPreferences share = getSharedPreferences("data",MODE_PRIVATE);
        user_id = share.getString("USERID","0");
        String base_str = share.getString("TOKEN_2","####");
        pswd_str =  new String
                (Base64.decode(base_str.getBytes(), Base64.DEFAULT));

        //TODO 上传服务器检查
        //检查通过后展示UI界面
        if(CheckAddScore(getIntent().getStringExtra("RESULT"),user_id,pswd_str)){
            //成功界面
        }else{
            //失败界面
        }
    }

    private boolean CheckAddScore(final String result, String user_id, String pswd_str) {
        FormBody formBody = new FormBody.Builder()
                .add(FormData.id, user_id)
                .add(FormData.pswd, pswd_str)
                .add(FormData.transhid, result)
                .build();
        HttpUtil.post(formBody, NetWorkData.add_score_api).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CheckActivity.this, "网络失败！", Toast.LENGTH_SHORT).show();
                        check_info.setVisibility(View.VISIBLE);
                        check_info.setText("网络请求失败，检查失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final int score = Integer.parseInt(response.body().string());
                runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         pg.setVisibility(View.GONE);
                         check_info.setVisibility(View.VISIBLE);
                         if(score>0)
                         check_info.setText("太棒了！投入可回收垃圾，获得"+score+"积分");
                         SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                         SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                         editor.putString("SCORE",(Integer.parseInt(sharedPreferences.getString("SCORE","0"))+score)+"");
                         editor.apply();
                         EventBus.getDefault().post(new SimpleBusBean(2,score+""));
                     }
                 });
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}

