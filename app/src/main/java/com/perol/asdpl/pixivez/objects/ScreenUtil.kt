package com.perol.asdpl.pixivez.objects

import android.content.res.Resources

class ScreenUtil {
    companion object {

        @JvmStatic
        fun dip2px(dpValue: Int): Int {
            return (0.5f + dpValue * Resources.getSystem()
                .displayMetrics.density).toInt()
        }

        @JvmStatic
        fun px2dip(pxValue: Int): Float {
            return pxValue / Resources.getSystem().displayMetrics.density
        }
    }
}