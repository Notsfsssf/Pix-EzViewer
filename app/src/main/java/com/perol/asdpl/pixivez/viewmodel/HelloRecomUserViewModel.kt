package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.SearchUserResponse

class HelloRecomUserViewModel :ViewModel(){
    val retrofit= RetrofitRespository.getInstance()
    val nexturl= MutableLiveData<String>()
    val adddata=MutableLiveData<List<SearchUserResponse.UserPreviewsBean>>()
    val data=MutableLiveData<List<SearchUserResponse.UserPreviewsBean>>()
    fun reData() {
        retrofit.getUserRecommanded().subscribe({
            nexturl.value=it.next_url
            data.value=it.user_previews
        },{

        },{})
    }

    fun getNext(){
        retrofit.getUserRecommandedUrl(nexturl.value!!).subscribe({
            nexturl.value=it.next_url
            adddata.value=it.user_previews
        },{},{})
    }
}