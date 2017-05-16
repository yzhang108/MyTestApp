package com.example.mytestapplication.widge.calendar;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.joda.time.LocalDate;

import java.util.List;

import static com.example.mytestapplication.widge.calendar.CalendaryUtil.ONE_WEEK_DAYS;

/**
 * Created by 张艳 on 2017/5/8.
 */

public class MonthPageAdapter extends CalendarBasePageAdapter {
    public MonthPageAdapter(int height, LocalDate date, OnCalendarClickListener listener) {
        super(height, date, listener);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LocalDate desTime = CalendaryUtil.getMonth(mStartTime, position - startPos);
        List<DateShowData> dateShowDatas = CalendaryUtil.getMonthShow(desTime.getYear(), desTime.getMonthOfYear());
        int pos = compulatePosition(position);
        if (monthViews.get(pos) == null) {
            RecyclerView recyclerView = new RecyclerView(container.getContext());
            recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), ONE_WEEK_DAYS));
            MonthDayRVAdapter textRVAdapter = new MonthDayRVAdapter(recyclerView.getContext(), dateShowDatas, desTime, onCalendarClickListener);
            recyclerView.setAdapter(textRVAdapter);
            monthViews.put(pos, recyclerView);
            monthAdapter.put(pos, textRVAdapter);
        } else {
            MonthDayRVAdapter textRVAdapter = (MonthDayRVAdapter)monthAdapter.get(pos);
            textRVAdapter.updateData(dateShowDatas, desTime);
            RecyclerView recyclerView = monthViews.get(pos);
            ViewParent parent = recyclerView.getParent();
            if (parent != null) {
                ViewGroup vp = (ViewGroup) parent;
                vp.removeView(recyclerView);
            }
        }
        container.addView(monthViews.get(pos));
        return monthViews.get(pos);
    }


    @Override
    public void changeStartDate(LocalDate selectedDate) {
        int day = selectedDate.getDayOfMonth();
        int monthDays = CalendaryUtil.getMonthDays(mStartTime.getYear(), mStartTime.getMonthOfYear());
        if (day <= monthDays) {
            mStartTime = mStartTime.plusDays(day - mStartTime.getDayOfMonth());
        } else {
            mStartTime = mStartTime.plusDays(monthDays - mStartTime.getDayOfMonth());
        }
        for(int i=0;i<monthAdapter.size();i++){
            MonthDayRVAdapter adapter=(MonthDayRVAdapter)monthAdapter.get(i);
            adapter.changeSelectedDate(day);
        }
    }

}
