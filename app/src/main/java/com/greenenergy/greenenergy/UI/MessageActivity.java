package com.greenenergy.greenenergy.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.greenenergy.greenenergy.Adapter.MessageAdapter;
import com.greenenergy.greenenergy.Bean.MessageBean;
import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.Utils.GsonUtil;
import com.greenenergy.greenenergy.Utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MessageActivity extends BaseActivity {
    private List<MessageBean> mMessageBeanList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        StatusUI.StatusUISetting(this,"#50000000");
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("消息");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTextView = (TextView) findViewById(R.id.textView);
        //开始请求网络数据
        StartGetMessage();
        mTextView.setText("正在请求数据……");
    }

    private void initRec() {
        if (mMessageBeanList.size() == 0) {
            mTextView.setVisibility(View.VISIBLE);
            Toast.makeText(MessageActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();
        } else {
            mTextView.setVisibility(View.GONE);
            mRecyclerView = (RecyclerView) findViewById(R.id.message_list);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
            mRecyclerView.setAdapter(new MessageAdapter(mMessageBeanList, MessageActivity.this));
        }
    }

    private void StartGetMessage() {
        HttpUtil.get(NetWorkData.getallnotice).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MessageActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        mTextView.setText("有网络错误呀！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message_json = response.body().string();
                Log.d("XEREX",message_json);
                mMessageBeanList = GsonUtil.parseJsonWithGsonToList(message_json,MessageBean.class);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       //配置Rec
                       initRec();
                   }
               });
            }
        });
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
