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
import android.graphics.*
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*

//Pixiv动图的帧动画解决方案，如果需要拿去用把Copyright带上或者提一下我的id吧，研究了挺久的
class AnimationView : SurfaceView, SurfaceHolder.Callback, Runnable {

    private val drawPool: MutableList<String> =
        ArrayList()
    private val handlerThread = HandlerThread("UgoiraView")
    private var needToDraw = false
    private var isSurfaceCreated = false

    private var painterHandler: Handler? = null
    var delayTime: Long = 50

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    private fun init() {
        holder?.addCallback(this)
        holder?.setFormat(PixelFormat.TRANSLUCENT)
        handlerThread.start()
    }

    fun pausePlay() {
        needToDraw = false
        painterHandler?.removeCallbacks(this)
        painterHandler = null
    }

    fun startPlay() {
        if (drawPool.isEmpty()) return
        needToDraw = true
        painterHandler = Handler(handlerThread.looper)
        painterHandler?.post(this)
    }

    fun startAnimation(
        pathListRGB: List<String>
    ) {
        drawPool.clear()
        drawPool.addAll(pathListRGB)
        painterHandler = Handler(handlerThread.looper)
        painterHandler?.post(this)
    }


    lateinit var onStart: () -> Unit
    lateinit var onEnd: () -> Unit
    fun onStartListener(listener: () -> Unit) {
        this.onStart = listener
    }

    fun onEndListener(listener: () -> Unit) {
        this.onEnd = listener
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        isSurfaceCreated = true
        needToDraw = true

    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        needToDraw = false
    }

    fun setPreviewImage(bitmap: Bitmap) {
        val canvas = holder.lockCanvas()
        if (canvas != null) {
            val targetBitmap = bitmap
            targetBitmap?.let {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                val react = RectF(
                    this@AnimationView.left.toFloat(),
                    this@AnimationView.top.toFloat(),
                    this@AnimationView.width.toFloat(),
                    this@AnimationView.height.toFloat()
                )
                canvas.drawBitmap(it, null, react, null)
            }
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun run() {
        post { onStart() }
        var i = 0
        while (true) {
            if (needToDraw) {
                try {
                    val preTime = System.currentTimeMillis()
                    draw(drawPool[i])
                    val duration = System.currentTimeMillis() - preTime
                    i++
                    if (i == drawPool.count()) i = 0
                    Thread.sleep(
                        Math.max(
                            0,
                            delayTime - duration
                        )
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                break
            }
        }

        post {
            onEnd()
        }
    }

    private fun draw(path: String) {
        val canvas = holder.lockCanvas()
        if (canvas != null) {
            val targetBitmap = BitmapFactory.decodeFile(path)
            targetBitmap?.let {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                val react = RectF(
                    this@AnimationView.left.toFloat(),
                    this@AnimationView.top.toFloat(),
                    this@AnimationView.width.toFloat(),
                    this@AnimationView.height.toFloat()
                )
                canvas.drawBitmap(it, null, react, null)
            }
            holder.unlockCanvasAndPost(canvas)
        }
    }

}