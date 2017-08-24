package com.greenenergy.greenenergy.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.R;

public class DevelopActivity extends BaseActivity {
     private EditText ip;
    private Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop);
        ip = (EditText) findViewById(R.id.editText3);
        ok = (Button) findViewById(R.id.button4);
        final SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ip.getText().toString().equals("")){
                    Toast.makeText(DevelopActivity.this,"请填写服务器信息",Toast.LENGTH_SHORT).show();
                }else{
                    editor.putString("IP",ip.getText().toString());
                    editor.commit();
                }
            }
        });
    }
}
