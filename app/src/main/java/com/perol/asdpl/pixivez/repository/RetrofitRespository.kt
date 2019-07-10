package com.perol.asdpl.pixivez.repository
import com.perol.asdpl.pixivez.R.color.tag
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ReFreshFunction
import com.perol.asdpl.pixivez.responses.*
import com.perol.asdpl.pixivez.services.AppApiPixivService
import com.perol.asdpl.pixivez.services.PxEZApp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody


class RetrofitRespository {


    var restClient = RestClient()
    var appApiPixivService: AppApiPixivService
    var sharedPreferencesServices: SharedPreferencesServices
    var Authorization: String
    var gifApiPixivService: AppApiPixivService
    var reFreshFunction: ReFreshFunction

    init {
        appApiPixivService = restClient.retrofit_AppAPI.create(AppApiPixivService::class.java)
        gifApiPixivService = restClient.getretrofit_GIF().create(AppApiPixivService::class.java)
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        Authorization = sharedPreferencesServices.getString("Authorization")
        reFreshFunction = ReFreshFunction.getInstance()

    }

    fun schedulersTransformer() {

        return
    }

    fun getLikeIllust(userid: Long, pub: String,tag:String?) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getLikeIllust(Authorization, userid, pub, tag)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    fun getIllustBookmarkTags(userid: Long, pub: String) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getIllustBookmarkTags(Authorization, userid, pub)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getNextUserIllusts(nexturl: String) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getNextUserIllusts(Authorization, nexturl)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getUserIllusts(id: Long,type:String) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getUserIllusts(Authorization, id, type)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getIllustRecommended(id: Long) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getIllustRecommended(Authorization, id)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getIllustRanking(mode: String, pickdata: String?) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getIllustRanking(Authorization, mode, pickdata)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun create(observable: Observable<Any>): Observable<Any>? {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            observable
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun getSearchAutoCompleteKeywords(newText: String?): Observable<PixivResponse> {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.getSearchAutoCompleteKeywords(Authorization, newText)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)


    }

    fun getPixivison(category: String) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getPixivisionArticles(Authorization, category)
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .retryWhen(reFreshFunction)

    fun getRecommend() = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getRecommend(Authorization)
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .retryWhen(reFreshFunction)

    fun getSearchIllust(word: String, sort: String, search_target: String?, bookmark_num: Int?, duration: String?): Observable<SearchIllustResponse> {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.getSearchIllust(word, sort, search_target, bookmark_num, duration, Authorization)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    }

    fun getUserFollower(long: Long): Observable<SearchUserResponse> {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.getUserFollower(Authorization, long)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun getUserFollowing(long: Long, restrict: String): Observable<SearchUserResponse> {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.getUserFollowing(Authorization, long, restrict)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun getNext(string: String): Observable<RecommendResponse> {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.getNext(Authorization, string)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun getFollowIllusts(restrict: String) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getFollowIllusts(Authorization, restrict)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getSearchUser(string: String): Observable<SearchUserResponse>? {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.getSearchUser(Authorization, string)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    }

    fun getNextUser(string: String): Observable<SearchUserResponse> {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.getNextUser(Authorization, string)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }


    fun postLikeIllust(int: Long): Observable<ResponseBody>? {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.postLikeIllust(Authorization, int.toLong(), "public", null)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    }

    fun getIllustTrendTags(): Observable<TrendingtagResponse> {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.getIllustTrendTags(Authorization)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun postLikeIllustWithTags(int: Long, string: String, arrayList: ArrayList<String>): Observable<ResponseBody>? =
            Observable.just(1).flatMap {
                Authorization = sharedPreferencesServices.getString("Authorization")
                appApiPixivService.postLikeIllust(Authorization, int.toLong(), string, arrayList)
            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)


    fun getIllust(long: Long): Observable<IllustDetailResponse>? {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.getIllust(Authorization, long)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun postUnlikeIllust(long: Long): Observable<ResponseBody> {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.postUnlikeIllust(Authorization, long)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun postfollowUser(long: Long, restrict: String): Observable<ResponseBody> {
        return Observable.just(1).flatMap {
            Authorization = sharedPreferencesServices.getString("Authorization")
            appApiPixivService.postFollowUser(Authorization, long, restrict)
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    }

    fun postunfollowUser(long: Long) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.postUnfollowUser(Authorization, long)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getUgoiraMetadata(long: Long) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getUgoiraMetadata(Authorization, long)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getGIFFile(string: String) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        gifApiPixivService.getGIFFile(string)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getBookmarkDetail(long: Long) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getLikeIllustDetail(Authorization, long)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)

    fun getUserDetail(userid: Long) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getUserDetail(Authorization, userid)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    fun getUserRecommanded() = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getUserRecommended(Authorization)
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).retryWhen(reFreshFunction)
    fun getUserRecommandedUrl(url:String) = Observable.just(1).flatMap {
        Authorization = sharedPreferencesServices.getString("Authorization")
        appApiPixivService.getNextUser(Authorization,url)
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



