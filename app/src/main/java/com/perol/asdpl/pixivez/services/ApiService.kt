package com.perol.asdpl.pixivez.services

import com.perol.asdpl.pixivez.responses.SearchUserResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/v1/user/recommended?filter=for_android")
    suspend fun getUserRecommended(): SearchUserResponse
}