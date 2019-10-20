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
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.SearchUserResponse

class IllustratorViewModel : ViewModel() {
    val retrofitRespository = RetrofitRespository.getInstance()
    val userpreviews = MutableLiveData<ArrayList<SearchUserResponse.UserPreviewsBean>>()
    val adduserpreviews = MutableLiveData<ArrayList<SearchUserResponse.UserPreviewsBean>>()
    val nexturl = MutableLiveData<String>()
    val refreshcomplete = MutableLiveData<Boolean>()

    init {
        refreshcomplete.value = false
    }

    fun first(long: Long, restrict: String, param2: Boolean) {
        if (param2) {
            retrofitRespository.getUserFollowing(long, restrict).subscribe({
                userpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
                nexturl.value = it.next_url
            }, {}, {})
        } else {
            retrofitRespository.getUserFollower(long).subscribe({
                userpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
                nexturl.value = it.next_url
            }, {}, {})
        }
    }

    fun OnLoadMore(string: String) {
        retrofitRespository.getNextUser(string).subscribe({
            adduserpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
            nexturl.value = it.next_url
        }, {}, {})
    }

    fun OnRefresh(long: Long, restrict: String, param2: Boolean) {
        if (param2) {
            retrofitRespository.getUserFollowing(long, restrict).subscribe({
                userpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
                nexturl.value = it.next_url
                refreshcomplete.value = !refreshcomplete.value!!
            }, {}, {})
        } else {
            retrofitRespository.getUserFollower(long).subscribe({
                userpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
                nexturl.value = it.next_url
                refreshcomplete.value = !refreshcomplete.value!!
            }, {}, {})
        }
    }

}