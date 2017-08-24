package com.greenenergy.greenenergy.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;

public class RecycActivity extends BaseActivity {
    private Button com;
    private Button home;
    private EditText addr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyc);
        StatusUI.StatusUISetting(this,"#50000000");
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("一键回收");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        com = (Button) findViewById(R.id.button8);
        home = (Button) findViewById(R.id.button9);
        addr = (EditText) findViewById(R.id.editText6);

        final SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);

        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addr.setText(sharedPreferences.getString("COMP",""));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addr.setText(sharedPreferences.getString("HOME",""));
            }
        });
    }
    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_note:
                startActivity(new Intent(RecycActivity.this, WebViewActivity.class).putExtra("URL", NetWorkData.recyc_note).putExtra("TITLE","一键回收说明"));

                break;
        }
        return true;
    }
}
