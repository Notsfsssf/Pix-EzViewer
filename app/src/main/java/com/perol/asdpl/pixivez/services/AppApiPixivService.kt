package com.perol.asdpl.pixivez.services


import com.perol.asdpl.pixivez.responses.*

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Created by asdpl on 2018/2/10.
 */

interface AppApiPixivService {

    @GET("v1/walkthrough/illusts")
    fun  walkthroughIllusts(): Observable<IllustNext>

    //    @FormUrlEncoded
    //    @POST("/v1/mute/edit")
    //    public abstract l<PixivResponse> postMuteSetting(@Header("Authorization") String paramString, @Field("add_user_ids[]") List<Long> paramList1, @Field("delete_user_ids[]") List<Long> paramList2, @Field("add_tags[]") List<String> paramList3, @Field("delete_tags[]") List<String> paramList4);
    @GET("/v2/illust/bookmark/detail")
    fun getLikeIllustDetail(@Header("Authorization") paramString: String, @Query("illust_id") paramLong: Long): Observable<BookMarkDetailResponse>

    @GET("v1/user/bookmark-tags/illust")
    fun getIllustBookmarkTags(@Header("Authorization") paramString1: String, @Query("user_id") paramLong: Long, @Query("restrict") paramString2: String): Observable<BookMarkTagsResponse>

    @GET
    fun getNexttags(@Header("Authorization") paramString1: String, @Url paramString2: String): Observable<BookMarkTagsResponse>

    @GET("/v1/spotlight/articles?filter=for_ios")
    fun getPixivisionArticles(@Header("Authorization") paramString1: String, @Query("category") paramString2: String): Observable<SpotlightResponse>

    @GET("/v1/user/recommended?filter=for_android")
    fun getUserRecommended(@Header("Authorization") paramString: String): Observable<SearchUserResponse>

    @GET
    fun getNextPixivisionArticles(@Header("Authorization") paramString1: String, @Url paramString2: String): Observable<SpotlightResponse>

    @Streaming
    @GET
    fun getGIFFile(@Url fileUrl: String): Observable<ResponseBody>

    @Multipart
    @POST("/v1/user/profile/edit")
    fun postUserProfileEdit(@Header("Authorization") paramString: String, @Part paramRequestBody: MultipartBody.Part): Observable<ResponseBody>

    @GET("/v1/ugoira/metadata")
    fun getUgoiraMetadata(@Header("Authorization") paramString: String, @Query("illust_id") paramLong: Long): Observable<UgoiraMetadataResponse>

    @GET("/v1/user/browsing-history/illusts")
    fun getIllustBrowsingHistory(@Header("Authorization") paramString: String): Observable<IllustNext>

    @GET("/v1/user/bookmarks/illust")
    fun getLikeIllust(@Header("Authorization") paramString1: String, @Query("user_id") paramLong: Long, @Query("restrict") paramString2: String, @Query("tag") paramString3: String?): Observable<IllustNext>

    @FormUrlEncoded
    @POST("/v1/user/follow/delete")
    fun postUnfollowUser(@Header("Authorization") paramString: String, @Field("user_id") paramLong: Long): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/user/follow/add")
    fun postFollowUser(@Header("Authorization") paramString1: String, @Field("user_id") paramLong: Long, @Field("restrict") paramString2: String): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/illust/bookmark/delete")
    fun postUnlikeIllust(@Header("Authorization") paramString: String, @Field("illust_id") paramLong: Long): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("/v2/illust/bookmark/add")
    fun postLikeIllust(@Header("Authorization") paramString1: String, @Field("illust_id") paramLong: Long, @Field("restrict") paramString2: String, @Field("tags[]") paramList: List<String>?): Observable<ResponseBody>

    @GET("/v2/search/autocomplete?merge_plain_keyword_results=true")
    fun getSearchAutoCompleteKeywords(@Header("Authorization") paramString1: String, @Query("word") paramString2: String?): Observable<PixivResponse>

    @GET("/v1/trending-tags/illust?filter=for_ios")
    fun getIllustTrendTags(@Header("Authorization") paramString: String): Observable<TrendingtagResponse>

    @GET("/v1/search/illust?filter=for_ios&merge_plain_keyword_results=true")
    fun getSearchIllust(@Query("word") paramString1: String, @Query("sort") paramString2: String, @Query("search_target") paramString3: String?, @Query("bookmark_num") paramInteger: Int?, @Query("duration") paramString4: String?, @Header("Authorization") paramString5: String): Observable<SearchIllustResponse>
    @GET("/v1/search/popular-preview/illust?filter=for_ios&merge_plain_keyword_results=true")
    fun getSearchIllustPreview(@Query("word") paramString1: String, @Query("sort") paramString2: String, @Query("search_target") paramString3: String?, @Query("bookmark_num") paramInteger: Int?, @Query("duration") paramString4: String?, @Header("Authorization") paramString5: String): Observable<SearchIllustResponse>
    @GET("/v1/search/novel")
    fun getSearchNovel(@Header("Authorization") paramString1: String, @Query("word") paramString2: String, @Query("sort") paramString3: String, @Query("search_target") paramString4: String, @Query("bookmark_num") paramInteger: Int?, @Query("duration") paramString5: String): Call<PixivResponse>

    @GET("/v1/search/user?filter=for_ios")
    fun getSearchUser(@Header("Authorization") paramString1: String, @Query("word") paramString2: String): Observable<SearchUserResponse>

