package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.Tags
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.SearchHistoryEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TagsTextViewModel : ViewModel() {
    val retrofitRespository = RetrofitRespository.getInstance()
    var appDatabase = AppDatabase.getInstance(PxEZApp.instance)
    val tags = MutableLiveData<List<Tags>>()
    fun onQueryTextChange(newText: String) {
        retrofitRespository.getSearchAutoCompleteKeywords(newText).subscribe({
            tags.value = it.tags
        }, {})
    }

    fun addhistory(searchword: String) {
        Observable.create<Int> {
            appDatabase.searchhistoryDao().insert(SearchHistoryEntity(searchword))
           // resethistory()
            it.onNext(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnError {

        }.subscribe{


        }

    }
}