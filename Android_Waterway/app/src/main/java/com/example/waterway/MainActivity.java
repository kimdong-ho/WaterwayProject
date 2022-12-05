package com.example.waterway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    final static String TAG = "Waterwayproject";
    String urlStr1;
    String urlStr3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlStr1 = "https://9avyoiat5g.execute-api.ap-northeast-2.amazonaws.com/prod/devices/WaterwayProject";
        urlStr3 = "https://9avyoiat5g.execute-api.ap-northeast-2.amazonaws.com/prod/devices";

        // 좌측 버튼 클릭시 현재 상태 조회 액티비티로 전환
        Button nowBtn = findViewById(R.id.nowBtn);
        nowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), NowActivity.class);
                startActivity(intent1);
            }
        });

        // 우측 버튼 클릭시 로그 조회 액티비티로 전환
        Button rogBtn = findViewById(R.id.rogBtn);
        rogBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), LogActivity.class);
                startActivity(intent2);
            }
        });

        // 콤보 박스 생성
        final String [] dong = {"-성북구 동 선택 -", "성북동", "삼선동", "안암동"};
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dong);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        List<String> categories = new ArrayList<String>();
        categories.add("-성북구 동 선택-");
        categories.add("성북동");
        categories.add("삼선동");
        categories.add("안암동");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position , long id) {
                Toast.makeText(getApplicationContext(), categories.get(position) + "이 선택되었습니다.", Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });

    }
}