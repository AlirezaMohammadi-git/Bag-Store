package com.example.bagstore.Model.Repository.ProductRepo

import android.util.Log
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Model.Data.Ad
import com.example.bagstore.Model.Local.Room.RoomDao
import com.example.bagstore.Model.Net.ApiService

class ProductRepositoryImpl(
    private val apiService: ApiService,
    private val roomDao: RoomDao
) : ProductRepository {
    override suspend fun getAllProducts(isInternetConnected: Boolean): List<Product> {
        if (isInternetConnected) {
            val dataFromServer = apiService.getProducts()
            if (dataFromServer.success) {
                roomDao.insertOrUpdateProduct(dataFromServer.products)
                return dataFromServer.products
            }
        } else {
            return roomDao.getAllProducts()
        }
        return listOf()//if none of above works
    }

    override suspend fun getRandomAds(isInternetConnected: Boolean): List<Ad> {
        if (isInternetConnected) {
            val dataFromServer = apiService.getRandomAds()
            if (dataFromServer.success) {
                roomDao.insertOrUpdateAd(dataFromServer.ads)
                return dataFromServer.ads
            }
        } else {
            return roomDao.getAllAds()
        }
        return listOf()//if none of above works
    }

    override suspend fun getProductsByCategory(category: String): List<Product> {
        Log.i("myTag", "getProductsByCategory: ${roomDao.getProductByCategory(category).size} ")
        return roomDao.getProductByCategory(category)
    }

}