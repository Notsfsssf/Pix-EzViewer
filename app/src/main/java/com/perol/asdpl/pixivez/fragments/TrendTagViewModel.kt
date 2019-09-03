package com.perol.asdpl.pixivez.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.SearchHistoryEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrendTagViewModel : ViewModel() {
    var appDatabase = AppDatabase.getInstance(PxEZApp.instance)
    var searchhistroy = MutableLiveData<ArrayList<String>>()
    var retrofitRespository: RetrofitRespository=RetrofitRespository.getInstance()
    init {
        resethistory()
    }
    fun addhistory(searchword: String) {
        Observable.create<Int> {
            appDatabase.searchhistoryDao().insert(SearchHistoryEntity(searchword))
            resethistory()
            it.onNext(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnError {

        }.subscribe{


        }

    }
    fun sethis() {
        Observable.create<Int> {
            appDatabase.searchhistoryDao().deletehistory()

        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({}, { }, {})
    }
    fun resethistory() {
        appDatabase.searchhistoryDao().getSearchHistory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    val arrayList = ArrayList<String>()
                    it.map { th ->

                        arrayList.add(th.word)
                    }
                    searchhistroy.value = arrayList
                },{},{})
    }
    fun getIllustTrendTags()
       = retrofitRespository.getIllustTrendTags()



    // TODO: Implement the ViewModel
}
