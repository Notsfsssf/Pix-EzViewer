/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.networks;

import android.content.Context;
import android.content.SharedPreferences;

import com.perol.asdpl.pixivez.services.PxEZApp;


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
