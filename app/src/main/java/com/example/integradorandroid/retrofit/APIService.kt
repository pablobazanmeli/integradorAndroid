package com.example.integradorandroid.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("activity")
    suspend fun getActivityCall(@Query("participants") participants: Int?, @Query("type") type:String?): Response<BoredData>

}