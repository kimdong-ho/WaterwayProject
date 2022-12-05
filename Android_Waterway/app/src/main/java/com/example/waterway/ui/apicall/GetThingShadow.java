package com.example.waterway.ui.apicall;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.snowproject.httpconnection.GetRequest;
//import com.android.snowproject.R;

import com.example.waterway.R;
import com.example.waterway.httpconnection.GetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetThingShadow extends GetRequest {
    final static String TAG = "WaterwayProject";
    String urlStr;

    public GetThingShadow(Activity activity, String urlStr) {
        super(activity);
        this.urlStr = urlStr;
    }

    @Override
    protected void onPreExecute() {
        try {
            Log.e(TAG, urlStr);
            url = new URL(urlStr);

        } catch (MalformedURLException e) {
            Toast.makeText(activity,"URL is invalid:"+urlStr, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            activity.finish();
        }
    }

    @Override
    public void onPostExecute(String jsonString) {
        if (jsonString == null)
            return ;
        Map<String, String> state = getStateFromJSONString(jsonString);

        TextView reported_ledTV = activity.findViewById(R.id.reported_led);
        TextView reported_weight1TV = activity.findViewById(R.id.reported_weight1);
        TextView reported_weight2TV = activity.findViewById(R.id.reported_weight2);
        TextView reported_motorTV = activity.findViewById(R.id.reported_motor);
        reported_weight1TV.setText(state.get("reported_weight1"));
        reported_ledTV.setText(state.get("reported_LED"));
        reported_weight2TV.setText(state.get("reported_weight2"));
        reported_motorTV.setText(state.get("reported_motor"));
    }

    protected Map<String, String> getStateFromJSONString(String jsonString) {
        Map<String, String> output = new HashMap<>();
        try {
            // 처음 double-quote와 마지막 double-quote 제거
            jsonString = jsonString.substring(1,jsonString.length()-1);
            // \\\" 를 \"로 치환
            jsonString = jsonString.replace("\\\"","\"");
            Log.i(TAG, "jsonString="+jsonString);
            JSONObject root = new JSONObject(jsonString);
            JSONObject state = root.getJSONObject("state");
            JSONObject reported = state.getJSONObject("reported");
            String weight1Value = reported.getString("weight1");
            String weight2Value = reported.getString("weight2");
            String ledValue = reported.getString("LED");
            //String motorValue = reported.getString("Motor");
            output.put("reported_weight1", weight1Value);
            output.put("reported_weight2", weight2Value);
            output.put("reported_LED",ledValue);
            //output.put("reported_motor",motorValue);
        } catch (JSONException e) {
            Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        return output;
    }
}
