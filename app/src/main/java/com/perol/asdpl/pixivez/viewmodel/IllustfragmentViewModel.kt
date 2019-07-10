package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.IllustsBean
import com.perol.asdpl.pixivez.services.PxEZApp
import java.time.Duration

class IllustfragmentViewModel : ViewModel() {


    var llustlivedata = MutableLiveData<ArrayList<IllustsBean>>()
    var retrofitRespository = RetrofitRespository.getInstance()
    var nexturl = MutableLiveData<String>()
    var bookmarkid = MutableLiveData<Long>()

    fun firstsetdata(word: String, sort: String, search_target: String?, duration: String?) {
        retrofitRespository.getSearchIllust(word, sort, search_target, null, duration)
          .subscribe({
                    llustlivedata.value = it.illusts as ArrayList<IllustsBean>
                    nexturl.value = it.next_url
                }, {}, {})
    }

    fun OnLoadMoreListen() {
        if (nexturl.value != null) {
            retrofitRespository.getNext(nexturl.value!!).subscribe({
                llustlivedata.value = it.illusts as ArrayList<IllustsBean>
                nexturl.value = it.next_url
            }, {},{})
        }

    }

    fun OnItemChildLongClick(int: Long) {
        retrofitRespository.postLikeIllust(int)!!.subscribe({
            bookmarkid.value = int
        },{},{})
    }
}

