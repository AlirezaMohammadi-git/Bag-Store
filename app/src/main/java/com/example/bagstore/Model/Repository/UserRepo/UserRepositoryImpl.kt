package com.example.bagstore.Model.Repository.UserRepo

import android.content.SharedPreferences
import com.example.bagstore.Model.Local.TokenInMemory
import com.example.bagstore.Model.Net.ApiService
import com.example.bagstore.Utils.NO_ADDRESS
import com.example.bagstore.Utils.NO_POSTALCODE
import com.example.bagstore.Utils.SUCCESS_VALUE
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.Date

class UserRepositoryImpl(
    val apiService: ApiService,
    val sharePref: SharedPreferences
) : UserRepository {
    override suspend fun signUp(name: String, email: String, password: String): String {
        val json = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", email)
            addProperty("password", password)
        }
        val respons = apiService.signUp(json)
        return if (respons.success) {
            saveToken(respons.token)
            loadTokenFromSharePref()
            SUCCESS_VALUE
        } else {
            respons.message
        }
    }
    override suspend fun signIn(email: String, password: String): String {
        val json = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }
        val respons = apiService.signIn(json)
        return if (respons.success) {
            saveToken(respons.token)
            loadTokenFromSharePref()
            SUCCESS_VALUE
        } else {
            respons.message
        }
    }
    override fun saveToken(token: String) {
        sharePref.edit().putString("token", token).apply()
    }
    override fun loadTokenFromSharePref() {
        TokenInMemory.refreshToken(sharePref.getString("token", null))
    }
    override fun signOut() {
        sharePref.edit().clear().apply()
        TokenInMemory.refreshToken(null)
    }
    override fun saveAddress(address: String) {
        sharePref.edit().apply {
            putString("address", address)
        }.apply()
    }
    override fun savePostalCode(postalCode: String) {
        sharePref.edit().apply {
            putString("postalCode", postalCode)
        }.apply()
    }
    override fun getUserAddress(): String {
        return sharePref.getString("address", NO_ADDRESS)!!
    }
    override fun getUserPostalCode(): String {
        return sharePref.getString("postalCode", NO_POSTALCODE)!!
    }
    override fun saveEmail(email: String) {
        sharePref.edit().putString("UserEmail", email).apply()
    }
    override fun getEmail(): String {
        return sharePref.getString("UserEmail", "No Email")!!
    }
    override fun saveLoginTime(time: Long) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd - hh:mm:ss")
        val formatedTime = simpleDateFormat.format(Date(time))



        sharePref.edit().putString("LoginTime", formatedTime.toString()).apply()
    }
    override fun getLoginTime(): String {
        return sharePref.getString("LoginTime", "0")!!
    }
}