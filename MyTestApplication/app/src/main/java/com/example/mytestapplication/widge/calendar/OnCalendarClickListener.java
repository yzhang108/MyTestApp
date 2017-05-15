package com.example.mytestapplication.widge.calendar;

import org.joda.time.LocalDate;

/**
 * Created by 张艳 on 2017/5/12.
 */

public interface OnCalendarClickListener {
    void onDatePicked(LocalDate date);
}