    @GET("/v1/search/popular-preview/illust?filter=for_ios")
    fun getPopularPreviewIllust(@Header("Authorization") paramString1: String, @Query("word") paramString2: String, @Query("search_target") paramString3: String, @Query("duration") paramString4: String): Call<PixivResponse>
    

    @GET("/v1/user/follower?filter=for_ios")
    fun getUserFollower(@Header("Authorization") paramString: String, @Query("user_id") paramLong: Long): Observable<SearchUserResponse>

    @GET("/v1/user/following?filter=for_ios")
    fun getUserFollowing(@Header("Authorization") paramString1: String, @Query("user_id") paramLong: Long, @Query("restrict") paramString2: String): Observable<SearchUserResponse>

    @GET("/v1/illust/recommended?filter=for_ios&include_ranking_label=true")
    fun getRecommend(@Header("Authorization") Authorization: String): Observable<RecommendResponse>

    @GET("/v1/illust/detail?filter=for_ios")
    fun getIllust(@Header("Authorization") paramString: String, @Query("illust_id") paramLong: Long): Observable<IllustDetailResponse>

    @GET("/v1/illust/detail?filter=for_ios")
    suspend fun getIllustCor(@Header("Authorization") paramString: String, @Query("illust_id") paramLong: Long): IllustDetailResponse

    @GET("/v2/illust/related?filter=for_android")
    fun getIllustRecommended(@Header("Authorization") paramString: String, @Query("illust_id") paramLong: Long): Observable<RecommendResponse>

    @GET("/v1/manga/recommended?filter=for_ios")
    fun getRecommendedMangaList(@Header("Authorization") paramString1: String, @Query("include_ranking_illusts") paramBoolean: Boolean, @Query("bookmark_illust_ids") paramString2: String): Observable<RecommendResponse>

    @GET
    fun getNext(@Header("Authorization") paramString1: String, @Url paramString2: String): Observable<RecommendResponse>

    @FormUrlEncoded
    @POST("/v2/user/browsing-history/illust/add")
    fun postAddIllustBrowsingHistory(@Header("Authorization") paramString: String, @Field("illust_ids[]") paramList: List<Long>): Observable<ResponseBody>

    @GET
    fun getNextUserFollowing(@Url paramString2: String): Observable<SearchUserResponse>

    @GET
    fun getNextHistory(@Header("Authorization") Authorization: String, @Url paramString2: String): Observable<IllustNext>



    @GET("/v1/illust/ranking?filter=for_ios")
    fun getIllustRanking(@Header("Authorization") paramString1: String, @Query("mode") paramString2: String, @Query("date") paramString3: String?): Observable<IllustNext>

    @GET("/v1/illust/ranking?filter=for_ios")
    fun getIllustRanking1(@Header("Authorization") paramString1: String, @Query("mode") paramString2: String, @Query("date") paramString3: String): Observable<ResponseBody>

    @GET("/v2/illust/follow")
    fun getFollowIllusts(@Header("Authorization") paramString1: String, @Query("restrict") paramString2: String): Observable<IllustNext>

    @GET("/v1/illust/comments")
    fun getIllustComments(@Header("Authorization") paramString: String, @Query("illust_id") paramLong: Long): Observable<IllustCommentsResponse>
    @GET("/v1/illust/comments")
    fun getIllustNextComments(@Header("Authorization") paramString: String, @Url paramString2: String): Observable<IllustCommentsResponse>
    @FormUrlEncoded
    @POST("v1/illust/comment/add")
    fun postIllustComment(@Header("Authorization") paramString1: String, @Field("illust_id") illust_id: Long, @Field("comment") comment: String, @Field("parent_comment_id") parent_comment_id: Int?): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/mute/edit")
    fun postMuteSetting(@Header("Authorization") paramString: String, @Field("add_user_ids[]") paramList1: List<Long>, @Field("delete_user_ids[]") paramList2: List<Long>, @Field("add_tags[]") paramList3: List<String>, @Field("delete_tags[]") paramList4: List<String>): Observable<PixivResponse>

    @GET("/v1/user/detail?filter=for_ios")
    fun getUserDetail(@Header("Authorization") paramString: String, @Query("user_id") id: Long): Observable<UserDetailResponse>

    @GET("/v1/user/detail?filter=for_ios")
    fun getUserDetailBody(@Header("Authorization") paramString: String, @Query("user_id") id: Long): Observable<ResponseBody>

    @GET("/v1/user/illusts?filter=for_ios")
    fun getUserIllusts(@Header("Authorization") paramString1: String, @Query("user_id") paramLong: Long, @Query("type") paramString2: String): Observable<IllustNext>

    @GET("/v1/user/illusts?filter=for_ios")
    suspend fun getUserIllustsCor(@Header("Authorization") paramString1: String, @Query("user_id") paramLong: Long, @Query("type") paramString2: String): IllustNext

    @GET
    fun getNextUserIllusts(@Header("Authorization") paramString1: String, @Url paramString2: String): Observable<IllustNext>

    @GET
    suspend fun getNextUserIllustsCor(@Header("Authorization") paramString1: String, @Url paramString2: String): IllustNext

    @GET
    fun getNextUserIllustsBlock(@Header("Authorization") paramString1: String, @Url paramString2: String): Call<IllustNext>

    @GET
    fun getNextUser(@Header("Authorization") paramString1: String, @Url paramString2: String): Observable<SearchUserResponse>

    @GET
    fun getImageBitmap(@Url paramString2: String): Observable<ResponseBody>

}
