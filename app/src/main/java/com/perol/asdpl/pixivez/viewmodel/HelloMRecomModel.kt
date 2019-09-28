package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.responses.SpotlightResponse
import com.perol.asdpl.pixivez.services.PxEZApp

class HelloMRecomModel : ViewModel() {
    var retrofitRespository = RetrofitRespository.getInstance()
    val illusts = MutableLiveData<ArrayList<Illust>>()
    val addillusts = MutableLiveData<ArrayList<Illust>>()
    val bookmarknum = MutableLiveData<Illust>()
    val nexturl = MutableLiveData<String>()
    val articles=MutableLiveData<SpotlightResponse>()
    fun firstget() {
        retrofitRespository.getRecommend().subscribe ({
            nexturl.value = it.next_url
            illusts.value = it.illusts as ArrayList<Illust>?
        },{

        },{})
        retrofitRespository.getPixivison("all").subscribe({
            articles.value=it
        },{},{})
    }

    fun onLoadMoreRequested() {
        retrofitRespository.getNext(nexturl.value!!).subscribe ({
            nexturl.value = it.next_url
            addillusts.value = it.illusts as ArrayList<Illust>?
        },{},{})
    }

    fun OnRefreshListener() {
        retrofitRespository.getRecommend().subscribe ({
            nexturl.value = it.next_url
            illusts.value = it.illusts as ArrayList<Illust>?
        },{},{})
        retrofitRespository.getPixivison("all").subscribe({
            articles.value=it
        },{},{})
    }

    fun OnItemChildLongClick(id: Illust) {
        if (id.is_bookmarked) {
            retrofitRespository.postUnlikeIllust(id.id.toLong())!!.subscribe({
                bookmarknum.value = id
            },{},{})
        } else {
            retrofitRespository.postLikeIllust(id.id)!!.subscribe({
                bookmarknum.value = id
            },{},{})
        }
    }

}