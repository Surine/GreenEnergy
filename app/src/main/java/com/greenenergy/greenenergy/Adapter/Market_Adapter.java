package com.greenenergy.greenenergy.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.greenenergy.greenenergy.Bean.Market_info;
import com.greenenergy.greenenergy.R;

import java.util.List;

/**
 * Created by surine on 2017/9/14.
 */

public class Market_Adapter extends BaseQuickAdapter<Market_info, BaseViewHolder> {

    public Market_Adapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Market_info item) {
        helper.setText(R.id.title,item.getTitle());
        helper.setText(R.id.price,"￥"+item.getPrice());
        Glide.with(mContext).load(item.getPicture_url()).into((ImageView) helper.getView(R.id.back));
        helper.addOnClickListener(R.id.back);
    }
}
