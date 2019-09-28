package com.perol.asdpl.pixivez.objects

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.perol.asdpl.pixivez.R

class TToast {
    companion object {
        fun retoken(context: Context) {
            val toast = Toast.makeText(context, "", Toast.LENGTH_LONG)
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.toast_retoken, null)
            toast.view = view
            toast.show()
        }
        fun startDownload(context: Context) {
            val toast = Toast.makeText(context, "", Toast.LENGTH_LONG)
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.toast_startdownload, null)
            toast.view = view
            toast.setGravity(Gravity.BOTTOM,0,0)
            toast.show()
        }
    }

}
