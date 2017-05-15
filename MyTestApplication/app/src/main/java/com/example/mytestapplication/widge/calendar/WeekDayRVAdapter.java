package com.example.mytestapplication.widge.calendar;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by 张艳 on 2017/5/15.
 */

public class WeekDayRVAdapter extends TextRVAdapter {
    public WeekDayRVAdapter(Context context, List<DateShowData> dateShowDatas) {
        super(context, dateShowDatas);
    }

    public WeekDayRVAdapter(Context context, List<DateShowData> dateShowDatas, LocalDate date, OnCalendarClickListener dateClick) {
        super(context, dateShowDatas, date, dateClick);
    }

    public void changeSelectedDate(int dayOfWeek){
        if(mSelectedDate!=null){
            int weedDay=mSelectedDate.getDayOfWeek();
            if(weedDay==7)
                weedDay=0;
            if(weedDay==dayOfWeek)
                return;
        }
        if(dateShowDatas!=null && dateShowDatas.size()>dayOfWeek){
            mSelectedDate=dateShowDatas.get(dayOfWeek).date;
        }
        notifyDataSetChanged();
//        AppLogger.e("mSelectedDate="+mSelectedDate.toString("yyyy-MM-dd"));
    }
}
