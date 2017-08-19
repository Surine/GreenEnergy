package com.greenenergy.greenenergy.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.R;

public class AddrSetActivity extends AppCompatActivity {
    private EditText home;
    private EditText comp;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addr_set);
        StatusUI.StatusUISetting(this,"#50000000");
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("设置常用地址");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        home = (EditText) findViewById(R.id.editText4);
        comp = (EditText) findViewById(R.id.editText5);
        text = (TextView) findViewById(R.id.textView8);

        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        home.setText(sharedPreferences.getString("HOME",""));
        comp.setText(sharedPreferences.getString("COMP",""));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void Finish() {
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("HOME",home.getText().toString());
        editor.putString("COMP",comp.getText().toString());
        editor.commit();
        finish();
    }
}
