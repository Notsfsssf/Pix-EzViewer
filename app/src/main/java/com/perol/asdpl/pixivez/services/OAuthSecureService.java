package com.perol.asdpl.pixivez.services;

import com.perol.asdpl.pixivez.responses.PixivOAuthResponse;

import java.util.Map;


import javax.xml.transform.Result;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by asdpl on 2018/2/10.
 */

public interface OAuthSecureService {
    @FormUrlEncoded
    @POST("/auth/token")
    Observable<PixivOAuthResponse> postAuthToken(@FieldMap Map<String, Object> map);
    @FormUrlEncoded
    @POST("/auth/token")
    Call<ResponseBody> postAuthTokenx(@FieldMap Map<String, Object> map);
    @FormUrlEncoded
    @POST("/auth/token")
    Call<PixivOAuthResponse> postAuthTokenSync(@Field("client_id") String paramString1, @Field("client_secret") String paramString2, @Field("grant_type") String paramString3, @Field("username") String paramString4, @Field("password") String paramString5, @Field("device_token") String paramString6, @Field("get_secure_url") boolean paramBoolean);

    @FormUrlEncoded
    @POST("/auth/token")
    Observable<PixivOAuthResponse> postRefreshAuthToken(@Field("client_id") String paramString1, @Field("client_secret") String paramString2, @Field("grant_type") String paramString3, @Field("refresh_token") String paramString4, @Field("device_token") String paramString5,@Field("get_secure_url") boolean paramBoolean);
}
