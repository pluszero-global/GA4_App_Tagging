package com.pluszero.liveinsight

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import com.google.firebase.analytics.FirebaseAnalytics
import org.json.JSONArray
import org.json.JSONObject

class AnalyticsWebInterface(context: Context?) {
    private val mAnalytics: FirebaseAnalytics
    @JavascriptInterface
    fun logEvent(name: String, jsonParams: String) {
        if(!jsonParams.contains("gtm.")) {
            mAnalytics.logEvent(name, bundleFromJson(jsonParams))
        }
    }

    @JavascriptInterface
    fun setUserProperty(jsonParams: String) {
        LOGD("setParam:$jsonParams")
        val jsonObject = JSONObject(jsonParams)
        val iterator: Iterator<String> = jsonObject.keys()
        while(iterator.hasNext()) {
            val key: String = iterator.next()
            val value: Any = jsonObject.get(key)

            if(key == "user_id"){
                mAnalytics.setUserId(value.toString())
            } else {
                mAnalytics.setUserProperty(key, value.toString())
            }
        }
    }

    private fun LOGD(message: String) {
        // Only log on debug builds, for privacy
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }

    private fun arrayListFromJsonArray(json: String): Bundle {
        val jsonArray = JSONArray(json)
        val arrayList:ArrayList<String> = ArrayList()
        val bundle:Bundle = Bundle()

        for(i in 0 until jsonArray.length()){
            bundle.putAll(bundleFromJson(jsonArray[i].toString()))
        }

        return bundle
    }

    private fun bundleFromJson(json: String): Bundle {
        val jsonObject = JSONObject(json)
        val bundle: Bundle = Bundle()

        val iterator: Iterator<String> = jsonObject.keys()
        while(iterator.hasNext()){
            val key: String = iterator.next()
            val value: Any = jsonObject.get(key)
            when (value::class.simpleName) {
                "String" -> bundle.putString(key, value as String)
                "Int" -> bundle.putInt(key, value as Int)
                "Long" -> bundle.putLong(key, value as Long)
                "Boolean" -> bundle.putBoolean(key, value as Boolean)
                "Float" -> bundle.putFloat(key, value as Float)
                "Double" -> bundle.putDouble(key, value as Double)
                "JSONObject" -> bundle.putBundle(key, bundleFromJson(value.toString()) as Bundle)
                "JSONArray" -> bundle.putBundle(key, arrayListFromJsonArray(value.toString()) as Bundle)
                else -> bundle.putString(key, value::class.simpleName)
            }
        }
        return bundle
    }

    companion object {
        const val TAG = "AnalyticsWebInterface"
    }

    init {
        mAnalytics = FirebaseAnalytics.getInstance(context!!)

    }
}
