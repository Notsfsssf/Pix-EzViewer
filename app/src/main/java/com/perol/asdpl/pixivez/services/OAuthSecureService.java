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

package com.perol.asdpl.pixivez.services;

import com.perol.asdpl.pixivez.responses.PixivOAuthResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


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
    Observable<PixivOAuthResponse> postRefreshAuthToken(@Field("client_id") String paramString1, @Field("client_secret") String paramString2, @Field("grant_type") String paramString3, @Field("refresh_token") String paramString4, @Field("device_token") String paramString5, @Field("get_secure_url") boolean paramBoolean);
}
