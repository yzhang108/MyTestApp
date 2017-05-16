package com.example.mytestapplication.widge.calendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.widge.calendar.CalendaryUtil;
import com.example.mytestapplication.widge.calendar.adapter.BaseDayAdapter;
import com.example.mytestapplication.widge.calendar.adapter.MonthPageAdapter;
import com.example.mytestapplication.widge.calendar.adapter.WeekPageAdapter;
import com.example.mytestapplication.widge.calendar.animation.AutoMoveAnimation;
import com.example.mytestapplication.widge.calendar.animation.CalendarViewMoveAnimation;
import com.example.mytestapplication.widge.calendar.entity.CalendarState;
import com.example.mytestapplication.widge.calendar.listener.OnDayClickListener;

import org.joda.time.LocalDate;

import java.util.Locale;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static com.example.mytestapplication.widge.calendar.CalendaryUtil.ONE_WEEK_DAYS;


/**
 * Created by 张艳 on 2017/5/8.
 */
@TargetApi(11)
public class MyCalendaryView extends LinearLayout {
    private Context mContext;
    private RecyclerView weekBarView;
    private ViewPager monthView, weekView;
    private LinearLayout legendView;
    private TextView tvSelectedDateShow;
    private TextView tvToday;

    private BaseDayAdapter weekBarAdapter;
    private MonthPageAdapter monthPageAdapter;
    private WeekPageAdapter weekPageAdapter;
    private View mDragView;
    private FrameLayout mCalendarView;
    private float mDownPosition[] = new float[2];
    private boolean mIsScrolling = false;
    private int mMinDistance = 0;
    private int rowHegiht = 0;
    private CalendarState mState = CalendarState.MONTH;
    private boolean isLegendVisible = false;
    private int mAutoScrollDistance;
    private boolean mCurrentRowsIsSix = false;
    private int currentSelectDayRowNum = 1;

    private LocalDate initDate;//初始化时系统默认选中的时间,是整个时间计算的参照物，相当于坐标元点
    private LocalDate selectedDate;
    private OnDayClickListener onPickWeekDayListener, onPickMonthDayListener;


    public MyCalendaryView(Context context) {
        this(context, null);
    }

