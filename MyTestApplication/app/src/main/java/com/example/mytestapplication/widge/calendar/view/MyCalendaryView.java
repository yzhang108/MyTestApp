package com.example.mytestapplication.widge.calendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.widge.calendar.CalendaryUtil;
import com.example.mytestapplication.widge.calendar.adapter.BaseDayAdapter;
import com.example.mytestapplication.widge.calendar.adapter.MonthPageAdapter;
import com.example.mytestapplication.widge.calendar.adapter.WeekPageAdapter;
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
    private int mCurrMonthRows = 6;
    private int mLastMonthRows = 0;
    private int mNextMonthRows = 0;
    private int currentSelectDayRowNum = 1;
    private boolean mStartChangeMonth = false;
    private int startScrollMonthPosition = 0;
    private int currentMonthPosition = 0;

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
        LocalDate lastDate = initDate.minusMonths(1);
        LocalDate nextData = initDate.plusMonths(1);
        mLastMonthRows = CalendaryUtil.getMonthRows(lastDate.getYear(), lastDate.getMonthOfYear());
        mNextMonthRows = CalendaryUtil.getMonthRows(nextData.getYear(), nextData.getMonthOfYear());
        mCurrMonthRows = CalendaryUtil.getMonthRows(selectedDate.getYear(), selectedDate.getMonthOfYear());

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
                        return;
                    }
                    setSelectedDate(date);
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
                if (mStartChangeMonth) {
                    if (position == startScrollMonthPosition) {
                        //判断下个月
                        if (mNextMonthRows != mCurrMonthRows) {
                            float moveY = (mNextMonthRows - mCurrMonthRows) * rowHegiht * positionOffset;
                            mDragView.setY(compulateDragViewMaxTop() + moveY);
                        }
                    } else if (position < startScrollMonthPosition) {
                        //判断上个月
                        if (mCurrMonthRows != mLastMonthRows) {
                            float moveY = (mLastMonthRows - mCurrMonthRows) * rowHegiht * (1 - positionOffset);
                            mDragView.setY(compulateDragViewMaxTop() + moveY);
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentMonthPosition = position;
                monthPageAdapter.notifyCurrentItem(position);
                LocalDate date = monthPageAdapter.getShowDate(position);
                if (date != null) {
                    if (date.isEqual(selectedDate)) {
                        return;
                    }
                    setSelectedDate(date);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1:
                        //开始滑动
                        startScrollMonthPosition = monthView.getCurrentItem();
                        initMonthRows();
                        if (mCurrMonthRows == mLastMonthRows && mCurrMonthRows == mNextMonthRows)
                            mStartChangeMonth = false;
                        mStartChangeMonth = true;
                        break;
                    case 2:
                        //切换页面了
                        if (!mStartChangeMonth) {
                            startScrollMonthPosition = currentMonthPosition;
                            mStartChangeMonth = true;
                        }
                        break;
                    case 0:
                        //滑动结束
                        if (mStartChangeMonth) {
                            if (monthView.getCurrentItem() != startScrollMonthPosition) {
                                //说明确实切换页面了，移动到最终位置
                                if (monthView.getCurrentItem() > startScrollMonthPosition) {
                                    float moveY = (mNextMonthRows - mCurrMonthRows) * rowHegiht * 1;
                                    mDragView.setY(compulateDragViewMaxTop() + moveY);
                                } else {
                                    float moveY = (mLastMonthRows - mCurrMonthRows) * rowHegiht * 1;
                                    mDragView.setY(compulateDragViewMaxTop() + moveY);
                                }
                            } else {
                                mDragView.setY(compulateDragViewMaxTop());
                            }
                        }
                        mStartChangeMonth = false;
                        initMonthRows();
                        break;
                }
            }
        });
        monthView.setCurrentItem(monthPageAdapter.getStartPos(), false);
    }

    private void initMonthRows() {
        LocalDate curDate = monthPageAdapter.getShowDate(monthView.getCurrentItem());
        LocalDate lastDate = monthPageAdapter.getShowDate(monthView.getCurrentItem() - 1);
        LocalDate nextDate = monthPageAdapter.getShowDate(monthView.getCurrentItem() + 1);
        mCurrMonthRows = CalendaryUtil.getMonthRows(curDate.getYear(), curDate.getMonthOfYear());
        mLastMonthRows = CalendaryUtil.getMonthRows(lastDate.getYear(), lastDate.getMonthOfYear());
        mNextMonthRows = CalendaryUtil.getMonthRows(nextDate.getYear(), nextDate.getMonthOfYear());
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case ACTION_DOWN:
                mDownPosition[0] = ev.getRawX();
                mDownPosition[1] = ev.getRawY();
                if (mState == CalendarState.WEEK && !isViewUnderPosition(mDragView, mDownPosition[0], mDownPosition[1])) {
                    return false;
                } else if (mState == CalendarState.MONTH) {
                    if (!(isViewUnderPosition(monthView, mDownPosition[0], mDownPosition[1]) || isViewUnderPosition(mDragView, mDownPosition[0], mDownPosition[1])))
                        return false;
                }
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
                        initMonthRows();
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
        if (mState == CalendarState.WEEK && canChildScrollUp(mDragView))
            return false;
        switch (ev.getActionMasked()) {
            case ACTION_MOVE:
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

    private static boolean canChildScrollUp(View view) {
        // 如果当前版本小于 14，那就得自己背锅
        if (android.os.Build.VERSION.SDK_INT < 14) {
            // 这里给出了如果当前 view 是 AbsListView 的实例的检测方法
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return view.getScrollY() > 0;
            }
        } else {
            return view.canScrollVertically(-1);
        }
    }

    public void dragView(float distance) {
        if (distance < 0) {
            drawUp(distance);
        } else if (distance > 0) {
            drawDown(distance);
        }
    }

    private void drawUp(float distanceY) {
        float dragViewScrollY = Math.min(Math.abs(distanceY), mAutoScrollDistance);
        float canDragViewScrollY = getDragViewCanUpScope();
        float dragViewTransY = mDragView.getY() - Math.min(dragViewScrollY, canDragViewScrollY);
        mDragView.setY(dragViewTransY);

        float scrollY = dragViewScrollY / (float) (mCurrMonthRows - 1);
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

        float scrollY = dragviewScrollY / (float) (mCurrMonthRows - 1);
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
        return mCalendarView.getTop() + rowHegiht * mCurrMonthRows + (isLegendVisible ? legendView.getHeight() : 0);
    }


    private int compulateLegendViewTop() {
        return mCalendarView.getTop() + rowHegiht * mCurrMonthRows;
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

    private boolean isViewUnderPosition(View view, float x, float y) {
//        AppLogger.e("x=" + x + ",y=" + y);
        if (view == null) return false;
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
//        AppLogger.e("viewLocation.x=" + viewLocation[0] + ",viewLocation.y=" + viewLocation[1]);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
//        AppLogger.e("parentLocation.x=" + parentLocation[0] + ",parentLocation.y=" + parentLocation[1]);
        float screenX = parentLocation[0] + x;
        float screenY = parentLocation[1] + y;
//        AppLogger.e("screenX=" + screenX + ",screenY=" + screenY);
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
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
