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

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.perol.asdpl.pixivez.viewmodel.BlockViewModel
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


abstract class BaseFragment : LazyFragment() {
    var isR18on = false

    var blockTags = emptyList<String>()

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AdapterRefreshEvent) {

    }

    lateinit var blockViewModel: BlockViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        isR18on = PreferenceManager.getDefaultSharedPreferences(requireActivity())
            .getBoolean("r18on", false)
        blockViewModel = ViewModelProvider(requireActivity()).get(BlockViewModel::class.java)
        try {
            runBlocking {
                val result = blockViewModel.getAllTags()
                blockTags = result.map {
                    it.name
                }
                if (blockTags.isNullOrEmpty()) blockTags = emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}

