package com.example.mytestapplication.widge.calendar;

import android.annotation.TargetApi;
import android.content.Context;
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
import com.example.mytestapplication.utils.AppLogger;

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
    private RecyclerView weekRecyclerView;
    private ViewPager monthVP, weekVP;
    private LinearLayout llytTag;
    private TextView tvSelectedYearAndMonth;
    private TextView tvToday;

    private TextRVAdapter titleAdapter;
    private MonthPageAdapter monthPageAdapter;
    private WeekPageAdapter weekPageAdapter;
    private View mDragView;
    private FrameLayout mDayFrameLayout;
    private int TOP_MIN = 0;
    private float mDownPosition[] = new float[2];
    private boolean mIsScrolling = false;
    private int mMinDistance = 0;
    private final int ROW_SIZE = 16;
    private int rowHegiht = 0;
    private CalendarState mState = CalendarState.OPEN;
    private boolean isShowTag = false;
    private int mAutoScrollDistance;
    private boolean mCurrentRowsIsSix = false;
    private int currentSelectDayRowNum = 2;
    private LocalDate initDate;//初始化时系统默认选中的时间,是整个时间计算的参照物，相当于坐标元点
    private LocalDate selectedDate;
    private OnCalendarClickListener onPickWeekDayListener, onPickMonthDayListener;


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
        weekRecyclerView = (RecyclerView) findViewById(R.id.recycler_week_text);
        monthVP = (ViewPager) findViewById(R.id.viewpage_month_days);
        weekVP = (ViewPager) findViewById(R.id.viewpage_week_days);
        llytTag = (LinearLayout) findViewById(R.id.llyt_tag);
        tvSelectedYearAndMonth = (TextView) findViewById(R.id.tv_year_and_month_text);
        tvToday = (TextView) findViewById(R.id.tv_today_text);
        mDayFrameLayout = (FrameLayout) findViewById(R.id.frame_days);
        mMinDistance = getResources().getDimensionPixelSize(R.dimen.calendar_min_distance);
        rowHegiht = getResources().getDimensionPixelSize(R.dimen.calendary_row_1_height);
        mAutoScrollDistance = getResources().getDimensionPixelSize(R.dimen.auto_scroll_distance);

        weekVP.setVisibility(INVISIBLE);
        llytTag.setVisibility(isShowTag ? VISIBLE : GONE);

        initDate = new LocalDate(2017, 5, 31);
        setSelectedDate(initDate);
        mCurrentRowsIsSix = CalendaryUtil.getMonthRows(selectedDate.getYear(), selectedDate.getMonthOfYear()) == 6;

        initListener();
        initData();
    }


    private void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        showCurrDate(selectedDate);
        currentSelectDayRowNum=CalendaryUtil.getWeekRow(selectedDate.getYear(),selectedDate.getMonthOfYear(),selectedDate.getDayOfMonth());
        AppLogger.e("currentSelectDayRowNum="+currentSelectDayRowNum);
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
        tvSelectedYearAndMonth.setText(showTime);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        weekRecyclerView.setLayoutManager(new GridLayoutManager(mContext, ONE_WEEK_DAYS));
        String[] dayTitles = mContext.getResources().getStringArray(R.array.calendar_week);
        titleAdapter = new TextRVAdapter(mContext, CalendaryUtil.getTitleWeekShow(dayTitles));
        weekRecyclerView.setAdapter(titleAdapter);
    }

    private void refreshWeekViewWidthSelectedDate(){
        int weekDuration = CalendaryUtil.getWeeksAgo(initDate, selectedDate);
        int position = weekPageAdapter.getStartPos() + weekDuration;
        if (weekPageAdapter != null) {
            weekPageAdapter.changeStartDate(selectedDate);
        }
        weekVP.setCurrentItem(position, false);
    }
    private void refreshMonthViewWidthSelectedDate(){
        int monthDuration = CalendaryUtil.getMonthsAgo(initDate, selectedDate);
        int monthPosition = monthPageAdapter.getStartPos() + monthDuration;
        if (monthPageAdapter != null) {
            monthPageAdapter.changeStartDate(selectedDate);
        }
        if (monthPosition != monthVP.getCurrentItem()) {
            monthVP.setCurrentItem(monthPosition, false);
        }
    }

    private void initListener() {
        onPickMonthDayListener = new OnCalendarClickListener() {
            @Override
            public void onDatePicked(LocalDate date) {
//                AppLogger.d("fromDate="+selectedDate.toString("yyyy-MM-dd")+",toDate="+date.toString("yyyy-MM-dd"));
                int monthDuration = CalendaryUtil.getMonthsAgo(selectedDate, date);
                monthPageAdapter.changeStartDate(date);
                setSelectedDate(date);
                refreshWeekViewWidthSelectedDate();
                if (monthDuration != 0) {
                    int mposition = monthVP.getCurrentItem() + monthDuration;
//                    AppLogger.e("monthDuration==" + monthDuration + ",mposition=" + mposition);
                    monthVP.setCurrentItem(mposition, true);
                }

            }
        };
        onPickWeekDayListener = new OnCalendarClickListener() {
            @Override
            public void onDatePicked(LocalDate date) {
                setSelectedDate(date);
                weekPageAdapter.changeStartDate(date);
                refreshMonthViewWidthSelectedDate();

            }
        };
    }

    /**
     * 初始化折叠后的一周日期
     */
    private void initWeekDate() {
        weekPageAdapter = new WeekPageAdapter(rowHegiht, initDate, onPickWeekDayListener);
        weekVP.setAdapter(weekPageAdapter);
        weekVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LocalDate date = weekPageAdapter.getShowDate(position);
//                AppLogger.d("weekadapter position="+position+",mSelectedDate="+selectedDate.toString("yyyy-MM-dd"));
                if (date != null) {
//                    AppLogger.d("weekadapter ,date="+date.toString("yyyy-MM-dd"));
                    if (date.isEqual(selectedDate)) {
//                        AppLogger.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                        autoScrollWhenRowNumChange();
                        return;
                    }
                    setSelectedDate(date);
                    refreshMonthViewWidthSelectedDate();
                    autoScrollWhenRowNumChange();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        weekVP.setCurrentItem(weekPageAdapter.getStartPos(), false);
    }

    /**
     * 初始化展开时的一月日期
     */
    private void initMonthDate() {
        int maxHeight = rowHegiht * ROW_SIZE;
        AppLogger.e("maxHeight=" + maxHeight);
        monthPageAdapter = new MonthPageAdapter(maxHeight, initDate, onPickMonthDayListener);
        monthVP.setAdapter(monthPageAdapter);
        monthVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                monthPageAdapter.printHeight();
                LocalDate date = monthPageAdapter.getShowDate(position);
//                AppLogger.d("monthadapter, position==" + position+",date="+date+",selectedDate="+selectedDate);
                if (date != null) {
                    if (date.isEqual(selectedDate)) {
//                        AppLogger.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                        autoScrollWhenRowNumChange();
                        return;
                    }
                    refreshWeekViewWidthSelectedDate();
                    setSelectedDate(date);
                    autoScrollWhenRowNumChange();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        monthVP.setCurrentItem(monthPageAdapter.getStartPos(), false);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int height = MeasureSpec.getSize(heightMeasureSpec);
//        AppLogger.e("childCount=" + childCount+",height="+height+",getHeight="+getHeight());
        if (childCount != 5) {
            throw new IllegalStateException("calendarview layout must have exactly 1 children!");
        }
        int selectTimeHeight = getChildAt(0).getLayoutParams().height;
        int weekbarHeight = getChildAt(1).getLayoutParams().height;
        TOP_MIN = selectTimeHeight + weekbarHeight;
        mDragView = getChildAt(childCount - 1);
        setViewHeight(mDragView, height - TOP_MIN - rowHegiht);
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
//        float frameVPY = mDayFrameLayout.getY();
//        AppLogger.e("monthVP.getY()=" + monthVP.getY() + ",frameVPY=" + frameVPY + ",mdragviewY=" + mDragView.getY());
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
//                AppLogger.i("mDownPosition[0]=" + mDownPosition[0] + ",mDownPosition[1]=" + mDownPosition[1]);

                return true;
            case ACTION_MOVE:
//                Log.i("ScheduleLayout", "onTouchEvent  ACTION_MOVE");
                mIsScrolling = true;
                weekVP.setVisibility(INVISIBLE);
                float distance = event.getRawY() - mDownPosition[1];
                dragView(distance);

                mDownPosition[0] = event.getRawX();
                mDownPosition[1] = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
//                Log.i("ScheduleLayout", "onTouchEvent  ACTION_UP");
            case MotionEvent.ACTION_CANCEL:
//                Log.i("ScheduleLayout", "onTouchEvent  ACTION_CANCEL");
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
//                AppLogger.e("x=" + x + ",y=" + y + ",distanceX=" + distanceX + ",distanceY=" + distanceY);
                if (distanceY > distanceX * 2.0f && distanceY > mMinDistance) {
                    return (mState == CalendarState.OPEN && y < mDownPosition[1] && checkDragViewCanScrollUp()) || (mState == CalendarState.CLOSE && y > mDownPosition[1] && checkDragViewCanScrollDown());
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
        float monthViewTransY = monthVP.getY() - Math.min(scrollY * (currentSelectDayRowNum ), canMonthViewScrollY);
        monthVP.setY(monthViewTransY);

    }

    private void drawDown(float distanceY) {
        float dragviewScrollY = Math.min(Math.abs(distanceY), mAutoScrollDistance);
        float canScrollY = getDragViewCanDownScope();
        float transY = mDragView.getY() + Math.min(dragviewScrollY, canScrollY);
        mDragView.setY(transY);

        float scrollY = dragviewScrollY / (mCurrentRowsIsSix ? 5.0f : 4.0f);
        float canMonthViewScrollY = getMonthViewCanDownScope();
        float monthViewTransY = monthVP.getY() + Math.min(scrollY * (currentSelectDayRowNum ), canMonthViewScrollY);
        monthVP.setY(monthViewTransY);
    }

    private void toggleState() {
        if (mState == CalendarState.OPEN) {
            setState(CalendarState.CLOSE);
        } else {
            setState(CalendarState.OPEN);
        }
    }

    @TargetApi(11)
    public void setState(CalendarState state) {
        this.mState = state;
        printY();
        if (state == CalendarState.OPEN) {
            weekVP.setVisibility(INVISIBLE);
            llytTag.setVisibility(isShowTag ? VISIBLE : GONE);
            monthVP.setY(compulateMothViewMaxTop());
            mDragView.setY(compulateDragViewMaxTop());
        } else if (state == CalendarState.CLOSE) {
            weekVP.setVisibility(VISIBLE);
            llytTag.setVisibility(GONE);
            monthVP.setY(compulateMothViewMinTop());
            mDragView.setY(compulateDragViewMinTop());
        }
        printY();
    }

    private float getDragViewCanUpScope() {
        return mDragView.getY() - compulateDragViewMinTop();
    }

    private float getDragViewCanDownScope() {
        return compulateDragViewMaxTop() - mDragView.getY();
    }

    private float getMonthViewCanUpScope() {
        return monthVP.getY() - compulateMothViewMinTop();
    }

    private float getMonthViewCanDownScope() {
        return compulateMothViewMaxTop() - monthVP.getY();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        AppLogger.e("onLayout,monthVP.getHeight()="+monthVP.getHeight()+",weekVP.getHeight()="+weekVP.getHeight()+",frame.height="+mDayFrameLayout.getHeight());
        setState(mState);
    }

    private int compulateMothViewMinTop() {
        return -rowHegiht * (currentSelectDayRowNum );
    }

    private int compulateMothViewMaxTop() {
        return 0;
    }

    private int compulateDragViewMinTop() {
        return mDayFrameLayout.getTop() + weekVP.getHeight();
    }

    private int compulateDragViewMaxTop() {
//        return mDayFrameLayout.getTop() + monthVP.getHeight();
        return mDayFrameLayout.getTop() + rowHegiht*(mCurrentRowsIsSix?6:5);
    }

    private boolean checkDragViewCanScrollUp() {
        if (mState == CalendarState.OPEN && mDragView.getY() > compulateDragViewMinTop())
            return true;
        return false;
    }

    private boolean checkDragViewCanScrollDown() {
        if (mState == CalendarState.CLOSE && mDragView.getY() < compulateDragViewMaxTop())
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
                    setState(CalendarState.CLOSE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mDragView.startAnimation(calendarViewMoveAnimation);
        } else if (mDragView.getY() < compulateDragViewMaxTop() - rowHegiht * 1.5f) {
//            位于中间，如果当前是展开状态，那么向上折叠,如果当前是折叠状态，那么向下展开
            float distance = mAutoScrollDistance;
            if (mState == CalendarState.OPEN) {
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
                    setState(CalendarState.OPEN);
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
        if (mState == CalendarState.OPEN) {
            if (mCurrentRowsIsSix && rows != 6) {
                //向上滑动
                AutoMoveAnimation animation = new AutoMoveAnimation(mDragView, -rowHegiht, 200, 1.5f);
                mDragView.startAnimation(animation);
            } else if (!mCurrentRowsIsSix && rows != 5) {
//            想下滑动
                AutoMoveAnimation animation = new AutoMoveAnimation(mDragView, rowHegiht, 200, 1.5f);
                mDragView.startAnimation(animation);
            }
        }
        mCurrentRowsIsSix = (rows == 6);
    }
}
