package com.perol.asdpl.pixivez.services;


import com.perol.asdpl.pixivez.responses.AboutPictureResponse;
import com.perol.asdpl.pixivez.responses.BookMarkDetailResponse;
import com.perol.asdpl.pixivez.responses.BookMarkTagsResponse;
import com.perol.asdpl.pixivez.responses.FollowResponse;
import com.perol.asdpl.pixivez.responses.IllustCommentsResponse;
import com.perol.asdpl.pixivez.responses.IllustDetailResponse;
import com.perol.asdpl.pixivez.responses.IllustRankingResponse;
import com.perol.asdpl.pixivez.responses.IllustfollowResponse;
import com.perol.asdpl.pixivez.responses.PixivResponse;
import com.perol.asdpl.pixivez.responses.RecommendResponse;
import com.perol.asdpl.pixivez.responses.SearchIllustResponse;
import com.perol.asdpl.pixivez.responses.SearchUserResponse;
import com.perol.asdpl.pixivez.responses.SpotlightResponse;
import com.perol.asdpl.pixivez.responses.TrendingtagResponse;
import com.perol.asdpl.pixivez.responses.UgoiraMetadataResponse;
import com.perol.asdpl.pixivez.responses.UserDetailResponse;
import com.perol.asdpl.pixivez.responses.UserIllustsResponse;
import com.perol.asdpl.pixivez.responses.WalkthroughResponse;

import java.util.List;

import io.reactivex.Observable;
import kotlinx.coroutines.Deferred;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by asdpl on 2018/2/10.
 */

public interface AppApiPixivService {
    //    @FormUrlEncoded
//    @POST("/v1/mute/edit")
//    public abstract l<PixivResponse> postMuteSetting(@Header("Authorization") String paramString, @Field("add_user_ids[]") List<Long> paramList1, @Field("delete_user_ids[]") List<Long> paramList2, @Field("add_tags[]") List<String> paramList3, @Field("delete_tags[]") List<String> paramList4);
    @GET("/v2/illust/bookmark/detail")
    Observable<BookMarkDetailResponse> getLikeIllustDetail(@Header("Authorization") String paramString, @Query("illust_id") long paramLong);

    @GET("v1/user/bookmark-tags/illust")
    Observable<BookMarkTagsResponse> getIllustBookmarkTags(@Header("Authorization") String paramString1, @Query("user_id") long paramLong, @Query("restrict") String paramString2);

    @GET("v1/walkthrough/illusts")
    Observable<WalkthroughResponse> getWalkthroughIllusts();

    @GET
    Observable<BookMarkTagsResponse> getNexttags(@Header("Authorization") String paramString1, @Url String paramString2);

    @GET("/v1/spotlight/articles?filter=for_ios")
    Observable<SpotlightResponse> getPixivisionArticles(@Header("Authorization") String paramString1, @Query("category") String paramString2);

    @GET("/v1/user/recommended?filter=for_android")
    Observable<SearchUserResponse> getUserRecommended(@Header("Authorization") String paramString);

    @GET
    Observable<SpotlightResponse> getNextPixivisionArticles(@Header("Authorization") String paramString1, @Url String paramString2);

    @Streaming
    @GET
    Observable<ResponseBody> getGIFFile(@Url String fileUrl);

    @Multipart
    @POST("/v1/user/profile/edit")
    Observable<ResponseBody> postUserProfileEdit(@Header("Authorization") String paramString, @Part MultipartBody.Part paramRequestBody);

    @GET("/v1/ugoira/metadata")
    Observable<UgoiraMetadataResponse> getUgoiraMetadata(@Header("Authorization") String paramString, @Query("illust_id") long paramLong);

    @GET("/v1/user/browsing-history/illusts")
    Observable<UserIllustsResponse> getIllustBrowsingHistory(@Header("Authorization") String paramString);

    @GET("/v1/user/bookmarks/illust")
    Observable<UserIllustsResponse> getLikeIllust(@Header("Authorization") String paramString1, @Query("user_id") long paramLong, @Query("restrict") String paramString2, @Query("tag") String paramString3);

    @FormUrlEncoded
    @POST("/v1/user/follow/delete")
    Observable<ResponseBody> postUnfollowUser(@Header("Authorization") String paramString, @Field("user_id") long paramLong);

    @FormUrlEncoded
    @POST("/v1/user/follow/add")
    Observable<ResponseBody> postFollowUser(@Header("Authorization") String paramString1, @Field("user_id") long paramLong, @Field("restrict") String paramString2);

    @FormUrlEncoded
    @POST("/v1/illust/bookmark/delete")
    Observable<ResponseBody> postUnlikeIllust(@Header("Authorization") String paramString, @Field("illust_id") long paramLong);

    @FormUrlEncoded
    @POST("/v2/illust/bookmark/add")
    Observable<ResponseBody> postLikeIllust(@Header("Authorization") String paramString1, @Field("illust_id") long paramLong, @Field("restrict") String paramString2, @Field("tags[]") List<String> paramList);

    @GET("/v1/search/autocomplete")
    Observable<PixivResponse> getSearchAutoCompleteKeywords(@Header("Authorization") String paramString1, @Query("word") String paramString2);

    @GET("/v1/trending-tags/illust?filter=for_ios")
    Observable<TrendingtagResponse> getIllustTrendTags(@Header("Authorization") String paramString);

    @GET("/v1/search/illust?filter=for_ios")
    Observable<SearchIllustResponse> getSearchIllust(@Query("word") String paramString1, @Query("sort") String paramString2, @Query("search_target") String paramString3, @Query("bookmark_num") Integer paramInteger, @Query("duration") String paramString4, @Header("Authorization") String paramString5);

