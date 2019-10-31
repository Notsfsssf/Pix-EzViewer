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

package com.perol.asdpl.pixivez.objects

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.perol.asdpl.pixivez.R

class ThemeUtil {
    companion object {
        val themeArray = arrayOf(
                R.style.AppThemeBase_Primary,
                R.style.AppThemeBase_blue,
                R.style.AppThemeBase_pink,
                R.style.AppThemeBase_miku,
                R.style.AppThemeBase_purple,
                R.style.AppThemeBase_cyan,
                R.style.AppThemeBase_green,
                R.style.AppThemeBase_indigo,
                R.style.AppThemeBase_red,
                R.style.AppThemeBase_now
        )

        @JvmStatic
        fun themeInit(activity: AppCompatActivity) {
            activity.apply {
                var intColor = PreferenceManager.getDefaultSharedPreferences(activity).getInt("colorint", 0)
                if (intColor >= themeArray.size)
                    intColor = 0
                setTheme(themeArray[intColor])

            }
//            Colorful().apply(activity)
            //  activity.window.statusBarColor = Colorful().getPrimaryColor().getColorPack().normal().asInt()
        }
    }
}
