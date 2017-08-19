package com.greenenergy.greenenergy.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.Utils.AppUtil;
import com.greenenergy.greenenergy.Utils.NetUtil;

import google.google.zxing.activity.CaptureActivity;

public class MainActivity extends AppCompatActivity {
     private DrawerLayout mDrawerLayout;
    private MapView mmap;
    private Button zoomInBtn;
    private Button zoomOutBtn;
    private BaiduMap baidumap;
    private LocationClient mlc;
    private double lati_;
    private double long_;
    private boolean isFirstLocate = true;
    private MapStatusUpdate update;
    private ImageView location_icon;
    private MyLocationData locData;
    private ImageView error;
    final String items[] = {"垃圾桶损坏", "区域不整洁", "举报违规", "其他问题"};
    private Button scan;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusUI.StatusUISetting(this,"#50000000");

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                mDrawerLayout.closeDrawers();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            switch (item.getItemId()){
                                case R.id.nav_setting:
                                    startActivity(new Intent(MainActivity.this,SettingActivity.class));
                                    break;
                                case R.id.nav_share:
                                    //分享
                                    AppUtil.ShareAPP(MainActivity.this);
                                    break;
                                case R.id.nav_develop:
                                    startActivity(new Intent(MainActivity.this,DevelopActivity.class));
                                    break;
                                case R.id.nav_money:
                                    startActivity(new Intent(MainActivity.this,MoneyActivity.class));
                                    break;
                                case R.id.nav_record:
                                    startActivity(new Intent(MainActivity.this,RecordActivity.class));
                                    break;
                                case R.id.nav_market:
                                    startActivity(new Intent(MainActivity.this,MarketActivity.class));
                                    break;
                                case R.id.nav_recycle:
                                    startActivity(new Intent(MainActivity.this,RecycActivity.class));
                                    break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return true;
            }
        });



        mmap = (MapView) findViewById(R.id.map);

        if(NetUtil.checkNet()){
            //初始化定位
            initLocation();
        }else{
            Toast.makeText(MainActivity.this, R.string.error,Toast.LENGTH_SHORT).show();
        }
        //自定义定位方式调整按钮
        MyLocationButton();

        //初始化地图
        initMap();

        scan();

        //缩放view
        MyZoomControl();

        error = (ImageView)findViewById(R.id.imageView2);
        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //设置标题
                builder.setTitle("客户服务");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this,"你点击的是条目"+i,Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    private void scan() {
      scan = (Button)findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(intent);
            }
        });
    }

    private void MyLocationButton() {
        location_icon = (ImageView) findViewById(R.id.my_location);
        location_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location(locData);
            }
        });
    }


    //初始化地图
    private void initMap() {
        mmap = (MapView) findViewById(R.id.map);
        mmap.setVisibility(View.VISIBLE);
        baidumap = mmap.getMap();

        //修改默认view
        changeDefaultBaiduMapView(mmap);

        // 开启定位图层
        baidumap.setMyLocationEnabled(true);
    }

    private void changeDefaultBaiduMapView(MapView mmap) {
        // 隐藏地图上比例尺
        mmap.showScaleControl(false);

        // 隐藏地图缩放控件
        mmap.showZoomControls(false);

//        // 隐藏百度的LOGO
//        View child = this.mmap.getChildAt(1);
//        //原理也就是在view里面找到一个iamgeview（也就是logo），然后隐藏
//        if (child != null && (child instanceof ImageView)) {
//            child.setVisibility(View.INVISIBLE);
//        }
    }


    private void MyZoomControl() {
        zoomInBtn = (Button) findViewById(R.id.zoomin);
        zoomOutBtn = (Button) findViewById(R.id.zoomout);
        zoomInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float zoomLevel = baidumap.getMapStatus().zoom;

                if(zoomLevel<=18){
                    baidumap.animateMapStatus(MapStatusUpdateFactory.zoomIn());
                    zoomOutBtn.setEnabled(true);
                }else{
                    Toast.makeText(MainActivity.this, "已经放至最大！", Toast.LENGTH_SHORT).show();
                    zoomInBtn.setEnabled(false);
                }
            }
        });
        zoomOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float zoomLevel = baidumap.getMapStatus().zoom;
                if(zoomLevel>4){
                    baidumap.animateMapStatus(MapStatusUpdateFactory.zoomOut());
                    zoomInBtn.setEnabled(true);
                }else{
                    zoomOutBtn.setEnabled(false);
                    Toast.makeText(MainActivity.this, "已经缩至最小！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initLocation() {

        //客户端配置对象
        LocationClientOption lco = new LocationClientOption();
        //定位时间间隔，就是我们上面说的f服务要实现的功能
        //f服务的运行才能保证一直定位
        // span参数不能小于1000ms
        lco.setScanSpan(1000);
        //初始化定位客户端
        mlc = new LocationClient(getApplicationContext());
        //给定位客户端注册自定义监听器
        mlc.registerLocationListener(new LocationListener());
        //设置一些配置选项
        //当然这个lco配置还有很多，具体在下面我会列举，
        mlc.setLocOption(lco);
        //启动定位客户端
        mlc.start();
    }

    public class LocationListener implements BDLocationListener {

        //接受位置信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mmap == null) {
                return;
            }
            baidumap.clear();
            //调用设置方法
            moveTo(bdLocation);
        }

    }

    //地图控制方法
    private void moveTo(final BDLocation bdLocation) {
        lati_ = bdLocation.getLatitude();
        long_ = bdLocation.getLongitude();

        //显示定位图标
         locData = new MyLocationData.Builder()
                //  不显示半径圆圈
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(bdLocation.getDirection()).latitude(lati_)
                .longitude(long_).build();
        //配置（定位模式，允许显示方向，图标）
        MyLocationConfiguration configuration
                =new MyLocationConfiguration(LocationMode.NORMAL,true,null);
        //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
        baidumap.setMyLocationConfiguration(configuration);



        if(isFirstLocate){
            Location(locData);
        }

    }

    private void Location(MyLocationData locData) {
        baidumap.setMyLocationData(locData);
        isFirstLocate = false;
        baidumap.setMyLocationConfiguration(new MyLocationConfiguration(
                LocationMode.NORMAL, true, null));
        LatLng ll = new LatLng(lati_,long_);
        update = MapStatusUpdateFactory.newLatLng(ll);
        baidumap.animateMapStatus(update);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //延时动画缩放
                    Thread.sleep(300);
                    update = MapStatusUpdateFactory.zoomTo(18f);
                    baidumap.animateMapStatus(update);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //停止运行，防止后台耗电
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlc.stop();
        // 开启定位图层
        baidumap.setMyLocationEnabled(false);
    }


    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
    //onResume状态
    @Override
    protected void onResume() {
        super.onResume();
        mmap.onResume();
    }

    //pause状态
    @Override
    protected void onPause() {
        super.onPause();
        mmap.onPause();
    }


}