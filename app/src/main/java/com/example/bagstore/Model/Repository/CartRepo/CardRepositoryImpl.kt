package com.example.bagstore.Model.Repository.CartRepo

import android.util.Log
import com.example.bagstore.Model.Data.UserCartInfo
import com.example.bagstore.Model.Net.ApiService
import com.example.bagstore.Utils.TAG
import com.google.gson.JsonObject

class CardRepositoryImpl(
    private val apiService: ApiService,
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

    override suspend fun getBudgetNumber(): Int {
        val userCart = apiService.getUserCart()
        var counter = 0
        if ( userCart.success ){
            userCart.productList.forEach {
                counter += (it.quantity ?: "0").toInt()
            }
        }
        return counter
    }


}