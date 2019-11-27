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

package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.Illust

class RankingMViewModel : BaseViewModel() {
    val retrofitRespository = RetrofitRespository.getInstance()
    val nexturl = MutableLiveData<String>()
    val addillusts = MutableLiveData<ArrayList<Illust>>()
    val illusts = MutableLiveData<ArrayList<Illust>>()
    val bookmarknum = MutableLiveData<Illust>()
    fun first(mode: String, picdata: String?) {
        retrofitRespository.getIllustRanking(mode, picdata).subscribe({
            nexturl.value = it.next_url
            illusts.value = ArrayList<Illust>(it.illusts)
        }, { it.printStackTrace() }, {}).add()
    }

    fun OnRefresh(mode: String, picdata: String?) {
        retrofitRespository.getIllustRanking(mode, picdata).subscribe({
            nexturl.value = it.next_url
            illusts.value = it.illusts as ArrayList<Illust>?
        }, {}, {}).add()
    }

    fun OnLoadMore() {
        retrofitRespository.getNext(nexturl.value!!).subscribe({
            nexturl.value = it.next_url
            addillusts.value = it.illusts as ArrayList<Illust>?
        }, {}, {}).add()
    }

    fun OnItemChildLongClick(id: Illust) {
        if (id.is_bookmarked) {
            retrofitRespository.postUnlikeIllust(id.id)!!.subscribe({
                bookmarknum.value = id
            }, {}, {}).add()
        } else {
            retrofitRespository.postLikeIllust(id.id)!!.subscribe({
                bookmarknum.value = id
            }, {}, {}).add()
        }
    }

    fun datapick(mode: String, picdata: String?) {
        retrofitRespository.getIllustRanking(mode, picdata).subscribe({
            nexturl.value = it.next_url
            illusts.value = ArrayList<Illust>(it.illusts)
        }, {}, {}).add()
    }
}