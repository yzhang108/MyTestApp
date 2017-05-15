package com.example.mytestapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytestapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张艳 on 2016/10/11.
 */

public class TestAdapter extends RecyclerView.Adapter {

    public List<String> dataInfos = new ArrayList<>();
    public Context context;

    public TestAdapter(List<String> dataInfos, Context context) {
        this.context = context;
        addData(dataInfos);
    }

    public void updateData(List<String> items){
        int oldSize=dataInfos.size();
        dataInfos.clear();
        notifyItemRangeRemoved(0,oldSize);
        addData(items);
    }

    public void addData(List<String> items){
        int oldSize=dataInfos.size();
        if(items!=null && items.size()>0){
            dataInfos.addAll(items);
            Log.i("zy","111");
            notifyItemRangeInserted(oldSize,items.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewAdapter holder = new MyViewAdapter(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewAdapter viewHolder=(MyViewAdapter)holder;
        viewHolder.itemName.setText(dataInfos.get(position));
    }

    @Override
    public int getItemCount() {
        return dataInfos.size();
    }

    private class MyViewAdapter extends RecyclerView.ViewHolder {

        public TextView itemName;

        public MyViewAdapter(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.textview_name);
        }
    }
}
