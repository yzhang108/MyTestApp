package com.example.mytestapplication.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.model.RecyclerViewDataInfo;
import com.example.mytestapplication.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 张艳 on 2016/7/28.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    public List<RecyclerViewDataInfo> dataInfos = new ArrayList<>();
    public Context context;
    private List<Integer> itemHeight = new ArrayList<>();
    private OnItemClickListener listener;

    public HomeAdapter(Context context, List<RecyclerViewDataInfo> infos, OnItemClickListener listener) {
        this.context = context;
        if (infos != null) {
            dataInfos.addAll(infos);
        }
        this.listener = listener;
        itemHeight.add(DensityUtil.dip2px(context, 90));
        itemHeight.add(DensityUtil.dip2px(context, 130));
        itemHeight.add(DensityUtil.dip2px(context, 150));
        itemHeight.add(DensityUtil.dip2px(context, 110));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.itemName.setText(dataInfos.get(position).text);
        ViewGroup.LayoutParams params = holder.itemName.getLayoutParams();
        final int pos = (new Random()).nextInt(4);
        Log.i("random", "pos=" + pos);
        params.height = itemHeight.get(pos);
        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataInfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.textview_name);
        }


    }

    public interface OnItemClickListener {
        public void onClickListener(int pos);
    }
}
