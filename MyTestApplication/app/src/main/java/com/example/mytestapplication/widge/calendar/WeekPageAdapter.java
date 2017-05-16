package com.example.mytestapplication.widge.calendar;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.mytestapplication.utils.AppLogger;

import org.joda.time.LocalDate;

import java.util.List;

import static com.example.mytestapplication.widge.calendar.CalendaryUtil.ONE_WEEK_DAYS;

/**
 * Created by 张艳 on 2017/5/8.
 */

public class WeekPageAdapter extends CalendarBasePageAdapter {
    public WeekPageAdapter(int viewHeight, LocalDate initDate, OnCalendarClickListener listener) {
        super(viewHeight, initDate, listener);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LocalDate desTime = CalendaryUtil.getWeek(mStartTime, position - startPos);
        List<DateShowData> dateShowDatas = CalendaryUtil.getWeekShow(desTime);
//        AppLogger.e("mstartTime=" + mStartTime.toString("yyyy-MM-dd") + ", desTime=" + desTime.toString("yyyy-MM-dd"));
        int pos = compulatePosition(position);
        if (monthViews.get(pos) == null) {
//            AppLogger.e("1111111");
            RecyclerView recyclerView = new RecyclerView(container.getContext());
            recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mViewHeight));
            recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), ONE_WEEK_DAYS));
            WeekDayRVAdapter textRVAdapter = new WeekDayRVAdapter(recyclerView.getContext(), dateShowDatas, desTime, onCalendarClickListener);
            recyclerView.setAdapter(textRVAdapter);
            monthViews.put(pos, recyclerView);
            monthAdapter.put(pos, textRVAdapter);
        } else {
//            AppLogger.e("2222222");
            WeekDayRVAdapter textRVAdapter = (WeekDayRVAdapter) monthAdapter.get(pos);
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
        int week = selectedDate.getDayOfWeek();
        if (week == 7) {
            week = 0;
        }
        int startWeek = mStartTime.getDayOfWeek();
        if (startWeek == 7) {
            startWeek = 0;
        }
        AppLogger.e("startWeek=" + mStartTime.getDayOfWeek() + ", selectWeek=" + selectedDate.getDayOfWeek() + ",====,startWeek=" + startWeek + ",selWeek=" + week);
        mStartTime = mStartTime.plusDays(week - startWeek);
        AppLogger.e("week, mStartTime=" + mStartTime.toString("yyyy-MM-dd"));
        for (int i = 0; i < monthAdapter.size(); i++) {
            ((WeekDayRVAdapter) monthAdapter.get(i)).changeSelectedDate(week);
        }
    }
}
