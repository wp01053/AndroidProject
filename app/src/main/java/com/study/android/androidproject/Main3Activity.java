package com.study.android.androidproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class Main3Activity extends AppCompatActivity {
    private static final String TAG = "lecture";

    Button btn_Show;
    Date selectedDate;
    TextView tvSelectedDate;

    TextView date;
    SharedPreferences.Editor editor2;
    String datee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        btn_Show = findViewById(R.id.btn_show);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        date = findViewById(R.id.date);

        btn_Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyGridViewCalendar myGridViewCalendar = new MyGridViewCalendar();
                myGridViewCalendar.setSelectedDate(new Date());
                myGridViewCalendar.show(getSupportFragmentManager(), "grid_view_calendar");

                SharedPreferences pref = getSharedPreferences("ID", Activity.MODE_PRIVATE);
                datee = pref.getString("datee", ""); //key, value(defaults)
                date = findViewById(R.id.date);
                Log.d(TAG , datee  +  " 값");
                date.setText("오늘의 출근 시간 " +"\n"+datee);
            }
        });

    }
}