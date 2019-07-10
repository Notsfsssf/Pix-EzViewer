package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.BookMarkTagsResponse
import com.perol.asdpl.pixivez.responses.IllustsBean
import com.perol.asdpl.pixivez.responses.UserDetailResponse
import io.reactivex.Single

class UserBookMarkViewModel : ViewModel() {
    val retrofit = RetrofitRespository.getInstance()
    val data = MutableLiveData<List<IllustsBean>>()
    val adddata = MutableLiveData<List<IllustsBean>>()
    val nexturl = MutableLiveData<String>()
    val tags = MutableLiveData<BookMarkTagsResponse>()
    val preference = SharedPreferencesServices.getInstance()
    fun OnLoadMoreListener() {
        if (nexturl.value!=null)
        retrofit.getNextUserIllusts(nexturl.value!!).subscribe({
            adddata.value = it.illusts
            nexturl.value = it.next_url
        }, {}, {})
    }

    fun isUser(id: Long) = Single.create<Boolean> {
        val isuser = id == preference.getString("userid").toLong()
        it.onSuccess(isuser)
    }

    fun OnRefreshListener(id: Long, string: String,tag :String?) {
        retrofit.getLikeIllust(id, string,tag).subscribe({
            data.value = it.illusts
            nexturl.value = it.next_url
        }, {}, {})
    }

    fun first(id: Long, string: String): Single<Boolean> {

        retrofit.getLikeIllust(id, string,null).subscribe({
            data.value = it.illusts
            nexturl.value = it.next_url
        }, {}, {})
        retrofit.getIllustBookmarkTags(id, string).subscribe({
            tags.value = it

        }, {}, {})
        return isUser(id)
    }
}