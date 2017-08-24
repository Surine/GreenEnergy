package com.greenenergy.greenenergy.Init;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by surine on 2017/8/22.
 */

public class BaseActivity extends AppCompatActivity {
    /**
     * android6.0权限处理
     * @param permissionName    权限名称
     */
    public void permissionDispose(String[] permissionName){

        if(ContextCompat.checkSelfPermission(this, permissionName[0])!= PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, permissionName[1])!= PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, permissionName[2])!= PackageManager.PERMISSION_GRANTED
        ){
            //没有权限,开始申请
            ActivityCompat.requestPermissions(this,permissionName,0);
        }else{
            //有权限
            onAccreditSucceed();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&
                grantResults[1]==PackageManager.PERMISSION_GRANTED&&
                grantResults[2]==PackageManager.PERMISSION_GRANTED
                ){
            //授权成功
            onAccreditSucceed();
        }else if(grantResults[0]==PackageManager.PERMISSION_DENIED||
                grantResults[1]==PackageManager.PERMISSION_DENIED||
                grantResults[2]==PackageManager.PERMISSION_DENIED
                ){
            //授权失败
            onAccreditFailure ();
        }
    }

    /**
     * 有授权执行的方法(子类重写)
     */
    public void onAccreditSucceed() {
    }

    /**
     * 没有授权执行的方法(子类重写)
     */
    public void onAccreditFailure() {
    }
}
