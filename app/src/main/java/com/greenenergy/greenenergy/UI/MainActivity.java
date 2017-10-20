package com.greenenergy.greenenergy.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.greenenergy.greenenergy.Bean.BaiduPoiInfo;
import com.greenenergy.greenenergy.Bean.Can_Poi;
import com.greenenergy.greenenergy.Bean.SimpleBusBean;
import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.Utils.AppUtil;
import com.greenenergy.greenenergy.Utils.GsonUtil;
import com.greenenergy.greenenergy.Utils.HttpUtil;
import com.greenenergy.greenenergy.Utils.NetUtil;
import com.greenenergy.greenenergy.Utils.WalkingRouteOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import google.google.zxing.activity.CaptureActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity{
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
    private String pswd_str;
    private String phone_str;
    private TextView phonenum;
    private TextView category;
    private TextView score;
    private TextView energy;
    private TextView other;
    private MarkerOptions markerOptions;
    private Marker marker;
    private RoutePlanSearch mSearch;
    private CardView mCardView;
    private Button exit_info;
    private TextView uid_can;
    private TextView time;
    private String marker_id;
    private TranslateAnimation mShowAction;
    private TranslateAnimation mHiddenAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusUI.StatusUISetting(this,"#50000000");

        EventBus.getDefault().register(this);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(this,R.style.Toolbar_TitleText);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw);
        mCardView = (CardView)findViewById(R.id.marker_info);
        exit_info = (Button)findViewById(R.id.exit_info);
        uid_can = (TextView) findViewById(R.id.uid_can);
        time = (TextView) findViewById(R.id.time);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        View headerView = navigationView.getHeaderView(0);
        phonenum = (TextView) headerView.findViewById(R.id.PhoneNum);
        category = (TextView) headerView.findViewById(R.id.category);
        score = (TextView) headerView.findViewById(R.id.score);
        energy = (TextView) headerView.findViewById(R.id.energy);
        other = (TextView) headerView.findViewById(R.id.other);

        exit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCardView.startAnimation(mHiddenAction);
                mCardView.setVisibility(View.GONE);
                //重新定位
                baidumap.clear();
                Location(locData);
            }
        });

        //info展示动画
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -2.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -2.0f);
        mHiddenAction.setDuration(500);

        //本地更新UI
        updateUI();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        //创建路线规划实例
        mSearch = RoutePlanSearch.newInstance();
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

            if (NetUtil.checkNet()) {
                //初始化定位
                initLocation();
            } else {
                Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this,"反馈成功",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    private void GetMarkerLocation(final double lati_, final double long_) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                 GetPoint(lati_,long_);
            }
        }).start();
    }

    private void GetPoint(double lati_, double long_) {
        String can_url = NetWorkData.get_can_api+"location="+long_+","+lati_+"&radius=50000";
        HttpUtil.get(can_url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                BaiduPoiInfo poi_info = GsonUtil.parseJsonWithGson(res, BaiduPoiInfo.class);
                if (poi_info.getSize() != null) {
                    if (poi_info.getSize().equals("0")) {
                        Toast.makeText(MainActivity.this, "本地区未开通服务，敬请期待", Toast.LENGTH_SHORT).show();
                    } else {
                        AddMakers(poi_info.getSize(),poi_info.getContents());
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          //  Toast.makeText(MainActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void AddMakers(String size, List<Can_Poi> contents) {
        List<OverlayOptions> moptions = new ArrayList<>();

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.marker);
        BitmapDescriptor bitmap_full = BitmapDescriptorFactory
                .fromResource(R.drawable.marker_full);
        for(int i = 0;i<Integer.parseInt(size);i++){
            OverlayOptions oo;
            LatLng point = new LatLng(Double.parseDouble(contents.get(i).getLocation()[1]),Double.parseDouble(contents.get(i).getLocation()[0]));
            if(contents.get(i).getIs_full() == 0) {
               oo = new MarkerOptions().position(point).icon(bitmap).title(contents.get(i).getUid());
            }else{
                oo = new MarkerOptions().position(point).icon(bitmap_full).title(contents.get(i).getUid());
            }
            moptions.add(oo);
       }
        baidumap.addOverlays(moptions);
        baidumap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getIcon().getBitmap().sameAs(BitmapFactory.decodeResource(getResources(),R.drawable.marker_full))){
                    SharedPreferences share = getSharedPreferences("data",MODE_PRIVATE);
                    if(share.getString("CATEGORY","0").equals("0")){
                        Toast.makeText(MainActivity.this, "宝宝已经满啦，请选择其他的垃圾桶！", Toast.LENGTH_SHORT).show();
                    }else{
                        StartWalkProcess(marker.getPosition());
                    }
                }else{
                    marker_id = marker.getTitle();
                    //Toast.makeText(MainActivity.this, "路线规划！", Toast.LENGTH_SHORT).show();
                    //开始路径规划
                    StartWalkProcess(marker.getPosition());
                }
                return false;
            }
        });
    }

    //步行路径
    private void StartWalkProcess(LatLng position) {
        Toast.makeText(MainActivity.this,"正在规划路线，请稍后",Toast.LENGTH_SHORT).show();
        PlanNode stNode = PlanNode.withLocation(new LatLng(lati_, long_));
        PlanNode enNode = PlanNode.withLocation(position);

        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult result) {
                //路径规划步行结果
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MainActivity.this, "抱歉，路径规划失败", Toast.LENGTH_SHORT).show();
                }
                if (result.getRouteLines().size() == 1) {
                    baidumap.clear();
                    // 直接显示
                    WalkingRouteOverlay overlay = new WalkingRouteOverlay(baidumap);
                    baidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    mCardView.startAnimation(mShowAction);
                    mCardView.setVisibility(View.VISIBLE);
                    uid_can.setText(marker_id);
                    time.setText((result.getRouteLines().get(0).getDuration()/60)+"分钟");
                } else {
                    Toast.makeText(MainActivity.this, "抱歉，路径规划失败", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult result) {
                Log.d("SWS",result+"结果");
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
                Log.d("SWS",result+"结果");
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                Log.d("SWS",result+"结果");
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult result) {
                Log.d("SWS",result+"结果");
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult result) {
                Log.d("SWS",result+"结果");
            }
        });

        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode).to(enNode));
    }

    private void updateUI() {
        SharedPreferences share = getSharedPreferences("data",MODE_PRIVATE);
        phonenum.setText(share.getString("PHONE","ERROR"));
        if(share.getString("CATEGORY","0").equals("0")){
            category.setText("普通VIP");
        }else{
            category.setText("社区管理");
        }
        score.setText(share.getString("SCORE","0"));
        energy.setText(share.getString("ENERGY","0"));
        other.setText(share.getString("OTHER","0"));
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
               if(mCardView.getVisibility() == View.VISIBLE) {
                   mCardView.startAnimation(mHiddenAction);
                   mCardView.setVisibility(View.GONE);
               }
                //重新定位
                baidumap.clear();
                Location(locData);
            }
        });
    }


    //初始化地图
    private void initMap() {
        mmap = (MapView) findViewById(R.id.map);
        mmap.setVisibility(View.VISIBLE);
        baidumap = mmap.getMap();
     //   baidumap.setTrafficEnabled(true);
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
        lco.setCoorType("bd09ll"); // 设置坐标类型
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


        if(isFirstLocate){
            Location(locData);
        }

    }

    private void Location(MyLocationData locData) {

        baidumap.setMyLocationData(locData);
        isFirstLocate = false;
        baidumap.setMyLocationConfiguration(new MyLocationConfiguration(
                LocationMode.NORMAL, true, null));

        //移动地图到指定位置
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
                    //请求垃圾桶位置
                    GetMarkerLocation(lati_,long_);
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
        EventBus.getDefault().unregister(this);
        // 关闭定位图层
        baidumap.setMyLocationEnabled(false);
    }


    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this,MessageActivity.class));
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

    @Subscribe
    public void onEventMainThread(final SimpleBusBean event) {
        if(event.getBus_id() == 2){
            updateUI();
        }else if(event.getBus_id() == 3){
            finish();
        }
    }

}
