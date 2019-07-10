package com.perol.asdpl.pixivez.networks;

import android.content.Context;
import android.content.SharedPreferences;

import com.perol.asdpl.pixivez.services.PxEZApp;

/**
 * Created by asdpl on 2018/2/10.
 */

public class SharedPreferencesServices {

    private SharedPreferences sp;
    private String FILE_NAME = "userinfo";
    public SharedPreferencesServices(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }
    public SharedPreferencesServices() {
        sp = PxEZApp.instance.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

    }
    private static SharedPreferencesServices instance;//单例模式 双重检查锁定
    public static SharedPreferencesServices getInstance() {
        if (instance == null) {
            synchronized (SharedPreferencesServices.class) {
                if (instance == null) {
                    instance = new SharedPreferencesServices();
                }
            }
        }
        return instance;
    }


    public void setString(String key, String value) {
        sp.edit().putString(key, value).apply();

    }

    public String getString(String key) {
        return sp.getString(key, null);
    }

    public void setBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public void setInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return sp.getInt(key, 0);
    }
}
