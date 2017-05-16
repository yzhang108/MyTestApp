package com.example.mytestapplication.widge.calendar.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.utils.AppLogger;
import com.example.mytestapplication.widge.calendar.listener.OnDayClickListener;
import com.example.mytestapplication.widge.calendar.entity.DateShowData;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张艳 on 2017/5/8.
 */

public class BaseDayAdapter extends RecyclerView.Adapter {
    protected Context context;
    protected List<DateShowData> dateShowDatas = new ArrayList<>();
    protected LocalDate mSelectedDate;
    protected OnDayClickListener onDateClick;
    protected int colorNormal, colorSelected;


    public BaseDayAdapter(Context context, List<DateShowData> dateShowDatas) {
        this.context = context;
        this.dateShowDatas = dateShowDatas;
        colorNormal = ContextCompat.getColor(context, R.color.black);
        colorSelected = ContextCompat.getColor(context, R.color.red_light);
    }

    public BaseDayAdapter(Context context, List<DateShowData> dateShowDatas, LocalDate date, OnDayClickListener dateClick) {
        this.context = context;
        this.dateShowDatas = dateShowDatas;
        this.mSelectedDate = date;
        this.onDateClick = dateClick;
        colorNormal = ContextCompat.getColor(context, R.color.black);
        colorSelected = ContextCompat.getColor(context, R.color.red_light);
    }

    public void updateData(List<DateShowData> dateShowDatas, LocalDate selectDate) {
        this.mSelectedDate = selectDate;
        int oldSize=this.dateShowDatas.size();
        this.dateShowDatas.clear();
        notifyItemRangeRemoved(0,oldSize);
        if (dateShowDatas != null && dateShowDatas.size() > 0) {
            this.dateShowDatas.addAll(dateShowDatas);
            notifyItemRangeInserted(0,this.dateShowDatas.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(LayoutInflater.from(context).inflate(R.layout.calendary_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TextViewHolder viewHolder = (TextViewHolder) holder;
        viewHolder.textView.setText(dateShowDatas.get(position).text);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDateClick != null) {
                    mSelectedDate=dateShowDatas.get(position).date;
                    notifyDataSetChanged();
                    onDateClick.onDatePicked(mSelectedDate);
                }
            }
        });
        if (mSelectedDate != null) {
//            AppLogger.i(position+",mSelectedDate="+mSelectedDate.toString("yyyy-MM-dd")+",currShowDate="+dateShowDatas.get(position).date.toString("yyyy-MM-dd"));
            if (dateShowDatas.get(position).date.isEqual(mSelectedDate)) {
                viewHolder.textView.setTextColor(colorSelected);
            } else {
                viewHolder.textView.setTextColor(colorNormal);
            }
        }

    }

    @Override
    public int getItemCount() {
        return dateShowDatas.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public TextViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_content_show);
        }
    }


    public LocalDate getSelectedDate() {
        return mSelectedDate;
    }

    public void setSelectedDate(LocalDate mSelectedDate) {
        this.mSelectedDate = mSelectedDate;
        AppLogger.e("mSelectedDate="+mSelectedDate.toString("yyyy-MM-dd"));
        notifyDataSetChanged();
    }
}
