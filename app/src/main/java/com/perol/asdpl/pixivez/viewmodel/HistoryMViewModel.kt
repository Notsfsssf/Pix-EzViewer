package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.IllustBeanEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class HistoryMViewModel : ViewModel() {
    val illustBeans = MutableLiveData<ArrayList<IllustBeanEntity>>()
    val appDatabase = AppDatabase.getInstance(PxEZApp.instance)

    fun first() {
        appDatabase.illusthistoryDao().getIllustHistory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Collections.reverse(it)
                    illustBeans.value =it as ArrayList<IllustBeanEntity>
                }

    }

    fun fabOnClick() {
        Observable.just(1).observeOn(Schedulers.io())
                .subscribe {
                    appDatabase.illusthistoryDao().deleteHistory()
                }
    }
}