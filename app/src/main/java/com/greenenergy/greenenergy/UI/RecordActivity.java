package com.greenenergy.greenenergy.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.R;

public class RecordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        StatusUI.StatusUISetting(this,"#50000000");
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("投放记录");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
