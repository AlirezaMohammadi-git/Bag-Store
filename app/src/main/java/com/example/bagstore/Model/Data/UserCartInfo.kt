package com.example.bagstore.Model.Data

data class UserCartInfo(
    val success: Boolean,
    val productList: List<Product>,
    val message: String,
    val totalPrice: Int
)