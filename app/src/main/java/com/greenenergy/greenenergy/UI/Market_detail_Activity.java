package com.greenenergy.greenenergy.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.greenenergy.greenenergy.Bean.CartInfo;
import com.greenenergy.greenenergy.Bean.Market_info;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.R;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

public class Market_detail_Activity extends AppCompatActivity {
    Market_info mMarket_info;
    private ImageView mImageView;
    private ImageView mCart;
    private TextView add_cart;
    private TextView title;
    private TextView price;
    private WebView mWebView;
    private RelativeLayout mRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);
        StatusUI.StatusUISetting(this,"#50000000");

        //set up the database
        Connector.getDatabase();

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("商品详情");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        mMarket_info = (Market_info) intent.getSerializableExtra("MINFO");

        mImageView = (ImageView) findViewById(R.id.image_market);
        mCart = (ImageView) findViewById(R.id.cart);
        mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Market_detail_Activity.this,CartActivity.class));
            }
        });
        add_cart = (TextView) findViewById(R.id.add_cart);
        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DataSupport.where("good_id = ?",mMarket_info.getGood_id()).find(CartInfo.class).size() != 0){
                    Toast.makeText(Market_detail_Activity.this,"已经在购物车了哟！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Market_detail_Activity.this, "添加成功，购物车等亲", Toast.LENGTH_SHORT).show();
                    CartInfo cart_info = new CartInfo();
                    cart_info.setChecked(true);
                    cart_info.setGood_id(mMarket_info.getGood_id());
                    cart_info.save();

                    Market_info market_info = new Market_info();
                    market_info.setGood_id(mMarket_info.getGood_id());
                    market_info.setInfo_url(mMarket_info.getInfo_url());
                    market_info.setPrice(mMarket_info.getPrice());
                    market_info.setTitle(mMarket_info.getTitle());
                    market_info.setPicture_url(mMarket_info.getPicture_url());
                    market_info.save();
                }
            }
        });
        title = (TextView) findViewById(R.id.goods_title);
        price = (TextView) findViewById(R.id.price);
        mWebView = (WebView) findViewById(R.id.webview_goods_detail);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.res1);

        title.setText(mMarket_info.getTitle());
        price.setText("￥"+mMarket_info.getPrice());
        mWebView.loadUrl(mMarket_info.getInfo_url());
        mWebView.setWebViewClient(new WebViewClient());

        Glide.with(this).
                load(mMarket_info.getPicture_url())
                .asBitmap()
                .into(new BitmapImageViewTarget(mImageView) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette p) {
                                    Palette.Swatch vibrant = p.getVibrantSwatch();
                                    Palette.Swatch mute = p.getLightMutedSwatch();
                                    if (vibrant != null) {
                                        mRelativeLayout.setBackgroundColor(vibrant.getRgb());
                                        title.setTextColor(vibrant.getTitleTextColor());
                                        price.setTextColor(vibrant.getBodyTextColor());
                                    } else if (mute != null) {
                                        mRelativeLayout.setBackgroundColor(mute.getRgb());
                                        title.setTextColor(mute.getTitleTextColor());
                                        price.setTextColor(mute.getBodyTextColor());
                                    } else {

                                    }
                                }
                            });
                        }
                });

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
