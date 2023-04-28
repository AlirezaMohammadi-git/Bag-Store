package com.example.bagstore.Model.Repository.UserRepo

interface UserRepository {

    suspend fun signUp(name: String, email: String, password: String): String

    suspend fun signIn(email: String, password: String): String

    fun saveToken(token: String)

    fun loadTokenFromSharePref()

    fun signOut()


}