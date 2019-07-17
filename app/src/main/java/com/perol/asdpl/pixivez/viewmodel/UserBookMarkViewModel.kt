package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.BookMarkTagsResponse
import com.perol.asdpl.pixivez.responses.IllustsBean
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserBookMarkViewModel : BaseViewModel() {
    val retrofit = RetrofitRespository.getInstance()
    val data = MutableLiveData<List<IllustsBean>>()
    val adddata = MutableLiveData<List<IllustsBean>>()
    val nexturl = MutableLiveData<String>()
    val tags = MutableLiveData<BookMarkTagsResponse>()
    val preference = SharedPreferencesServices.getInstance()
    fun OnLoadMoreListener() {
        if (nexturl.value != null)
            retrofit.getNextUserIllusts(nexturl.value!!).subscribe({
                adddata.value = it.illusts
                nexturl.value = it.next_url
            }, {}, {})
    }

    fun isUser(id: Long) = Single.create<Boolean> { it ->
        launchUI {
            val pt = AppDataRepository.getUser()
            val isuser = id == pt.userid
            it.onSuccess(isuser)
        }
    }

    fun OnRefreshListener(id: Long, string: String, tag: String?) {
        retrofit.getLikeIllust(id, string, tag).subscribe({
            data.value = it.illusts
            nexturl.value = it.next_url
        }, {}, {})
    }

    fun first(id: Long, string: String): Single<Boolean> {

        retrofit.getLikeIllust(id, string, null).subscribe({
            data.value = it.illusts
            nexturl.value = it.next_url
        }, {}, {})
        retrofit.getIllustBookmarkTags(id, string).subscribe({
            tags.value = it

        }, {}, {})
        return isUser(id)
    }
}