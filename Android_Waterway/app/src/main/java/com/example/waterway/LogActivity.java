package com.example.waterway;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterway.ui.apicall.GetLog;

public class LogActivity extends AppCompatActivity {
    final static String TAG = "Waterwayproject";

    String urlStr1;
    String urlStr2;

    private TextView textView_Date1;
    private TextView textView_Date2;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log); // XML 연결

        urlStr1 = "https://9avyoiat5g.execute-api.ap-northeast-2.amazonaws.com/prod/devices/WaterwayProject";
        urlStr2 = "https://9avyoiat5g.execute-api.ap-northeast-2.amazonaws.com/prod/devices/WaterwayProject/log";

        Button startDateBtn = findViewById(R.id.start_date_button); // 버튼 클릭 리스너
        startDateBtn.setOnClickListener(view -> {
            callbackMethod = new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    textView_Date1 = (TextView)findViewById(R.id.textView_date1);
                    textView_Date1.setText(String.format("%d-%d-%d ", year ,monthOfYear+1,dayOfMonth));
                }
            };

            DatePickerDialog dialog = new DatePickerDialog(LogActivity.this, callbackMethod, 2022, 11, 0);

            dialog.show();
        });

        Button startTimeBtn = findViewById(R.id.start_time_button); // 버튼 클릭 리스너
        startTimeBtn.setOnClickListener(view -> {

            TimePickerDialog.OnTimeSetListener timeSetListener = (view13, hourOfDay, minute) -> {
                TextView textView_Time1 = (TextView)findViewById(R.id.textView_time1);
                textView_Time1.setText(String.format("%d:%d", hourOfDay, minute));
            };

            TimePickerDialog dialog = new TimePickerDialog(LogActivity.this, timeSetListener, 0, 0, false);
            dialog.show();

        });

        Button endDateBtn = findViewById(R.id.end_date_button); // 버튼 클릭 리스너
        endDateBtn.setOnClickListener(view -> {
            callbackMethod = (view12, year, monthOfYear, dayOfMonth) -> {
                textView_Date2 = (TextView)findViewById(R.id.textView_date2);
                textView_Date2.setText(String.format("%d-%d-%d ", year ,monthOfYear+1,dayOfMonth));
            };

            DatePickerDialog dialog = new DatePickerDialog(LogActivity.this, callbackMethod, 2022, 11, 0);

            dialog.show();

        });

        Button endTimeBtn = findViewById(R.id.end_time_button); // 버튼 클릭 리스너
        endTimeBtn.setOnClickListener(view -> {

            TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                TextView textView_Time2 = (TextView)findViewById(R.id.textView_time2);
                textView_Time2.setText(String.format("%d:%d", hourOfDay, minute));
            };

            TimePickerDialog dialog = new TimePickerDialog(LogActivity.this, timeSetListener, 0, 0, false);
            dialog.show();
        });

        Button start = findViewById(R.id.log_start_button); // 버튼 클릭 리스너
        start.setOnClickListener(view -> new GetLog(LogActivity.this,urlStr2).execute());
    }
}
