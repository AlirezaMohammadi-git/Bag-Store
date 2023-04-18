package com.example.bagstore.Model.Net

import com.example.bagstore.Model.Data.LoginResponse
import com.example.bagstore.Model.Local.TokenInMemory
import com.example.bagstore.Utils.BASE_URL
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("signUp")
    suspend fun signUp(@Body jsonObject: JsonObject): LoginResponse

    @POST("signIn")
    suspend fun signIn(@Body jsonObject: JsonObject): LoginResponse

    @GET("refreshToken") //token send on header
    fun refreshToken() : Call<LoginResponse>


}

fun createApiService(): ApiService {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val oldRequest = it.request()
            val newRequest = oldRequest.newBuilder()
            if (TokenInMemory.token != null)
                newRequest.addHeader("Authorization", TokenInMemory.token!!)
            newRequest.addHeader("Accept", "application/json")
            newRequest.method(oldRequest.method(), oldRequest.body())
            return@addInterceptor it.proceed(newRequest.build())
        }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)

}