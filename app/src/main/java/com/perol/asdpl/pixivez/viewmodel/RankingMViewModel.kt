package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.IllustsBean
import com.perol.asdpl.pixivez.services.PxEZApp

class RankingMViewModel : ViewModel() {
    val retrofitRespository = RetrofitRespository.getInstance()
    val nexturl = MutableLiveData<String>()
    val addillusts = MutableLiveData<ArrayList<IllustsBean>>()
    val illusts = MutableLiveData<ArrayList<IllustsBean>>()
    val bookmarknum = MutableLiveData<IllustsBean>()
    fun first(mode: String, picdata: String?) {
        retrofitRespository.getIllustRanking(mode, picdata).subscribe ({
            nexturl.value = it.next_url
            illusts.value = it.illusts as ArrayList<IllustsBean>?
        },{},{})
    }

    fun OnRefresh(mode: String, picdata: String?) {
        retrofitRespository.getIllustRanking(mode, picdata).subscribe({
            nexturl.value = it.next_url
            illusts.value = it.illusts as ArrayList<IllustsBean>?
        },{},{})
    }

    fun OnLoadMore() {
        retrofitRespository.getNext(nexturl.value!!).subscribe({
            nexturl.value = it.next_url
            addillusts.value = it.illusts as ArrayList<IllustsBean>?
        },{},{})
    }
    fun OnItemChildLongClick(id: IllustsBean) {
        if (id.isIs_bookmarked) {
            retrofitRespository.postUnlikeIllust(id.id.toLong())!!.subscribe({
                bookmarknum.value = id
            },{},{})
        } else {
            retrofitRespository.postLikeIllust(id.id)!!.subscribe({
                bookmarknum.value = id
            },{},{})
        }
    }
    fun datapick(mode: String, picdata: String?) {
        retrofitRespository.getIllustRanking(mode, picdata).subscribe( {
            nexturl.value = it.next_url
            illusts.value = it.illusts as ArrayList<IllustsBean>?
        },{},{})
    }
}