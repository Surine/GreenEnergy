package com.greenenergy.greenenergy.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.UI.AddrSetActivity;
import com.greenenergy.greenenergy.UI.RegisterActivity;
import com.greenenergy.greenenergy.UI.WebViewActivity;
import com.greenenergy.greenenergy.Utils.AppUtil;

import java.io.File;

/**
 * Created by surine on 2017/8/17.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference mPreference_share;
    private Preference mPreference_addr;
    private Preference mPreference_lis;
    private Preference mPreference_money;
    private Preference mPreference_charge;
    private Preference mPreference_exit;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        findview();   //initview
        setLinsener();   //setlinstener
    }

    private void setLinsener() {
         mPreference_share.setOnPreferenceClickListener(this);
         mPreference_addr.setOnPreferenceClickListener(this);
         mPreference_lis.setOnPreferenceClickListener(this);
         mPreference_money.setOnPreferenceClickListener(this);
         mPreference_charge.setOnPreferenceClickListener(this);
         mPreference_exit.setOnPreferenceClickListener(this);
    }

    private void findview() {
        mPreference_share = findPreference("share");
        mPreference_addr = findPreference("addr");
        mPreference_lis = findPreference("lis");
        mPreference_money = findPreference("money");
        mPreference_charge = findPreference("charge");
        mPreference_exit = findPreference("exit");
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference == mPreference_share){
            //分享
            AppUtil.ShareAPP(getActivity());
        }else if (preference == mPreference_addr){
            startActivity(new Intent(getActivity(), AddrSetActivity.class));
        }else if(preference == mPreference_lis){
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("URL", NetWorkData.lis).putExtra("TITLE","用户协议"));
        }else if(preference == mPreference_money){
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("URL", NetWorkData.money).putExtra("TITLE","押金说明"));
        }else if(preference == mPreference_charge){
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("URL", NetWorkData.charge).putExtra("TITLE","充值说明"));
        }else if(preference == mPreference_exit){
            exit();
        }
        return true;
    }

    //退出登录
    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("确定退出？");
        builder.setMessage("退出后将清空用户本地信息");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               //删除数据文件，启动注册页面
                File file = new File("/data/data/com.greenenergy.greenenergy/shared_prefs/data.xml");
                deletefile(file);
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                getActivity().finish();
            }
        });
        builder.create().show();
    }

    private void deletefile(File file) {
        if(file.exists()){
            if(file.isFile()){
                file.delete();   //delete the  SharedPreferences
            }
        }
    }
}
