package com.example.mytestapplication.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mytestapplication.R;
import com.example.mytestapplication.widge.calendar.CalendarState;
import com.example.mytestapplication.widge.calendar.MyCalendaryView;

public class CalendarActivity extends AppCompatActivity {

    private Button close, open;
    private MyCalendaryView myCalendaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        myCalendaryView = (MyCalendaryView) findViewById(R.id.mycalendar);
        close = (Button) findViewById(R.id.bt_closed);
        open = (Button) findViewById(R.id.bt_open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendaryView.setState(CalendarState.OPEN);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendaryView.setState(CalendarState.CLOSE);
            }
        });
    }
}
