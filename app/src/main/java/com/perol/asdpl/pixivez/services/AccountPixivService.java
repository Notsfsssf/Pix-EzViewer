package com.perol.asdpl.pixivez.services;



import com.perol.asdpl.pixivez.responses.PixivAccountsEditResponse;
import com.perol.asdpl.pixivez.responses.PixivAccountsResponse;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Notsfsssf on 2018/3/24.
 */

public interface AccountPixivService {
    @FormUrlEncoded
    @POST("/api/provisional-accounts/create")
    Observable<PixivAccountsResponse> createProvisionalAccount(@Field("user_name") String paramString1, @Field("ref") String paramString2, @Header("Authorization") String paramString3);

    @FormUrlEncoded
    @POST("/api/account/edit")
    Observable<PixivAccountsEditResponse> editAccount(@Field("new_mail_address") String paramString1, @Field("new_user_account") String paramString2, @Field("current_password") String paramString3, @Field("new_password") String paramString4, @Header("Authorization") String paramString5);
}
