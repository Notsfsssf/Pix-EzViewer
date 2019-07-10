package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.SearchUserResponse
import com.perol.asdpl.pixivez.services.PxEZApp
import io.reactivex.Observable

class UserViewModel : ViewModel() {
    var users = MutableLiveData<SearchUserResponse>()

    var retrofitRespository = RetrofitRespository.getInstance()
    var nexturl = MutableLiveData<String>()
    fun getnextusers(word: String) {
        retrofitRespository.getNextUser(word)!!.subscribe({
            users.value = it
            nexturl.value = it.next_url
        },{},{})
    }

    fun getSearchUser(word: String) {
        val c = retrofitRespository.create(retrofitRespository.getSearchUser(word) as Observable<Any>) as Observable<SearchUserResponse>
        c.subscribe(
                {
                    users.value = it
                    nexturl.value = it.next_url
                }, {

        },{},{}
        )
    }
}