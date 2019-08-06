package com.perol.asdpl.pixivez.services

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Looper
import android.widget.Toast
import com.perol.asdpl.pixivez.objects.Toasty
import kotlin.concurrent.thread

class CrashHandler(private var mContext: Context) : Thread.UncaughtExceptionHandler {
    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(p0: Thread, p1: Throwable) {

        thread {
            p1.printStackTrace()
            Looper.prepare()
            if (p1 is OutOfMemoryError) {
                Toasty.normal(mContext, "发生内存溢出错误", Toast.LENGTH_LONG).show()
            } else {
                Toasty.normal(mContext, p1.localizedMessage!!, Toast.LENGTH_LONG).show()
                AlertDialog.Builder(mContext).setTitle("发生错误")
                        .setMessage("重启应用或清除数据,将错误信息详细描述发至反馈邮箱\nRestart the app or clear the data.Send a detailed description of this error message to the feedback mailbox\n${p1.localizedMessage}")
                        .setNeutralButton("OK") { _, _ ->

                        }.create().show()
            }
            Looper.loop()
        }.start()
    }
}