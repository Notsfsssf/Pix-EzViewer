package com.perol.asdpl.pixivez.services


import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SaucenaoService {
    @Multipart
    @POST("/search.php")
    fun searchpicforresult(@Part paramRequestBody: MultipartBody.Part): Observable<ResponseBody>
}
