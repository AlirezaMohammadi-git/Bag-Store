package com.example.bagstore.Model.Repository.UserRepo

interface UserRepository {

    suspend fun signUp(name: String, email: String, password: String): String

    suspend fun signIn(email: String, password: String): String

    fun saveToken(token: String)

    fun loadTokenFromSharePref()

    fun signOut()

    fun saveAddress(address: String)
    fun savePostalCode(postalCode: String)
    fun getUserAddress(): String
    fun getUserPostalCode(): String

    fun saveEmail(email: String)
    fun getEmail(): String

    fun saveLoginTime(time: Long)
    fun getLoginTime(): String


}