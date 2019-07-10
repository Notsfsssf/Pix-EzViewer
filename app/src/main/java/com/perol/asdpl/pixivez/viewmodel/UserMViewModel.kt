package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.UserDetailResponse
import io.reactivex.Single

class UserMViewModel : ViewModel() {
    var retrofitRespository = RetrofitRespository.getInstance()
    var userDetail = MutableLiveData<UserDetailResponse>()
    var isfollow = MutableLiveData<Boolean>()
    val prefer = SharedPreferencesServices.getInstance()

    init {

    }

    fun getdata(userid: Long) {
        retrofitRespository.getUserDetail(userid).subscribe({
            userDetail.value = it
            isfollow.value = it.user.isIs_followed
        }, {

        }, {})
    }

    fun onFabclick(userid: Long) {
        if (isfollow.value!!) {
            retrofitRespository.postunfollowUser(userid).subscribe({
                isfollow.value = false
            }, {}, {})
        } else {
            retrofitRespository.postfollowUser(userid, "public").subscribe({
                isfollow.value = true
            }, {}, {})
        }
    }

    fun onFabLongClick(userid: Long) {
        if (isfollow.value!!)
            retrofitRespository.postunfollowUser(userid).subscribe({
                isfollow.value = false
            }, {}, {})
        else retrofitRespository.postfollowUser(userid, "private").subscribe({
            isfollow.value = true
        }, {}, {})
    }

    fun isuser(id: Long) = Single.create<Boolean> {
        val isuser = id == prefer.getString("userid").toLong()
        it.onSuccess(isuser)
    }
}