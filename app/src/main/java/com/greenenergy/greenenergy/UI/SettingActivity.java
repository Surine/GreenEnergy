package com.greenenergy.greenenergy.UI;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.greenenergy.greenenergy.Fragment.SettingFragment;
import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.R;

public class SettingActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("设置");
        replaceFragment(new SettingFragment());
    }

    private void replaceFragment(SettingFragment prefFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.share_setting_fragment, prefFragment);
        transaction.commit();
    }



    //set the back button listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

