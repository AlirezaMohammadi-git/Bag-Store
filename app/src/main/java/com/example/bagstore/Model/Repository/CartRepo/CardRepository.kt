package com.example.bagstore.Model.Repository.CartRepo

import com.example.bagstore.Model.Data.CheckOut
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Model.Data.SubmitOrder

interface CardRepository {
    suspend fun addToCard(PID: String): Boolean
    suspend fun removeFromCart(PID: String): Boolean
    suspend fun getBudgetNumber(): Int
    suspend fun getCartProduct(): List<Product>
    suspend fun getTotalPrice(): Int
    suspend fun checkOrderStatus(orderId: String): CheckOut
    suspend fun submitOrder(address: String, postalCode: String): String
    fun saveOrderId( orderId: String )
    fun getOrderId() : String
    fun savePurchaseStatus( status : Int )
    fun getPurchaseStatus() : Int

}