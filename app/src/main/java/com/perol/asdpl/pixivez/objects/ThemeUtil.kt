package com.perol.asdpl.pixivez.objects

import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import io.multimoon.colorful.Colorful
import io.multimoon.colorful.CustomThemeColor

class ThemeUtil {
    companion object {
        @JvmStatic
        fun Themeinit(activity: AppCompatActivity) {
           Colorful().apply(activity,override =true)
//            activity.apply {
//                window.statusBarColor = Color.TRANSPARENT;
//                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            }
     activity.window.statusBarColor=Colorful().getPrimaryColor().getColorPack().normal().asInt()

        }
    }
}
