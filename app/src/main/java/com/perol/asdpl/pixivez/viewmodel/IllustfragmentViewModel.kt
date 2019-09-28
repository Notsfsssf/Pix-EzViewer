package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.Illust

class IllustfragmentViewModel : ViewModel() {


    var illusts = MutableLiveData<ArrayList<Illust>>()
    var addIllusts = MutableLiveData<ArrayList<Illust>>()
    var retrofitRespository = RetrofitRespository.getInstance()
    var nexturl = MutableLiveData<String>()
    var bookmarkid = MutableLiveData<Long>()
    fun setPreview(word: String, sort: String, search_target: String?, duration: String?) {
        retrofitRespository.getSearchIllustPreview(word, sort, search_target, null, duration)
                .subscribe({
                    illusts.value = it.illusts as ArrayList<Illust>
                    nexturl.value = it.next_url
                }, {}, {})
    }

    fun firstsetdata(word: String, sort: String, search_target: String?, duration: String?) {
        retrofitRespository.getSearchIllust(word, sort, search_target, null, duration)
                .subscribe({
                    illusts.value = it.illusts as ArrayList<Illust>
                    nexturl.value = it.next_url
                }, {}, {})
    }

    fun onLoadMoreListen() {
        if (nexturl.value != null) {
            retrofitRespository.getNext(nexturl.value!!).subscribe({
                addIllusts.value = it.illusts as ArrayList<Illust>
                nexturl.value = it.next_url
            }, {}, {})
        }

    }


}

