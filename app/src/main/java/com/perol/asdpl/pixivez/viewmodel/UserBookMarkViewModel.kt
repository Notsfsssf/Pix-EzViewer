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
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.BookMarkTagsResponse
import com.perol.asdpl.pixivez.responses.Illust
import io.reactivex.Single

class UserBookMarkViewModel : BaseViewModel() {
    val retrofit = RetrofitRespository.getInstance()
    val data = MutableLiveData<List<Illust>>()
    val adddata = MutableLiveData<List<Illust>>()
    val nexturl = MutableLiveData<String>()
    val tags = MutableLiveData<BookMarkTagsResponse>()
    val preference = SharedPreferencesServices.getInstance()
    fun onLoadMoreListener() {
        if (nexturl.value != null)
            retrofit.getNextUserIllusts(nexturl.value!!).subscribe({
                adddata.value = it.illusts
                nexturl.value = it.next_url
            }, {}, {})
    }

    private fun isUser(id: Long) = Single.create<Boolean> { it ->
        launchUI {
            val pt = AppDataRepository.getUser()
            val isuser = id == pt.userid
            it.onSuccess(isuser)
        }
    }

    fun onRefreshListener(id: Long, string: String, tag: String?) {
        retrofit.getLikeIllust(id, string, tag).subscribe({
            data.value = it.illusts
            nexturl.value = it.next_url
        }, {}, {}).add()
    }

    fun first(id: Long, string: String): Single<Boolean> {
        retrofit.getLikeIllust(id, string, null).subscribe({
            data.value = it.illusts
            nexturl.value = it.next_url
        }, {}, {}).add()
        retrofit.getIllustBookmarkTags(id, string).subscribe({
            tags.value = it

        }, {}, {}).add()
        return isUser(id)
    }
}