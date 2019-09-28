package com.perol.asdpl.pixivez.activity

import android.content.Context
import android.content.res.Resources.Theme
import androidx.appcompat.app.AppCompatActivity
import com.perol.asdpl.pixivez.objects.MyContextWrapper
import com.perol.asdpl.pixivez.services.PxEZApp
import io.multimoon.colorful.Colorful
import java.util.*


abstract class RinkActivity : AppCompatActivity() {
//    override fun getTheme(): Theme {
//
//        val theme = super.getTheme()
//
//        theme.applyStyle(Colorful().getCustomTheme(), true)
//
//        return super.getTheme()
//    }
    override fun attachBaseContext(newBase: Context?) {
        val locale = when (PxEZApp.language) {
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
