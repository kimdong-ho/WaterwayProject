package com.example.waterway;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterway.ui.apicall.GetThingShadow;
import com.example.waterway.ui.apicall.UpdateShadow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class NowActivity extends AppCompatActivity {
    final static String TAG = "WaterwayProject";

    String urlStr1;
    Timer timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now);
        urlStr1 = "https://9avyoiat5g.execute-api.ap-northeast-2.amazonaws.com/prod/devices/WaterwayProject";

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new GetThingShadow(NowActivity.this, urlStr1).execute();
            }
        },0,2000);

    Button onBtn = findViewById(R.id.onBtn); // LED 버튼 제어 함수
    onBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONObject payload = new JSONObject();

            try {
                JSONArray jsonArray = new JSONArray();
                String led_input = "ON";
                if (led_input != null && led_input.equals("")) {
                    JSONObject tag2 = new JSONObject();
                    tag2.put("tagName", "LED");
                    tag2.put("tagValue", led_input);
                    jsonArray.put(tag2);
                }

                if (jsonArray.length() > 0)
                    payload.put("tags", jsonArray);
            } catch (JSONException e) {
                Log.e(TAG, "JSONEXception");
            }
            Log.i(TAG,"payload="+payload);
            if (payload.length() >0 )
                new UpdateShadow(NowActivity.this,urlStr1).execute(payload);
            else
                Toast.makeText(NowActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
    });

        Button offBtn = findViewById(R.id.offBtn); // LED 버튼 제어 함수
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject payload = new JSONObject();

                try {
                    JSONArray jsonArray = new JSONArray();
                    String led_input = "OFF";
                    if (led_input != null && led_input.equals("")) {
                        JSONObject tag2 = new JSONObject();
                        tag2.put("tagName", "LED");
                        tag2.put("tagValue", led_input);
                        jsonArray.put(tag2);
                    }

                    if (jsonArray.length() > 0)
                        payload.put("tags", jsonArray);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONEXception");
                }
                Log.i(TAG,"payload="+payload);
                if (payload.length() >0 )
                    new UpdateShadow(NowActivity.this,urlStr1).execute(payload);
                else
                    Toast.makeText(NowActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });

        Button onMot = findViewById(R.id.onMot); // 모터 버튼 제어 함수
        onMot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject payload = new JSONObject();

                try {
                    JSONArray jsonArray = new JSONArray();
                    String motor_input = "ON";
                    if (motor_input != null && motor_input.equals("")) {
                        JSONObject tag2 = new JSONObject();
                        tag2.put("tagName", "Motor");
                        tag2.put("tagValue", motor_input);
                        jsonArray.put(tag2);
                    }

                    if (jsonArray.length() > 0)
                        payload.put("tags", jsonArray);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONEXception");
                }
                Log.i(TAG,"payload="+payload);
                if (payload.length() >0 )
                    new UpdateShadow(NowActivity.this,urlStr1).execute(payload);
                else
                    Toast.makeText(NowActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });

        Button offMot = findViewById(R.id.offMot); // 모터 버튼 제어 함수
        offMot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject payload = new JSONObject();

                try {
                    JSONArray jsonArray = new JSONArray();
                    String motor_input = "OFF";
                    if (motor_input != null && motor_input.equals("")) {
                        JSONObject tag2 = new JSONObject();
                        tag2.put("tagName", "Motor");
                        tag2.put("tagValue", motor_input);
                        jsonArray.put(tag2);
                    }

                    if (jsonArray.length() > 0)
                        payload.put("tags", jsonArray);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONEXception");
                }
                Log.i(TAG,"payload="+payload);
                if (payload.length() >0 )
                    new UpdateShadow(NowActivity.this,urlStr1).execute(payload);
                else
                    Toast.makeText(NowActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
