package com.pluszero.javatest;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class AnalyticsWebInterface {

    public static final String TAG = "AnalyticsWebInterface";
    private FirebaseAnalytics mAnalytics;

    public AnalyticsWebInterface(Context context) {
        mAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @JavascriptInterface
    public void logEvent(String name, String jsonParams) {

        mAnalytics.logEvent(name, bundleFromJson(jsonParams));
    }

    @JavascriptInterface
    public void setUserProperty(String jsonParams) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonParams);

        Iterator<String> keys = jsonObject.keys();

        while(keys.hasNext()){
            String key = keys.next();
            if(key == "user_id"){
                mAnalytics.setUserId(jsonObject.get(key).toString());
            }
            else{
                mAnalytics.setUserProperty(key, jsonObject.get(key).toString());
            }
        }
    }

    private void LOGD(String message) {
        // Only log on debug builds, for privacy
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }
    private Bundle arrayListFromJsonArray(String json) throws JSONException {
        //ArrayList<String> arrayList = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(json);
        Bundle bundle = new Bundle();
        if(jsonArray != null){
            for(int i=0; i<jsonArray.length(); i++){
                //arrayList.add(jsonArray.getString(i));
                bundle.putAll(bundleFromJson(jsonArray.getString(i)));
            }
        }
        return bundle;
    }
    private Bundle bundleFromJson(String json) {
        // [START_EXCLUDE]
        if (TextUtils.isEmpty(json)) {
            return new Bundle();
        }

        Bundle result = new Bundle();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);

                if (value instanceof String) {
                    result.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    result.putInt(key, (Integer) value);
                } else if (value instanceof Double) {
                    result.putDouble(key, (Double) value);
                } else if (value instanceof Long){
                    result.putLong(key, (Long) value);
                }else if (value instanceof Boolean){
                    result.putBoolean(key, (Boolean) value);
                }else if (value instanceof Float){
                    result.putFloat(key, (Float) value);
                }else if (value instanceof Float){
                    result.putFloat(key, (Float) value);
                }else if (value instanceof JSONObject){
                    result.putBundle(key, bundleFromJson(value.toString()));
                }else if (value instanceof JSONArray){
                    result.putBundle(key, arrayListFromJsonArray(value.toString()));
                }

            }

        } catch (JSONException e) {
            Log.w(TAG, "Failed to parse JSON, returning empty Bundle.", e);
            return new Bundle();
        }

        return result;
        // [END_EXCLUDE]
    }

}


