package com.greenenergy.greenenergy.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.greenenergy.greenenergy.Bean.SimpleBusBean;
import com.greenenergy.greenenergy.Bean.VersionInfo;
import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.UI.AddrSetActivity;
import com.greenenergy.greenenergy.UI.RegisterActivity;
import com.greenenergy.greenenergy.UI.WebViewActivity;
import com.greenenergy.greenenergy.Utils.AppUtil;
import com.greenenergy.greenenergy.Utils.GsonUtil;
import com.greenenergy.greenenergy.Utils.HttpUtil;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    private Preference mPreference_check;
    private Preference mPreference_about;
    private Preference mPreference_osl;
    private ProgressDialog pg;
    private String url = null;
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
         mPreference_check.setOnPreferenceClickListener(this);
         mPreference_about.setOnPreferenceClickListener(this);
         mPreference_osl.setOnPreferenceClickListener(this);
    }

    private void findview() {
        mPreference_share = findPreference("share");
        mPreference_addr = findPreference("addr");
        mPreference_lis = findPreference("lis");
        mPreference_money = findPreference("money");
        mPreference_charge = findPreference("charge");
        mPreference_exit = findPreference("exit");
        mPreference_check = findPreference("check");
        mPreference_about = findPreference("about");
        mPreference_osl = findPreference("osl");
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
        }else if(preference == mPreference_check){
            checkUpdate();
        }else if(preference == mPreference_about){
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("URL", NetWorkData.about).putExtra("TITLE","关于我们"));
        }else if(preference == mPreference_osl){
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("URL", NetWorkData.osl).putExtra("TITLE","开源协议"));
        }
        return true;
    }

    private void checkUpdate() {
        setDialog();
        StartCheck();
    }

    private void StartCheck() {
        HttpUtil.get(NetWorkData.update_api).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                        pg.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.d("SSSx",res);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(res!=null||!res.equals("")){
                            VersionInfo versioninfo = GsonUtil.parseJsonWithGson(res,VersionInfo.class);
                            int code = Integer.parseInt(versioninfo.getVersion_code());
                            if(code > getAppCode()){
                                //新版本
                                pg.dismiss();
                                url = versioninfo.getVersion_url();
                                showDialog("发现新版本",versioninfo.getVersion_log(),1);
                            }else{
                                pg.dismiss();
                                showDialog("版本","已经是最新版本!",0);
                            }
                        }else{
                            Toast.makeText(getActivity(),"服务器错误",Toast.LENGTH_SHORT).show();
                            pg.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void showDialog(String title, String message, int i) {
        AlertDialog.Builder bu = new AlertDialog.Builder(getActivity());
        bu.setTitle(title);
        bu.setMessage(message);
        if(i == 1){
            bu.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                     startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                }
            });
        }else{
            bu.setPositiveButton("确定",null);
        }
        bu.show();
    }

    private int getAppCode() {
        try {
            String pkName = getActivity().getPackageName();
            String versionName = getActivity().getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = getActivity().getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return versionCode;
        } catch (Exception e) {
        }
        return 1;
    }

    //set dialog
    private void setDialog() {
        //create the dialog
        pg = new ProgressDialog(getActivity());
        pg.setTitle("检查更新");
        pg.setMessage("正在检查服务器新版本……");
        pg.setCancelable(false);
        pg.show();
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
                EventBus.getDefault().post(new SimpleBusBean(3,"MESSAGE"));
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
