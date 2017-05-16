package com.example.mytestapplication.widge.calendar;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by 张艳 on 2017/5/15.
 */

public class MonthDayRVAdapter extends TextRVAdapter {
    public MonthDayRVAdapter(Context context, List<DateShowData> dateShowDatas) {
        super(context, dateShowDatas);
    }

    public MonthDayRVAdapter(Context context, List<DateShowData> dateShowDatas, LocalDate date, OnCalendarClickListener dateClick) {
        super(context, dateShowDatas, date, dateClick);
    }

    public void changeSelectedDate(int day) {
        if (dateShowDatas != null && dateShowDatas.size() >= 28) {
            //获取一个中间日期，来确定该月是哪个月
            DateShowData dateShowData = dateShowDatas.get(dateShowDatas.size() / 2);
            int month = dateShowData.date.getMonthOfYear();
            int year = dateShowData.date.getYear();
            int days = CalendaryUtil.getMonthDays(year, month);
            LocalDate newDate = null;
            if (day <= days) {
                newDate = new LocalDate(year, month, day);
            } else {
                newDate = new LocalDate(year, month, days);
            }
            if (newDate.isEqual(mSelectedDate))
                return;
            mSelectedDate = newDate;
        }

    }
}
