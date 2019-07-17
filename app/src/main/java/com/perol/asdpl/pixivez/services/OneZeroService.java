package com.perol.asdpl.pixivez.services;

import com.perol.asdpl.pixivez.responses.OneZeroResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OneZeroService {
    @GET("dns-query")
    Call<OneZeroResponse> getItem(@Query(value = "ct",encoded = true) String ct,@Query("name") String name,@Query("type") String type,@Query("do") String doo,@Query("cd") String cd);
}
