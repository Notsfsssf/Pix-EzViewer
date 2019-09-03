package com.perol.asdpl.pixivez.services


import com.perol.asdpl.pixivez.responses.PixivAccountsEditResponse
import com.perol.asdpl.pixivez.responses.PixivAccountsResponse


import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Notsfsssf on 2018/3/24.
 */

interface AccountPixivService {
    @FormUrlEncoded
    @POST("/api/provisional-accounts/create")
    fun createProvisionalAccount(@Field("user_name") paramString1: String, @Field("ref") paramString2: String, @Header("Authorization") paramString3: String): Observable<PixivAccountsResponse>

    @FormUrlEncoded
    @POST("/api/account/edit")
    fun editAccount(@Field("new_mail_address") paramString1: String, @Field("new_user_account") paramString2: String, @Field("current_password") paramString3: String, @Field("new_password") paramString4: String, @Header("Authorization") paramString5: String): Observable<PixivAccountsEditResponse>
}
