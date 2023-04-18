package com.example.bagstore.Model.Data

data class LoginResponse(
    val expiresAt: Int,
    val message: String,
    val success: Boolean,
    val token: String
)
