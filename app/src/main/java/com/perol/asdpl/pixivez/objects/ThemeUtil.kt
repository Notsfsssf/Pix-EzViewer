package com.perol.asdpl.pixivez.objects

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
            activity.window.statusBarColor=Colorful().getPrimaryColor().getColorPack().normal().asInt()

        }
    }
}
