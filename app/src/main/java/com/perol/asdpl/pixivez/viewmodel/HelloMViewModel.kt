package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.UserEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
class HelloMViewModel : ViewModel() {
    val appDatabase = AppDatabase.getInstance(PxEZApp.instance)
    var userBean = MutableLiveData<UserEntity>()
    fun first() {
        appDatabase.userDao().getIllustHistory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (it.size != 0) {
                userBean.value = it[0]
            }
            else{
                userBean.value=null
            }
        }
    }
}