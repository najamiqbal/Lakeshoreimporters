package com.dleague.lakeshoreimporters.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USER = "Orders";
    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
    public boolean addDaysToPref(ArrayList<NotificationModel> daysModel) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(daysModel);
        editor.putString(KEY_USER, json);
        editor.apply();
        // savePersonId(studentModel.getPerson_id());
        return true;
    }

    public List<NotificationModel> getdays() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        List<NotificationModel> productFromShared = new ArrayList<>();
        String json = sharedPreferences.getString(KEY_USER, "");
        Type type = new TypeToken<List<NotificationModel>>() {}.getType();
        productFromShared = gson.fromJson(json, type);
        // DaysModel obj = gson.fromJson(json, DaysModel.class);
        return productFromShared;
    }
    public boolean RemoveDays() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER);
        editor.apply();
        return true;
    }


}
