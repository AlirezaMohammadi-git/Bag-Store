package com.example.bagstore.Model.Data

data class SubmitOrder(
    val success: Boolean ,
    val orderId: Int,
    val paymentLink :String
)