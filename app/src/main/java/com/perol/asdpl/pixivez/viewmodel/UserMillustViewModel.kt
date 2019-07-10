package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.IllustsBean

class UserMillustViewModel : ViewModel() {
    val retrofit = RetrofitRespository.getInstance()
    val data = MutableLiveData<List<IllustsBean>>()
    val adddata=MutableLiveData<List<IllustsBean>>()
    val nexturl=MutableLiveData<String>()
    fun OnLoadMoreListener() {
        if (nexturl.value != null) {
            retrofit.getNextUserIllusts(nexturl.value!!).subscribe({
                adddata.value=it.illusts
                nexturl.value=it.next_url
            },{},{})
        }


    }

    fun OnRefreshListener(id: Long,type: String) {
        retrofit.getUserIllusts(id,type).subscribe({
            data.value=it.illusts
            nexturl.value=it.next_url
        }, {}, {})
    }

    fun first(id: Long,type:String) {
        retrofit.getUserIllusts(id,type).subscribe({
            data.value=it.illusts
            nexturl.value=it.next_url
        }, {}, {})
    }

}