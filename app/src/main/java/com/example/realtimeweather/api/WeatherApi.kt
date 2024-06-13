package com.example.realtimeweather.api

import retrofit2.Response
import retrofit2.http.GET

interface WeatherApi {

    @GET("/v1/current.json")
    suspend fun getWeatherData(
        @retrofit2.http.Query("key") key: String,
        @retrofit2.http.Query("q") city: String
    ): Response<WeatherModel>
}