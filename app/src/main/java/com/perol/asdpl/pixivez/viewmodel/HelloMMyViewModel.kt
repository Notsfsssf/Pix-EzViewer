package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.IllustsBean
import com.perol.asdpl.pixivez.services.PxEZApp

class HelloMMyViewModel : ViewModel() {
    var retrofitRespository = RetrofitRespository.getInstance()
    val illusts = MutableLiveData<ArrayList<IllustsBean>>()
    val addillusts = MutableLiveData<ArrayList<IllustsBean>>()
    val bookmarknum = MutableLiveData<IllustsBean>()
    val nexturl = MutableLiveData<String>()

    fun firstget() {
        retrofitRespository.getFollowIllusts("public").subscribe({
            nexturl.value = it.next_url
            illusts.value = it.illusts as ArrayList<IllustsBean>?
        }, {}, {})
    }
    fun onLoadMoreRequested() {
        retrofitRespository.getNext(nexturl.value!!).subscribe ({
            nexturl.value = it.next_url
            addillusts.value = it.illusts as ArrayList<IllustsBean>?
        },{},{})
    }

    fun OnRefreshListener(restrict:String) {
        retrofitRespository.getFollowIllusts(restrict).subscribe({
            nexturl.value = it.next_url
            illusts.value = it.illusts as ArrayList<IllustsBean>?
        }, {}, {})
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
}