    public MyCalendaryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.calendary_view, this, true);
        weekBarView = (RecyclerView) findViewById(R.id.recycler_week_text);
        monthView = (ViewPager) findViewById(R.id.viewpage_month_days);
        weekView = (ViewPager) findViewById(R.id.viewpage_week_days);
        legendView = (LinearLayout) findViewById(R.id.llyt_tag);
        tvSelectedDateShow = (TextView) findViewById(R.id.tv_year_and_month_text);
        tvToday = (TextView) findViewById(R.id.tv_today_text);
        mCalendarView = (FrameLayout) findViewById(R.id.frame_days);
        mMinDistance = getResources().getDimensionPixelSize(R.dimen.calendar_min_distance);
        rowHegiht = getResources().getDimensionPixelSize(R.dimen.calendary_row_1_height);
        mAutoScrollDistance = getResources().getDimensionPixelSize(R.dimen.auto_scroll_distance);
        setLegendVisible(true);

        initDate = new LocalDate(2017, 5, 31);
        setSelectedDate(initDate);
        mCurrentRowsIsSix = CalendaryUtil.getMonthRows(selectedDate.getYear(), selectedDate.getMonthOfYear()) == 6;

        initListener();
        initData();
    }


    private void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        showCurrDate(selectedDate);
        currentSelectDayRowNum = CalendaryUtil.getWeekRow(selectedDate.getYear(), selectedDate.getMonthOfYear(), selectedDate.getDayOfMonth());
    }


    private void initData() {
        initTitle();
        initMonthDate();
        initWeekDate();
    }

    private void showCurrDate(LocalDate date) {
        if (date == null)
            return;
        String showTime = date.toString("yyyy年MM月dd日", Locale.CHINESE);
        tvSelectedDateShow.setText(showTime);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        weekBarView.setLayoutManager(new GridLayoutManager(mContext, ONE_WEEK_DAYS));
        String[] dayTitles = mContext.getResources().getStringArray(R.array.calendar_week);
        weekBarAdapter = new BaseDayAdapter(mContext, CalendaryUtil.getTitleWeekShow(dayTitles));
        weekBarView.setAdapter(weekBarAdapter);
    }

    private void refreshWeekViewWidthSelectedDate() {
        int weekDuration = CalendaryUtil.getWeeksAgo(initDate, selectedDate);
        int position = weekPageAdapter.getStartPos() + weekDuration;
        if (weekPageAdapter != null) {
            weekPageAdapter.changeStartDate(selectedDate);
        }
        if (position != weekView.getCurrentItem()) {
            weekView.setCurrentItem(position, false);
        } else {
            weekPageAdapter.notifyCurrentItem(weekView.getCurrentItem());
        }
    }

    private void refreshMonthViewWidthSelectedDate() {
        int monthDuration = CalendaryUtil.getMonthsAgo(initDate, selectedDate);
        int monthPosition = monthPageAdapter.getStartPos() + monthDuration;
        if (monthPageAdapter != null) {
            monthPageAdapter.changeStartDate(selectedDate);
        }
        if (monthPosition != monthView.getCurrentItem()) {
            monthView.setCurrentItem(monthPosition, false);
        } else {
            monthPageAdapter.notifyCurrentItem(monthView.getCurrentItem());
        }
    }

    private void initListener() {
        onPickMonthDayListener = new OnDayClickListener() {
            @Override
            public void onDatePicked(LocalDate date) {
                int monthDuration = CalendaryUtil.getMonthsAgo(selectedDate, date);
                monthPageAdapter.changeStartDate(date);
                setSelectedDate(date);
                if (monthDuration != 0) {
                    int mposition = monthView.getCurrentItem() + monthDuration;
                    monthView.setCurrentItem(mposition, true);
                }

            }
        };
        onPickWeekDayListener = new OnDayClickListener() {
            @Override
            public void onDatePicked(LocalDate date) {
                setSelectedDate(date);
                weekPageAdapter.changeStartDate(date);
            }
        };
    }

    /**
     * 初始化折叠后的一周日期
     */
    private void initWeekDate() {
        weekPageAdapter = new WeekPageAdapter(rowHegiht, initDate, onPickWeekDayListener);
        weekView.setAdapter(weekPageAdapter);
        weekView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                weekPageAdapter.notifyCurrentItem(position);
                LocalDate date = weekPageAdapter.getShowDate(position);
                if (date != null) {
                    if (date.isEqual(selectedDate)) {
                        autoScrollWhenRowNumChange();
                        return;
                    }
                    setSelectedDate(date);
                    autoScrollWhenRowNumChange();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        weekView.setCurrentItem(weekPageAdapter.getStartPos(), false);
    }

    /**
     * 初始化展开时的一月日期
     */
    private void initMonthDate() {
        monthPageAdapter = new MonthPageAdapter(0, initDate, onPickMonthDayListener);
        monthView.setAdapter(monthPageAdapter);
        monthView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                monthPageAdapter.notifyCurrentItem(position);
                LocalDate date = monthPageAdapter.getShowDate(position);
                if (date != null) {
                    if (date.isEqual(selectedDate)) {
                        autoScrollWhenRowNumChange();
                        return;
                    }
                    setSelectedDate(date);
                    autoScrollWhenRowNumChange();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        monthView.setCurrentItem(monthPageAdapter.getStartPos(), false);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (childCount != 5) {
            throw new IllegalStateException("calendarview layout must have exactly 1 children!");
        }
        int selectTimeHeight = getChildAt(0).getLayoutParams().height;
        int weekbarHeight = getChildAt(1).getLayoutParams().height;
        mDragView = getChildAt(childCount - 1);
        if (mDragView.getBackground() == null) {
            mDragView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        setViewHeight(mDragView, height - selectTimeHeight - weekbarHeight - rowHegiht);
        setViewHeight(this, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.height != height) {
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void printY() {
//        float frameVPY = mCalendarView.getY();
//        AppLogger.e("monthView.getY()=" + monthView.getY() + ",frameVPY=" + frameVPY + ",mdragviewY=" + mDragView.getY());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case ACTION_DOWN:
                mDownPosition[0] = ev.getRawX();
                mDownPosition[1] = ev.getRawY();
//                AppLogger.i("mDownPosition[0]=" + mDownPosition[0] + ",mDownPosition[1]=" + mDownPosition[1]);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case ACTION_DOWN:
                mDownPosition[0] = event.getRawX();
                mDownPosition[1] = event.getRawY();

                return true;
            case ACTION_MOVE:
                if (!mIsScrolling) {
                    if (mState == CalendarState.MONTH) {
                        refreshWeekViewWidthSelectedDate();
                    } else {
                        refreshMonthViewWidthSelectedDate();
                    }
                }
                mIsScrolling = true;
                weekView.setVisibility(INVISIBLE);
                float distance = event.getRawY() - mDownPosition[1];
                dragView(distance);

                mDownPosition[0] = event.getRawX();
                mDownPosition[1] = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                finishScroll();
                mDownPosition[0] = 0;
                mDownPosition[1] = 0;
                mIsScrolling = false;
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsScrolling)
            return true;
        switch (ev.getActionMasked()) {
            case ACTION_MOVE:
//                AppLogger.e("这里是判断结束条件吧,即判断是否终止事件传递");
                float x = ev.getRawX();
                float y = ev.getRawY();
                float distanceX = Math.abs(x - mDownPosition[0]);
                float distanceY = Math.abs(y - mDownPosition[1]);
                if (distanceY > distanceX * 2.0f && distanceY > mMinDistance) {
                    return (mState == CalendarState.MONTH && y < mDownPosition[1] && checkDragViewCanScrollUp()) || (mState == CalendarState.WEEK && y > mDownPosition[1] && checkDragViewCanScrollDown());
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void dragView(float distance) {
        if (distance < 0) {
            drawUp(distance);
        } else if (distance > 0) {
            drawDown(distance);
        }
    }

    private void drawUp(float distanceY) {
        printY();
        float dragViewScrollY = Math.min(Math.abs(distanceY), mAutoScrollDistance);
        float canDragViewScrollY = getDragViewCanUpScope();
        float dragViewTransY = mDragView.getY() - Math.min(dragViewScrollY, canDragViewScrollY);
        mDragView.setY(dragViewTransY);

        float scrollY = dragViewScrollY / (mCurrentRowsIsSix ? 5.0f : 4.0f);
        float canMonthViewScrollY = getMonthViewCanUpScope();
        float monthViewTransY = monthView.getY() - Math.min(scrollY * (currentSelectDayRowNum), canMonthViewScrollY);
        monthView.setY(monthViewTransY);

        if (isLegendVisible)
            legendView.setY(compulateLegendViewTop() + monthViewTransY);
    }

    private void drawDown(float distanceY) {
        float dragviewScrollY = Math.min(Math.abs(distanceY), mAutoScrollDistance);
        float canScrollY = getDragViewCanDownScope();
        float transY = mDragView.getY() + Math.min(dragviewScrollY, canScrollY);
        mDragView.setY(transY);

        float scrollY = dragviewScrollY / (mCurrentRowsIsSix ? 5.0f : 4.0f);
        float canMonthViewScrollY = getMonthViewCanDownScope();
        float monthViewTransY = monthView.getY() + Math.min(scrollY * (currentSelectDayRowNum), canMonthViewScrollY);
        monthView.setY(monthViewTransY);

        if (isLegendVisible)
            legendView.setY(compulateLegendViewTop() + monthViewTransY);
    }

    private void toggleState() {
        if (mState == CalendarState.MONTH) {
            setState(CalendarState.WEEK);
        } else {
            setState(CalendarState.MONTH);
        }
    }

    @TargetApi(11)
    private void setState(CalendarState state) {
        this.mState = state;
        if (state == CalendarState.MONTH) {
            weekView.setVisibility(INVISIBLE);
            legendView.setY(compulateLegendViewTop());
            legendView.setVisibility(isLegendVisible ? VISIBLE : GONE);
            monthView.setY(compulateMothViewMaxTop());
            if (mDragView != null)
                mDragView.setY(compulateDragViewMaxTop());
        } else if (state == CalendarState.WEEK) {
            weekView.setVisibility(VISIBLE);
            legendView.setVisibility(GONE);
            monthView.setY(compulateMothViewMinTop());
            if (mDragView != null)
                mDragView.setY(compulateDragViewMinTop());
        }
    }

    private float getDragViewCanUpScope() {
        return mDragView.getY() - compulateDragViewMinTop();
    }

    private float getDragViewCanDownScope() {
        return compulateDragViewMaxTop() - mDragView.getY();
    }

    private float getMonthViewCanUpScope() {
        return monthView.getY() - compulateMothViewMinTop();
    }

    private float getMonthViewCanDownScope() {
        return compulateMothViewMaxTop() - monthView.getY();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setState(mState);
    }

    private int compulateMothViewMinTop() {
        return -rowHegiht * (currentSelectDayRowNum);
    }

    private int compulateMothViewMaxTop() {
        return 0;
    }

    private int compulateDragViewMinTop() {
        return mCalendarView.getTop() + weekView.getHeight();
    }

    private int compulateDragViewMaxTop() {
        return mCalendarView.getTop() + rowHegiht * (mCurrentRowsIsSix ? 6 : 5) + (isLegendVisible ? legendView.getHeight() : 0);
    }


    private int compulateLegendViewTop() {
        return mCalendarView.getTop() + rowHegiht * (mCurrentRowsIsSix ? 6 : 5);
    }

    private boolean checkDragViewCanScrollUp() {
        if (mState == CalendarState.MONTH && mDragView.getY() > compulateDragViewMinTop())
            return true;
        return false;
    }

    private boolean checkDragViewCanScrollDown() {
        if (mState == CalendarState.WEEK && mDragView.getY() < compulateDragViewMaxTop())
            return true;
        return false;
    }

    private void finishScroll() {
        if (mDragView.getY() <= (compulateDragViewMinTop() + rowHegiht * 1.5f)) {
//            位于顶部，如果当前是展开状态，那么向上折叠,如果当前是折叠状态，那么继续折叠
            CalendarViewMoveAnimation calendarViewMoveAnimation = new CalendarViewMoveAnimation(this, -mAutoScrollDistance);
            calendarViewMoveAnimation.setDuration(50);
            calendarViewMoveAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setState(CalendarState.WEEK);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mDragView.startAnimation(calendarViewMoveAnimation);
        } else if (mDragView.getY() < compulateDragViewMaxTop() - rowHegiht * 1.5f) {
//            位于中间，如果当前是展开状态，那么向上折叠,如果当前是折叠状态，那么向下展开
            float distance = mAutoScrollDistance;
            if (mState == CalendarState.MONTH) {
                distance = -distance;
            }
            CalendarViewMoveAnimation calendarViewMoveAnimation = new CalendarViewMoveAnimation(this, distance);
            calendarViewMoveAnimation.setDuration(300);
            calendarViewMoveAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    toggleState();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mDragView.startAnimation(calendarViewMoveAnimation);
        } else {
//            位于底部，如果当前是展开状态，那么向下展开,如果当前是折叠状态，那么向下展开
            CalendarViewMoveAnimation calendarViewMoveAnimation = new CalendarViewMoveAnimation(this, mAutoScrollDistance);
            calendarViewMoveAnimation.setDuration(50);
            calendarViewMoveAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setState(CalendarState.MONTH);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mDragView.startAnimation(calendarViewMoveAnimation);
        }
    }


    private void autoScrollWhenRowNumChange() {
        int rows = CalendaryUtil.getMonthRows(selectedDate.getYear(), selectedDate.getMonthOfYear());
        if (mState == CalendarState.MONTH) {
            if (mCurrentRowsIsSix && rows != 6) {
                //向上滑动
                AutoMoveAnimation animation = new AutoMoveAnimation(mDragView, -rowHegiht, 200, 1.5f);
                mDragView.startAnimation(animation);
            } else if (!mCurrentRowsIsSix && rows != 5) {
//            向下滑动
                AutoMoveAnimation animation = new AutoMoveAnimation(mDragView, rowHegiht, 200, 1.5f);
                mDragView.startAnimation(animation);
            }
        }
        mCurrentRowsIsSix = (rows == 6);
    }

    /**
     * 是否显示图例
     *
     * @param isVisible
     */
    public void setLegendVisible(boolean isVisible) {
        isLegendVisible = isVisible;
        legendView.setVisibility(isLegendVisible ? VISIBLE : GONE);
    }

    public void setViewState(CalendarState state) {
        setState(state);
    }

}