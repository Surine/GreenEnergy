package com.greenenergy.greenenergy.UI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.greenenergy.greenenergy.Adapter.Market_Adapter;
import com.greenenergy.greenenergy.Bean.Market_info;
import com.greenenergy.greenenergy.Init.BaseActivity;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.Utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MarketActivity extends BaseActivity {
    List<Market_info> mMarket_infos = new ArrayList<>();
    private Market_Adapter adapter;
    private RecyclerView recyclerView;
    private List<Integer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        StatusUI.StatusUISetting(this,"#50000000");
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("绿执商城");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        list=new ArrayList<>();
        list.add(R.drawable.back2);
        list.add(R.drawable.back);
        list.add(R.drawable.back2);
        for(int i = 0;i<=5;i++){
            initData();
        }

        recyclerView = (RecyclerView) findViewById(R.id.market_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter =  new Market_Adapter(R.layout.item_market,mMarket_infos);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        View headerView = getHeaderView(0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//               // Toast.makeText(MarketActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        adapter.addHeaderView(headerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MarketActivity.this,Market_detail_Activity.class).
                        putExtra("MINFO", (Serializable) mMarket_infos.get(position));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(
                                    MarketActivity.this,view,"goods_info_image").toBundle());
                }else{
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private View getHeaderView(int type, View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.include_header, (ViewGroup) recyclerView.getParent(), false);
        if (type == 0) {
            Banner banner = (Banner) view.findViewById(R.id.banner);
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置图片集合
            banner.setImages(list);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.RotateUp);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(3000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
            banner.start();

            LinearLayout lin1 = (LinearLayout) view.findViewById(R.id.lin_icon_1);
            LinearLayout lin2 = (LinearLayout) view.findViewById(R.id.lin_icon_2);
            LinearLayout lin3 = (LinearLayout) view.findViewById(R.id.lin_icon_3);

            lin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MarketActivity.this,"周边",Toast.LENGTH_SHORT).show();
                }
            });
            lin2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MarketActivity.this,"会员",Toast.LENGTH_SHORT).show();
                }
            });
            lin3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MarketActivity.this,"积分",Toast.LENGTH_SHORT).show();
                }
            });
        }
        view.setOnClickListener(listener);
        return view;
    }


    private void initData() {
       Market_info m_info = new Market_info(1,"GE1","家用拼接桶","https://item.taobao.com/item.htm?spm=5148.7631246.0.0.674c7e9ai6EGP2&id=532808451085",
               "http://p4.so.qhimgs1.com/bdr/_240_/t0134b1ccf384b08844.jpg",122.0);
      mMarket_infos.add(m_info);
        m_info = new Market_info(2,"GE2","未来垃圾桶","http://www.baidu.com",
                "http://p2.so.qhimgs1.com/bdr/_240_/t01afe8937921316b92.jpg",122.0);
        mMarket_infos.add(m_info);
        m_info = new Market_info(3,"GE3","小爱同学","http://www.baidu.com",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=246929195,3097830668&fm=11&gp=0.jpg",299.0);
        mMarket_infos.add(m_info);
        m_info = new Market_info(4,"GE4","小米mix2","http://www.baidu.com",
                "http://p0.so.qhimgs1.com/bdr/_240_/t0103d90f8384661570.jpg",3499.0);
        mMarket_infos.add(m_info);
        m_info = new Market_info(5,"GE5","小蚁摄像头","http://www.baidu.com",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=462506081,2959408709&fm=27&gp=0.jpg",300.0);
        mMarket_infos.add(m_info);
        m_info = new Market_info(6,"GE6","回收服务","http://www.baidu.com",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2211138326,1995492892&fm=27&gp=0.jpg",1.0);
        mMarket_infos.add(m_info);
        m_info = new Market_info(7,"GE7","烟雾报警器","http://www.baidu.com",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2598285606,3143182887&fm=27&gp=0.jpg",100.0);
        mMarket_infos.add(m_info);
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
