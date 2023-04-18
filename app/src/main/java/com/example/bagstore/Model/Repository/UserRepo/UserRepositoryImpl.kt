package com.example.bagstore.Model.Repository.UserRepo

import android.content.SharedPreferences
import com.example.bagstore.Model.Local.TokenInMemory
import com.example.bagstore.Model.Net.ApiService
import com.example.bagstore.Utils.SUCCESS_VALUE
import com.google.gson.JsonObject

class UserRepositoryImpl(
    val apiService: ApiService,
    val sharePref: SharedPreferences
) : UserRepository {
    override suspend fun signUp(name: String, email: String, password: String) : String {
        val json = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", email)
            addProperty("password", password)
        }
        val respons = apiService.signUp(json)
        return if (respons.success){
            saveToken(respons.token)
            loadTokenFromSharePref()
            SUCCESS_VALUE
        }else{
            respons.message
        }
    }
    override suspend fun signIn(email: String, password: String) : String {
        val json = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }
        val respons = apiService.signIn(json)
        return if (respons.success){
            saveToken(respons.token)
            loadTokenFromSharePref()
            SUCCESS_VALUE
        }else{
            respons.message
        }
    }
    override fun saveToken(token: String) {
        sharePref.edit().putString("token", token).apply()
    }
    override fun loadTokenFromSharePref() {
        TokenInMemory.refreshToken( sharePref.getString("token" , null ) )
    }

    override fun signOut() {
        sharePref.edit().clear().apply()
        TokenInMemory.refreshToken(null)
    }
}