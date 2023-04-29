package com.example.bagstore.Model.Repository.CartRepo

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.bagstore.Model.Data.CheckOut
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Model.Data.SubmitOrder
import com.example.bagstore.Model.Net.ApiService
import com.example.bagstore.Utils.TAG
import com.google.gson.JsonObject

class CardRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref: SharedPreferences
) : CardRepository {
    override suspend fun addToCard(PID: String): Boolean {
        val pid = JsonObject()
        pid.apply {
            addProperty("productId", PID)
        }
        val res = apiService.addToCart(pid)
        if (res.success) {
            return true
        } else {
            Log.e(TAG.Error.tag, "addToCard: ${res.message}")
            return false
        }
    }

    override suspend fun removeFromCart(PID: String): Boolean {
        val pid = JsonObject()
        pid.apply {
            addProperty("productId", PID)
        }
        val res = apiService.removeFromCart(pid)
        if (res.success) {
            return true
        } else {
            Log.e(TAG.Error.tag, "removeFromCart: ${res.message}")
            return false
        }
    }

    override suspend fun getBudgetNumber(): Int {
        val userCart = apiService.getUserCart()
        var counter = 0
        if (userCart.success) {
            userCart.productList.forEach {
                counter += (it.quantity ?: "0").toInt()
            }
        }
        return counter
    }

    override suspend fun getCartProduct(): List<Product> {
        val userCart = apiService.getUserCart()
        return if (userCart.success) userCart.productList else {
            Log.e(TAG.Error.tag, "getCartProduct: ${userCart.message}")
            listOf()
        }
    }

    override suspend fun getTotalPrice(): Int {
        val response = apiService.getUserCart()
        return if (response.success) response.totalPrice else {
            Log.e(TAG.Error.tag, "getTotalPrice: ${response.message}")
            0
        }
    }

    override suspend fun checkOrderStatus(orderId: String): CheckOut {
        val json = JsonObject()
        json.apply {
            addProperty("orderId", orderId)
        }
        val response = apiService.checkOut(json)
        return if (response.success == true) response else CheckOut(null, null)
    }

    override suspend fun submitOrder(address: String, postalCode: String): String {
        val json = JsonObject().apply {
            addProperty("address" , address)
            addProperty("postalCode" , postalCode)
        }
        val response = apiService.submitOrder( json)
        saveOrderId(response.orderId.toString())
        return if (response.success) response.paymentLink else {
            Log.e(TAG.Error.tag, "submitOrder: something went wrong", )
            "null"
        }
    }

    override fun saveOrderId(orderId: String) {
        sharedPref.edit().putString("orderId" , orderId).apply()
    }

    override fun getOrderId(): String {
        return sharedPref.getString("orderId" , "null")!!
    }

    override fun savePurchaseStatus(status: Int) {
        sharedPref.edit().putInt("PaymentStatus" , status ).apply()
    }

    override fun getPurchaseStatus(): Int {
        return sharedPref.getInt("PaymentStatus" , -2 )
    }
}