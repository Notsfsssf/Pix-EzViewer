package com.perol.asdpl.pixivez.services

import com.perol.asdpl.pixivez.responses.SearchUserResponse
import okhttp3.Callback
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("/v1/user/recommended?filter=for_android")
    suspend fun getUserRecommended(): SearchUserResponse


}