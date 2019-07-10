package com.perol.asdpl.pixivez.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.MyContextWrapper
import java.util.*


abstract class RinkActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        val pref = SharedPreferencesServices.getInstance()
        val locale = when (pref.getInt("language")) {
            1 -> {
                Locale.ENGLISH
            }
            2 -> {
                Locale.TRADITIONAL_CHINESE
            }
            else -> {
                Locale.SIMPLIFIED_CHINESE
            }
        }
        val context = MyContextWrapper.wrap(newBase, locale)
        super.attachBaseContext(context)
    }
}
