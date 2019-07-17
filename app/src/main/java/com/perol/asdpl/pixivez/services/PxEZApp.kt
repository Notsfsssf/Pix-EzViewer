package com.perol.asdpl.pixivez.services

import android.app.Application
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.os.BuildCompat
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import io.multimoon.colorful.*
import java.io.File


/**
 * Created by asdpl on 2018/2/24.
 */

class PxEZApp : Application() {
    //    override fun attachBaseContext(base: Context?) {
//        val pref= SharedPreferencesServices(base)
//        val locale=  when(pref.getInt("language")){
//            1->{
//                Locale.ENGLISH
//            }
//            2->{
//                Locale.TRADITIONAL_CHINESE
//            }
//            else->{
//                Locale.SIMPLIFIED_CHINESE
//            }
//        }
//        println(locale.language)
//        val context = MyContextWrapper.wrap(base, locale)
//        super.attachBaseContext(context)
//    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        val sharedPreferencesServices = SharedPreferencesServices(this)
        isR18On = sharedPreferencesServices.getBoolean("r18on")
        disableProxy = sharedPreferencesServices.getBoolean("disableproxy")
        storepath = if (sharedPreferencesServices.getString("storepath") != null)
            sharedPreferencesServices.getString("storepath")
        else
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "PxEz"
        saveformat = sharedPreferencesServices.getInt("saveformat")
        isaddflag = sharedPreferencesServices.getBoolean("isaddflag")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = resources.configuration.locales.get(0).language;
        } else {
            locale = resources.configuration.locale.language;
        }
        Log.d("locale", locale)
        val list = ArrayList<ThemeColorInterface>().also {
            val myCustomColor1 = CustomThemeColor(
                    this,
                    R.style.bili_primary_color,
                    R.style.bili_primary_dark_color,
                    R.color.pink, // <= use the color you defined in my_custom_primary_color
                    R.color.pink // <= use the color you defined in my_custom_primary_dark_color
            )
            val myCustomColor2 = CustomThemeColor(
                    this,
                    R.style.blue_primary_color,
                    R.style.blue_primary_dark_color,
                    R.color.blue, // <= use the color you defined in my_custom_primary_color
                    R.color.md_blue_400 // <= use the color you defined in my_custom_primary_dark_color
            )
            it+=ThemeColor.BLUE
            it+=ThemeColor.AMBER
            it+=ThemeColor.GREEN
            it+=ThemeColor.PINK
            it+=ThemeColor.PURPLE
            it+=ThemeColor.BLUE_GREY
            it+=ThemeColor.ORANGE
            it+=ThemeColor.RED
            it+=ThemeColor.TEAL
            it+=ThemeColor.LIGHT_BLUE
            it+=ThemeColor.LIGHT_GREEN
            it += myCustomColor1
            it += myCustomColor2
        }
        var colorNum=0
        if (sharedPreferencesServices.getInt("colornum").apply { colorNum=this }>(list.size-1)){
            sharedPreferencesServices.setInt("colornum",0)
        }
        val defaults: Defaults = Defaults(
                primaryColor = list[colorNum],
                accentColor = ThemeColor.PINK,
                useDarkTheme = false,
                translucent = true)
        initColorful(this, defaults)
        if (!disableProxy) {
            if (!BuildCompat.isAtLeastQ()) {
                try {

                } catch (e: Exception) {

                }
            } else {
                Toast.makeText(this, "Q", Toast.LENGTH_SHORT).show()
            }

        }

    }

    companion object {
        @JvmStatic
        var storepath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "PxEz"
        @JvmStatic
        var isR18On = true
        @JvmStatic
        var saveformat = 0
        @JvmStatic
        var isaddflag = false
        @JvmStatic
        var locale = "zh"
        @JvmStatic
        var disableProxy = false
        lateinit var instance: PxEZApp

    }


}
