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

package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.Tags
import com.perol.asdpl.pixivez.responses.TrendingtagResponse
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.SearchHistoryEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchMViewModel : ViewModel() {


    var searchhistroy = MutableLiveData<ArrayList<String>>()
    lateinit var autoword: MutableLiveData<List<Tags>>

    private var retrofitRespository: RetrofitRespository
    var trendtag = MutableLiveData<TrendingtagResponse>()
    var appDatabase = AppDatabase.getInstance(PxEZApp.instance)

    init {

        resethistory()
        retrofitRespository = RetrofitRespository.getInstance()

        autoword = MutableLiveData()


    }

    fun resethistory() {
        appDatabase.searchhistoryDao().getSearchHistory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val arrayList = ArrayList<String>()
                    it.map { th ->

                        arrayList.add(th.word)
                    }
                    searchhistroy.value = arrayList
                }, {}, {})
    }

    fun addhistory(string: String) {
        Observable.create<Int> {
            appDatabase.searchhistoryDao().insert(SearchHistoryEntity(string))
            resethistory()
            it.onNext(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnError {

        }.subscribe {


        }

    }

    fun sethis() {
        Observable.create<Int> {
            appDatabase.searchhistoryDao().deletehistory()

        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({}, { }, {})
    }

    fun onQueryTextSubmit(arrayList: ArrayList<String>) {

        Observable.create<Int> {
            appDatabase.searchhistoryDao().insert(SearchHistoryEntity(arrayList[0]))
            it.onNext(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { }
        resethistory()
    }

    fun onQueryTextChange(newText: String) {
        val c = retrofitRespository.getSearchAutoCompleteKeywords(newText)
        c.subscribe({ autoword.value = it.tags }, {}, {})


    }

    fun getIllustTrendTags() {
        val c = retrofitRespository.getIllustTrendTags()
        c.subscribe({ trendtag.value = it }, {}, {})
    }

}