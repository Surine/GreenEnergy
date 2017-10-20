package com.greenenergy.greenenergy.UI;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.greenenergy.greenenergy.Adapter.Cart_Adapter;
import com.greenenergy.greenenergy.Bean.CartInfo;
import com.greenenergy.greenenergy.Bean.Market_info;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import moe.feng.alipay.zerosdk.AlipayZeroSdk;

public class CartActivity extends AppCompatActivity implements OnItemSwipeListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private CheckBox mCheckBox;
    private TextView sum;
    private Button pay;
    private double Price = 0;
    private Button mButton;
    private Cart_Adapter adapter;
    private List<CartInfo> mCart_infos = new ArrayList<>();
    private Market_info market_info;
    private EditText name;
    private EditText phone;
    private EditText addr;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        StatusUI.StatusUISetting(this,"#50000000");
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("购物车");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        sum = (TextView) findViewById(R.id.sum);
        sum.setText("总计："+Price+"元");
        pay = (Button) findViewById(R.id.pay);
        mButton = (Button) findViewById(R.id.add);
        mButton.setOnClickListener(this);



        pref = getSharedPreferences("data",MODE_PRIVATE);
        mButton.setText(pref.getString("ADDR_2","添加收货地址"));
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("核实信息");
                builder.setMessage("收货地址："+pref.getString("ADDR_2","")+"\n联系人："+pref.getString("NAME","")
                +"\n联系方式："+pref.getString("PHONE_2",""));
                builder.setPositiveButton("正确", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //启动支付宝
                        AlipayZeroSdk.startAlipayClient(CartActivity.this,"FKX03048OCUOKHAXYNA50C");
                    }
                });
                builder.setNegativeButton("错误", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        mCart_infos = DataSupport.findAll(CartInfo.class);
        recyclerView = (RecyclerView) findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter =  new Cart_Adapter(R.layout.item_cart,mCart_infos);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        recyclerView.setAdapter(adapter);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

       // 开启滑动删除
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(this);

        mCheckBox.setVisibility(View.GONE);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CheckBox checkBox = (CheckBox) view;
                if(checkBox.isChecked()){
                     market_info =
                            DataSupport.where("good_id = ?",
                                    mCart_infos.get(position).getGood_id())
                                    .find(Market_info.class).get(0);
                    Price = Price + market_info.getPrice();
                }else{
                    market_info =
                            DataSupport.where("good_id = ?",
                                    mCart_infos.get(position).getGood_id())
                                    .find(Market_info.class).get(0);
                    Price = Price - market_info.getPrice();
                }
                sum.setText("总计："+Price+"元");
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

    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
        market_info =
                DataSupport.where("good_id = ?",
                        mCart_infos.get(pos).getGood_id())
                        .find(Market_info.class).get(0);
        Price = Price - market_info.getPrice();
         sum.setText("总计："+Price+"元");
         DataSupport.delete(CartInfo.class,mCart_infos.get(pos).getId());
         DataSupport.delete(Market_info.class,DataSupport.where("good_id = ?",mCart_infos.get(pos).getGood_id()).find(Market_info.class).get(0).getId());
    }

    @Override
    public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
    }

    @Override
    public void onClick(View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.addr_buy,null);
          name = (EditText) view.findViewById(R.id.name);
         phone = (EditText) view.findViewById(R.id.phone);
          addr = (EditText) view.findViewById(R.id.addr);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("填写收货地址");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!name.getText().toString().equals("")||
                        !phone.getText().toString().equals("")||
                           !addr.getText().toString().equals("")
                        ) {
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("NAME", name.getText().toString());
                    editor.putString("PHONE_2", phone.getText().toString());
                    editor.putString("ADDR_2", addr.getText().toString());
                    editor.apply();
                    mButton.setText(addr.getText().toString());
                }else{
                    Toast.makeText(CartActivity.this,"填写不完整",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
