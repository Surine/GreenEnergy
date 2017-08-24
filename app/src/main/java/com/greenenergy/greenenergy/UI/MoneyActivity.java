package com.greenenergy.greenenergy.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.R;

public class MoneyActivity extends BaseActivity {
    private Button charge;
    private Button list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        StatusUI.StatusUISetting(this,"#50000000");
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("我的钱包");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        charge = (Button) findViewById(R.id.charge);
        list = (Button) findViewById(R.id.list);
        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MoneyActivity.this,"充值功能暂未开启",Toast.LENGTH_SHORT).show();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MoneyActivity.this,"账单功能暂未开启",Toast.LENGTH_SHORT).show();
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
