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

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.services.PxEZApp

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
            val toast = Toast.makeText(context, PxEZApp.instance.getString(R.string.join_download_queue), Toast.LENGTH_SHORT)
//            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val view = inflater.inflate(R.layout.toast_startdownload, null)
//            toast.view = view
//            toast.setGravity(Gravity.BOTTOM,0,0)
            toast.show()
        }
    }

}
