package com.greenenergy.greenenergy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.greenenergy.greenenergy.Bean.MessageBean;
import com.greenenergy.greenenergy.MyData.NetWorkData;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.UI.MessageInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/8/25.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
   private List<MessageBean> mMessageBean = new ArrayList<>();

    public MessageAdapter(List<MessageBean> messageBeen, Context context) {
        mMessageBean = messageBeen;
        mContext = context;
    }

    private Context mContext;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion = holder.getAdapterPosition();
                mContext.startActivity(new Intent(mContext, MessageInfoActivity.class).putExtra("INFO", mMessageBean.get(postion).getContent()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       MessageBean messageBean = mMessageBean.get(position);
        holder.title.setText(messageBean.getTitle());
        Log.d("XEREX",NetWorkData.IP_port+messageBean.getPictureUrl());
        Glide.with(mContext).load(NetWorkData.IP_port_80+messageBean.getPictureUrl()).error(R.drawable.back2).into(holder.back);
    }

    @Override
    public int getItemCount() {
        return mMessageBean.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView back;
        private TextView title;
        private TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            back = (ImageView) itemView.findViewById(R.id.back);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
