package com.example.mytestapplication.widge.calendar.adapter;

import android.content.Context;

import com.example.mytestapplication.widge.calendar.listener.OnDayClickListener;
import com.example.mytestapplication.widge.calendar.entity.DateShowData;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by 张艳 on 2017/5/15.
 */

public class WeekDayAdapter extends BaseDayAdapter {
    public WeekDayAdapter(Context context, List<DateShowData> dateShowDatas) {
        super(context, dateShowDatas);
    }

    public WeekDayAdapter(Context context, List<DateShowData> dateShowDatas, LocalDate date, OnDayClickListener dateClick) {
        super(context, dateShowDatas, date, dateClick);
    }

    public void changeSelectedDate(int dayOfWeek) {
        if (mSelectedDate != null) {
            int weedDay = mSelectedDate.getDayOfWeek();
            if (weedDay == 7)
                weedDay = 0;
            if (weedDay == dayOfWeek)
                return;
        }
        if (dateShowDatas != null && dateShowDatas.size() > dayOfWeek) {
            mSelectedDate = dateShowDatas.get(dayOfWeek).date;
        }
    }
}
