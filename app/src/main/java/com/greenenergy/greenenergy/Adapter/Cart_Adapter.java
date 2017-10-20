package com.greenenergy.greenenergy.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.greenenergy.greenenergy.Bean.CartInfo;
import com.greenenergy.greenenergy.Bean.Market_info;
import com.greenenergy.greenenergy.R;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by surine on 2017/9/22.
 */

public class Cart_Adapter extends BaseItemDraggableAdapter<CartInfo, BaseViewHolder> {

    public Cart_Adapter(int layoutResId, List<CartInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CartInfo item) {
        List<Market_info> market_infos =
                DataSupport.where("good_id = ?", item.getGood_id())
                        .find(Market_info.class);
        if (market_infos.size() != 0) {
            helper.setText(R.id.cart_price, "￥" + market_infos.get(0).getPrice());
            helper.setText(R.id.cart_title, market_infos.get(0).getTitle());
            Glide.with(mContext).load(market_infos.get(0).getPicture_url()).into((ImageView) helper.getView(R.id.cart_iamge));
            helper.addOnClickListener(R.id.cart_choose);
            helper.setChecked(R.id.checkBox, item.isChecked());
        }
    }

}