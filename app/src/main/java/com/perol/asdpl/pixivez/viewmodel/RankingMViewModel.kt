package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.services.PxEZApp

class RankingMViewModel : ViewModel() {
    val retrofitRespository = RetrofitRespository.getInstance()
    val nexturl = MutableLiveData<String>()
    val addillusts = MutableLiveData<ArrayList<Illust>>()
    val illusts = MutableLiveData<ArrayList<Illust>>()
    val bookmarknum = MutableLiveData<Illust>()
    fun first(mode: String, picdata: String?) {
        retrofitRespository.getIllustRanking(mode, picdata).subscribe ({
            nexturl.value = it.next_url
            illusts.value =  ArrayList<Illust>(it.illusts)
        },{it.printStackTrace()},{})
    }

    fun OnRefresh(mode: String, picdata: String?) {
        retrofitRespository.getIllustRanking(mode, picdata).subscribe({
            nexturl.value = it.next_url
            illusts.value = it.illusts as ArrayList<Illust>?
        },{},{})
    }

    fun OnLoadMore() {
        retrofitRespository.getNext(nexturl.value!!).subscribe({
            nexturl.value = it.next_url
            addillusts.value = it.illusts as ArrayList<Illust>?
        },{},{})
    }
    fun OnItemChildLongClick(id: Illust) {
        if (id.is_bookmarked) {
            retrofitRespository.postUnlikeIllust(id.id)!!.subscribe({
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
            illusts.value =  ArrayList<Illust>(it.illusts)
        },{},{})
    }
}