package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.SearchUserResponse
import com.perol.asdpl.pixivez.services.PxEZApp

class IllustratorViewModel : ViewModel() {
    val retrofitRespository = RetrofitRespository.getInstance()
    val userpreviews = MutableLiveData<ArrayList<SearchUserResponse.UserPreviewsBean>>()
    val adduserpreviews = MutableLiveData<ArrayList<SearchUserResponse.UserPreviewsBean>>()
    val nexturl = MutableLiveData<String>()
    val refreshcomplete=MutableLiveData<Boolean>()
    init {
        refreshcomplete.value=false
    }
    fun first(long: Long, restrict: String, param2: Boolean) {
        if (param2) {
            retrofitRespository.getUserFollowing(long, restrict).subscribe ({
                userpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
                nexturl.value = it.next_url
            },{},{})
        } else {
            retrofitRespository.getUserFollower(long).subscribe ({
                userpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
                nexturl.value = it.next_url
            },{},{})
        }
    }

    fun OnLoadMore(string: String) {
        retrofitRespository.getNextUser(string).subscribe ({
            adduserpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
            nexturl.value = it.next_url
        },{},{})
    }

    fun OnRefresh(long: Long, restrict: String, param2: Boolean) {
        if (param2) {
            retrofitRespository.getUserFollowing(long, restrict).subscribe ({
                userpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
                nexturl.value = it.next_url
                refreshcomplete.value=!refreshcomplete.value!!
            },{},{})
        } else {
            retrofitRespository.getUserFollower(long).subscribe ({
                userpreviews.value = it.user_previews as ArrayList<SearchUserResponse.UserPreviewsBean>?
                nexturl.value = it.next_url
                refreshcomplete.value=!refreshcomplete.value!!
            },{},{})
        }
    }

}