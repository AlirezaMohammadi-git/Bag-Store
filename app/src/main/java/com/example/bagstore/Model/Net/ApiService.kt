package com.example.bagstore.Model.Net

import com.example.bagstore.Model.Data.AddNewCommentResponse
import com.example.bagstore.Model.Data.CartResponse
import com.example.bagstore.Model.Data.CommentResponse
import com.example.bagstore.Model.Data.LoginResponse
import com.example.bagstore.Model.Data.ProductRespons
import com.example.bagstore.Model.Data.RandomAdRespons
import com.example.bagstore.Model.Data.UserCartInfo
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
    fun refreshToken(): Call<LoginResponse>

    @GET("getSliderPics")
    suspend fun getRandomAds(): RandomAdRespons

    @GET("getProducts")
    suspend fun getProducts(): ProductRespons

    @POST("getComments")
    suspend fun getComments(@Body productId: JsonObject): CommentResponse

    @POST("addNewComment")
    suspend fun addNewComment(@Body newComment: JsonObject): AddNewCommentResponse //newComment -> product ID + comment text

    @POST("addToCart")
    suspend fun addToCart(@Body PID: JsonObject): CartResponse

    @GET("getUserCart")
    suspend fun getUserCart(): UserCartInfo
}

fun createApiService(): ApiService {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val oldRequest = it.request()
            val newRequest = oldRequest.newBuilder()
            if (TokenInMemory.token != null)
                newRequest.addHeader("Authorization", TokenInMemory.token!!)
            newRequest.addHeader("Accept", "application/json")//if server get other types of data
            newRequest.method(oldRequest.method, oldRequest.body)
            return@addInterceptor it.proceed(newRequest.build())
        }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)

}