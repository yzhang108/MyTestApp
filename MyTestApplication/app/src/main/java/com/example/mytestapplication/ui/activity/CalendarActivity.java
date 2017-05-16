package com.example.mytestapplication.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mytestapplication.R;
import com.example.mytestapplication.adapter.TestAdapter;
import com.example.mytestapplication.widge.calendar.entity.CalendarState;
import com.example.mytestapplication.widge.calendar.view.MyCalendaryView;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    //    private Button close, open;
    private MyCalendaryView myCalendaryView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        myCalendaryView = (MyCalendaryView) findViewById(R.id.mycalendar);
        myCalendaryView.setViewState(CalendarState.MONTH);
        myCalendaryView.setLegendVisible(false);

        recyclerView = (RecyclerView) findViewById(R.id.rl_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        List<String> showData=new ArrayList<>();
        for(int i=0;i<100;i++){
            showData.add("DATA  "+i);
        }
        TestAdapter testAdapter=new TestAdapter(showData,this);
        recyclerView.setAdapter(testAdapter);
    }
}