    @GET("/v1/search/novel")
    Call<PixivResponse> getSearchNovel(@Header("Authorization") String paramString1, @Query("word") String paramString2, @Query("sort") String paramString3, @Query("search_target") String paramString4, @Query("bookmark_num") Integer paramInteger, @Query("duration") String paramString5);

    @GET("/v1/search/user?filter=for_ios")
    Observable<SearchUserResponse> getSearchUser(@Header("Authorization") String paramString1, @Query("word") String paramString2);

    @GET("/v1/search/popular-preview/illust?filter=for_ios")
    Call<PixivResponse> getPopularPreviewIllust(@Header("Authorization") String paramString1, @Query("word") String paramString2, @Query("search_target") String paramString3, @Query("duration") String paramString4);



    @GET("/v2/illust/follow?restrict=public")
    Call<FollowResponse> getNewTab(@Header("Authorization") String Authorization);

    @GET("/v1/user/follower?filter=for_ios")
    Observable<SearchUserResponse> getUserFollower(@Header("Authorization") String paramString, @Query("user_id") long paramLong);

    @GET("/v1/user/following?filter=for_ios")
    Observable<SearchUserResponse> getUserFollowing(@Header("Authorization") String paramString1, @Query("user_id") long paramLong, @Query("restrict") String paramString2);

    @GET("/v1/illust/recommended?filter=for_ios&include_ranking_label=true")
    Observable<RecommendResponse> getRecommend(@Header("Authorization") String Authorization);

    @GET("/v1/illust/detail?filter=for_ios")
    Observable<IllustDetailResponse> getIllust(@Header("Authorization") String paramString, @Query("illust_id") long paramLong);

    @GET("/v2/illust/related?filter=for_android")
    Observable<RecommendResponse> getIllustRecommended(@Header("Authorization") String paramString, @Query("illust_id") long paramLong);

    @GET("/v1/manga/recommended?filter=for_ios")
    Observable<RecommendResponse> getRecommendedMangaList(@Header("Authorization") String paramString1, @Query("include_ranking_illusts") boolean paramBoolean, @Query("bookmark_illust_ids") String paramString2);
    @GET
    Observable<RecommendResponse> getNext(@Header("Authorization") String paramString1, @Url String paramString2);

    @FormUrlEncoded
    @POST("/v2/user/browsing-history/illust/add")
    Observable<ResponseBody> postAddIllustBrowsingHistory(@Header("Authorization") String paramString, @Field("illust_ids[]") List<Long> paramList);

    @GET
    Observable<SearchUserResponse> getNextUserFollowing(@Url String paramString2);

    @GET
    Observable<UserIllustsResponse> getNextHistory(@Header("Authorization") String Authorization, @Url String paramString2);

    @GET("/v1/illust/related?filter=for_ios")
    Observable<AboutPictureResponse> getAboutPicture(@Query("illust_id") int id);

    @GET("/v1/illust/ranking?filter=for_ios")
    Observable<IllustRankingResponse> getIllustRanking(@Header("Authorization") String paramString1, @Query("mode") String paramString2, @Query("date") String paramString3);

    @GET("/v1/illust/ranking?filter=for_ios")
    Observable<ResponseBody> getIllustRanking1(@Header("Authorization") String paramString1, @Query("mode") String paramString2, @Query("date") String paramString3);

    @GET("/v2/illust/follow")
    Observable<IllustfollowResponse> getFollowIllusts(@Header("Authorization") String paramString1, @Query("restrict") String paramString2);

    @GET("/v1/illust/comments")
    Observable<IllustCommentsResponse> getIllustComments(@Header("Authorization") String paramString, @Query("illust_id") long paramLong);

    @FormUrlEncoded
    @POST("v1/illust/comment/add")
    Observable<ResponseBody> postIllustComment(@Header("Authorization") String paramString1, @Field("illust_id") long illust_id, @Field("comment") String comment, @Field("parent_comment_id") Integer parent_comment_id);

    @FormUrlEncoded
    @POST("/v1/mute/edit")
    Observable<PixivResponse> postMuteSetting(@Header("Authorization") String paramString, @Field("add_user_ids[]") List<Long> paramList1, @Field("delete_user_ids[]") List<Long> paramList2, @Field("add_tags[]") List<String> paramList3, @Field("delete_tags[]") List<String> paramList4);

    @GET("/v1/user/detail?filter=for_ios")
    Observable<UserDetailResponse> getUserDetail(@Header("Authorization") String paramString, @Query("user_id") long id);

    @GET("/v1/user/detail?filter=for_ios")
    Observable<ResponseBody> getUserDetailBody(@Header("Authorization") String paramString, @Query("user_id") long id);

    @GET("/v1/user/illusts?filter=for_ios")
    Observable<UserIllustsResponse> getUserIllusts(@Header("Authorization") String paramString1, @Query("user_id") long paramLong, @Query("type") String paramString2);
    @GET("/v1/user/illusts?filter=for_ios")
    Deferred<UserIllustsResponse> getUserIllustsCor(@Header("Authorization") String paramString1, @Query("user_id") long paramLong, @Query("type") String paramString2);
    @GET
    Observable<UserIllustsResponse> getNextUserIllusts(@Header("Authorization") String paramString1, @Url String paramString2);
    @GET
   Deferred<UserIllustsResponse> getNextUserIllustsCor(@Header("Authorization") String paramString1, @Url String paramString2);
    @GET
    Call<UserIllustsResponse> getNextUserIllustsBlock(@Header("Authorization") String paramString1, @Url String paramString2);

    @GET
    Observable<SearchUserResponse> getNextUser(@Header("Authorization") String paramString1, @Url String paramString2);

    @GET
    Observable<ResponseBody> getImageBitmap(@Url String paramString2);

    void getNexttags();
}
