package com.example.mytestapplication.widge.calendar;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 张艳 on 2017/5/8.
 */

public enum CalendaryUtil {
    INSTANCE;
    public static final int ONE_WEEK_DAYS = 7;

    public static List<DateShowData> getTitleWeekShow(String[] datas) {
        if (datas == null || datas.length != ONE_WEEK_DAYS)
            return null;
        List<DateShowData> dateShowDatas = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            dateShowDatas.add(new DateShowData(datas[i]));
        }
        return dateShowDatas;
    }

    public static List<DateShowData> getMonthShow(int year, int month) {
        List<DateShowData> dateShowDatas = new ArrayList<>();
        int monthRows = getMonthRows(year, month);//一共几行
        int firstDayPos = getFirstDayWeek(year, month);//第一天在周几
        LocalDate dateTime = new LocalDate(year, month, 1);
        //得到第一天
        dateTime = dateTime.plusDays(1 - firstDayPos);
        for (int i = 0; i < monthRows * ONE_WEEK_DAYS; i++) {
//            String showTime=dateTime.toString("yyyy年MM月dd日", Locale.CHINESE);
//            AppLogger.e("showTime=="+showTime);
            DateShowData dateShowData = new DateShowData();
            dateShowData.date = dateTime;

            dateShowData.text = String.valueOf(dateTime.getDayOfMonth());
            dateShowDatas.add(i, dateShowData);
            dateTime = dateTime.plusDays(1);
        }
        return dateShowDatas;
    }

    public static LocalDate getMonth(LocalDate startDate, int monthDiff) {
        LocalDate desDate = startDate.plusMonths(monthDiff);
        return desDate;
    }

    public static LocalDate getWeek(LocalDate startDate, int weekDiff) {
        LocalDate desDate = startDate.plusWeeks(weekDiff);
        return desDate;
    }

    public static List<DateShowData> getWeekShow(LocalDate localDate) {
        List<DateShowData> dateShowDatas = new ArrayList<>();
        localDate = getFirstDayOfWeek(localDate);
        for (int i = 0; i < 7; i++) {
            DateShowData dateShowData = new DateShowData();
            dateShowData.date = localDate;
            dateShowData.text = localDate.toString("dd");
            dateShowDatas.add(dateShowData);
            localDate = localDate.plusDays(1);
        }
        return dateShowDatas;
    }

    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 返回当前月份1号位于周几
     *
     * @param year  年份
     * @param month 月份，传入系统获取的，不需要正常的
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 获得两个日期距离几周
     *
     * @return
     */
    public static int getWeeksAgo(LocalDate lastDay, LocalDate newDay) {
//        AppLogger.e("lastday="+lastDay.toString("yyyy-MM-dd")+",newDay="+newDay.toString("yyyy-MM-dd"));
        LocalDate fromDay = getFirstDayOfWeek(lastDay);
        LocalDate toDay = getFirstDayOfWeek(newDay);
//        AppLogger.e("fromDay="+fromDay.toString("yyyy-MM-dd")+",toDay="+toDay.toString("yyyy-MM-dd"));
        Period p = new Period(fromDay, toDay, PeriodType.days());
        int days = p.getDays();
//        AppLogger.e("days="+days);
        return days / 7;
    }

    public static LocalDate getFirstDayOfWeek(LocalDate localDate) {
        int dayOfWeek = localDate.getDayOfWeek() + 1;
        if (dayOfWeek == 8) {
            dayOfWeek = 1;
        }
        return localDate.plusDays(1 - dayOfWeek);
    }

//    /**
//     * 获得两个日期距离几个月
//     *
//     * @return
//     */
//    public static int getMonthsAgo(int lastYear, int lastMonth, int year, int month) {
//        return (year - lastYear) * 12 + (month - lastMonth);
//    }
    /**
     * 获得两个日期距离几个月
     *
     * @return
     */
    public static int getMonthsAgo(LocalDate fromDate, LocalDate toDate) {
        return (toDate.getYear() - fromDate.getYear()) * 12 + (toDate.getMonthOfYear() - fromDate.getMonthOfYear());
    }

    public static int getWeekRow(int year, int month, int day) {
        int week = getFirstDayWeek(year, month);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        int lastWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (lastWeek == 7)
            day--;
        return (day + week - 1) / 7;
    }

    public static int getMonthRows(int year, int month) {
        int size = getFirstDayWeek(year, month) + getMonthDays(year, month) - 1;
        return size % 7 == 0 ? size / 7 : (size / 7) + 1;
    }
}
