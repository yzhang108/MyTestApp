package com.example.mytestapplication.widge.calendar;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytestapplication.utils.AppLogger;

import org.joda.time.LocalDate;

/**
 * Created by 张艳 on 2017/5/12.
 */

public class CalendarBasePageAdapter extends PagerAdapter{
    protected SparseArray<RecyclerView> monthViews = new SparseArray<>();
    protected SparseArray<TextRVAdapter> monthAdapter = new SparseArray<>();
    public final static int ITEM_COUNT = 3;
    protected int viewHeight;
    protected LocalDate mStartTime;
    protected int startPos = 0;
    protected OnCalendarClickListener onCalendarClickListener;
    private final static int PAGE_COUNT=5000;

    public CalendarBasePageAdapter(int viewHeight, LocalDate mStartTime,OnCalendarClickListener listener) {
        this.viewHeight = viewHeight;
        this.mStartTime = mStartTime;
        startPos = PAGE_COUNT / 2;
        this.onCalendarClickListener=listener;
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
        AppLogger.e("monthAdapter.get(pos)="+monthAdapter.get(pos));
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
     * @param selectedDate
     */
    public void changeStartDate(LocalDate selectedDate){

    }

    public void notifyCurrentItem(int position){
        int pos = compulatePosition(position);
        if (monthAdapter.get(pos) != null) {
            monthAdapter.get(pos).notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(Object object) {
//        return POSITION_NONE;
        AppLogger.e("super.getItemPosition(object)="+super.getItemPosition(object));
        return super.getItemPosition(object);
    }
}
