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

package com.perol.asdpl.pixivez.repository

import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ReFreshFunction
import com.perol.asdpl.pixivez.responses.*
import com.perol.asdpl.pixivez.services.AppApiPixivService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.HttpException


class RetrofitRespository {


    var restClient = RestClient()
    var appApiPixivService: AppApiPixivService
    var sharedPreferencesServices: SharedPreferencesServices
    var Authorization: String = ""
    var gifApiPixivService: AppApiPixivService
    var reFreshFunction: ReFreshFunction

    init {
        appApiPixivService = restClient.retrofit_AppAPI.create(AppApiPixivService::class.java)
        gifApiPixivService = restClient.getretrofit_GIF().create(AppApiPixivService::class.java)
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        resetToken()
        reFreshFunction = ReFreshFunction.getInstance()

    }

    private fun resetToken() {
        runBlocking {
            try {
                Authorization = AppDataRepository.getUser().Authorization
            } catch (e: Exception) {
            }
        }
    }

    fun getLikeIllust(userid: Long, pub: String, tag: String?) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getLikeIllust(Authorization, userid, pub, tag)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getIllustBookmarkTags(userid: Long, pub: String) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getIllustBookmarkTags(Authorization, userid, pub)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getNextUserIllusts(nexturl: String) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getNextUserIllusts(Authorization, nexturl)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getUserIllusts(id: Long, type: String) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getUserIllusts(Authorization, id, type)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getIllustRecommended(id: Long) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getIllustRecommended(Authorization, id)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getIllustRanking(mode: String, pickdata: String?) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getIllustRanking(Authorization, mode, pickdata)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun create(observable: Observable<Any>): Observable<Any>? {
        return Observable.just(1).flatMap {
            resetToken()
            observable
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun getSearchAutoCompleteKeywords(newText: String): Observable<PixivResponse> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getSearchAutoCompleteKeywords(Authorization, newText)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)


    }

    fun getPixivison(category: String) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getPixivisionArticles(Authorization, category)
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .retryWhen(reFreshFunction)

    fun getRecommend() = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getRecommend(Authorization)
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .retryWhen(reFreshFunction)

    fun getSearchIllustPreview(word: String, sort: String, search_target: String?, bookmark_num: Int?, duration: String?): Observable<SearchIllustResponse> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getSearchIllustPreview(word, sort, search_target, bookmark_num, duration, Authorization)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    }

    fun getSearchIllust(
        word: String,
        sort: String,
        search_target: String?,
        start_date: String?,
        end_date: String?,
        bookmark_num: Int?
    ): Observable<SearchIllustResponse> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getSearchIllust(
                word,
                sort,
                search_target,
                start_date,
                end_date,
                bookmark_num,
                Authorization
            )
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    }

    fun postUserProfileEdit(part: MultipartBody.Part): Observable<ResponseBody> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.postUserProfileEdit(Authorization, part)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .retryWhen(reFreshFunction)
    }

    fun getUserFollower(long: Long): Observable<SearchUserResponse> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getUserFollower(Authorization, long)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun getUserFollowing(long: Long, restrict: String): Observable<SearchUserResponse> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getUserFollowing(Authorization, long, restrict)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun getNext(string: String): Observable<RecommendResponse> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getNext(Authorization, string)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun getFollowIllusts(restrict: String) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getFollowIllusts(Authorization, restrict)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getSearchUser(string: String): Observable<SearchUserResponse>? {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getSearchUser(Authorization, string)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    }

    fun getNextUser(string: String): Observable<SearchUserResponse> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getNextUser(Authorization, string)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }


    fun postLikeIllust(int: Long): Observable<ResponseBody>? {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.postLikeIllust(Authorization, int.toLong(), "public", null)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    }

    fun getIllustTrendTags(): Observable<TrendingtagResponse> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getIllustTrendTags(Authorization)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun postLikeIllustWithTags(
        int: Long,
        string: String,
        arrayList: ArrayList<String>
    ): Observable<ResponseBody> =
            Observable.just(1).flatMap {
                resetToken()
                appApiPixivService.postLikeIllust(Authorization, int.toLong(), string, arrayList)
            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)


    fun getIllust(long: Long): Observable<IllustDetailResponse> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.getIllust(Authorization, long)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    suspend fun getIllustCor(long: Long): IllustDetailResponse? {
        var illustDetailResponse: IllustDetailResponse? = null
        try {
            illustDetailResponse = appApiPixivService.getIllustCor(Authorization, long)
        } catch (e: Exception) {
            if (e is HttpException) {
                resetToken()
                getIllustCor(long)
            }
        } finally {
            if (illustDetailResponse == null) {
                resetToken()
                getIllustCor(long)
            }


            return illustDetailResponse
        }

    }

    fun postUnlikeIllust(long: Long): Observable<ResponseBody> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.postUnlikeIllust(Authorization, long)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun postfollowUser(long: Long, restrict: String): Observable<ResponseBody> {
        return Observable.just(1).flatMap {
            resetToken()
            appApiPixivService.postFollowUser(Authorization, long, restrict)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun postunfollowUser(long: Long) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.postUnfollowUser(Authorization, long)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getUgoiraMetadata(long: Long) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getUgoiraMetadata(Authorization, long)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getGIFFile(string: String) = Observable.just(1).flatMap {
        resetToken()
        gifApiPixivService.getGIFFile(string)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getBookmarkDetail(long: Long) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getLikeIllustDetail(Authorization, long)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getUserDetail(userid: Long) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getUserDetail(Authorization, userid)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getUserRecommanded() = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getUserRecommended(Authorization)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getUserRecommandedUrl(url: String) = Observable.just(1).flatMap {
        resetToken()
        appApiPixivService.getNextUser(Authorization, url)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    companion object {
        private var instance: RetrofitRespository? = null
        fun getInstance(): RetrofitRespository {
            if (instance == null) {
                synchronized(RetrofitRespository::class.java) {
                    if (instance == null) {
                        instance = RetrofitRespository()
                    }
                }
            }
            return instance!!
        }
    }

}



