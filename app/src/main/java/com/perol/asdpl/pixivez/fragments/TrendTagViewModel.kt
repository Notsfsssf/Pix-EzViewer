/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.fragments

import androidx.lifecycle.MutableLiveData
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.SearchHistoryEntity
import com.perol.asdpl.pixivez.viewmodel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrendTagViewModel : BaseViewModel() {
    var appDatabase = AppDatabase.getInstance(PxEZApp.instance)
    var searchhistroy = MutableLiveData<List<SearchHistoryEntity>>()
    var retrofitRespository: RetrofitRespository = RetrofitRespository.getInstance()

    init {
        resethistory()
    }

    fun addhistory(searchword: String) {
        Observable.create<Int> {
            appDatabase.searchhistoryDao().insert(SearchHistoryEntity(searchword))
            resethistory()
            it.onNext(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnError {

        }.subscribe {


        }.add()

    }

    fun sethis() {
        Observable.create<Int> {
            appDatabase.searchhistoryDao().deletehistory()

        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { }, {}).add()
    }

    fun resethistory() {
        appDatabase.searchhistoryDao().getSearchHistory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    searchhistroy.value = it
                }, {}, {}).add()
    }

    fun removeTag() {
        appDatabase.searchhistoryDao().deletehistory()
    }

    fun getIllustTrendTags() = retrofitRespository.getIllustTrendTags()
    fun deleteHistoryEntity(searchHistoryEntity: SearchHistoryEntity) = Observable.create<Int> {
        appDatabase.searchhistoryDao().deleteHistoryEntity(searchHistoryEntity)
        it.onNext(1)
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


}
