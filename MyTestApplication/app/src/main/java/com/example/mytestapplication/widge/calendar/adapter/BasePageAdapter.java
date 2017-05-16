package com.example.mytestapplication.widge.calendar.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytestapplication.widge.calendar.listener.OnDayClickListener;

import org.joda.time.LocalDate;

/**
 * Created by 张艳 on 2017/5/12.
 */

public class BasePageAdapter extends PagerAdapter {
    protected SparseArray<RecyclerView> monthViews = new SparseArray<>();
    protected SparseArray<BaseDayAdapter> monthAdapter = new SparseArray<>();
    public final static int ITEM_COUNT = 3;
    protected int mViewHeight;
    protected LocalDate mStartTime;
    protected int startPos = 0;
    protected OnDayClickListener onCalendarClickListener;
    private final static int PAGE_COUNT = 5000;

    public BasePageAdapter(int viewHeight, LocalDate mStartTime, OnDayClickListener listener) {
        this.mViewHeight = viewHeight;
        this.mStartTime = mStartTime;
        startPos = PAGE_COUNT / 2;
        this.onCalendarClickListener = listener;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    protected int compulatePosition(int position) {
        return position % ITEM_COUNT;
    }

    public LocalDate getShowDate(int position) {
        int pos = compulatePosition(position);
        if (monthAdapter.get(pos) != null) {
            return monthAdapter.get(pos).getSelectedDate();
        }
        return null;
    }


    public int getStartPos() {
        return startPos;
    }

    /**
     * 根据当前选定的时间，改变startTime
     *
     * @param selectedDate
     */
    public void changeStartDate(LocalDate selectedDate) {

    }

    public void notifyCurrentItem(int position) {
        int pos = compulatePosition(position);
        if (monthAdapter.get(pos) != null) {
            monthAdapter.get(pos).notifyDataSetChanged();
        }
    }


}
