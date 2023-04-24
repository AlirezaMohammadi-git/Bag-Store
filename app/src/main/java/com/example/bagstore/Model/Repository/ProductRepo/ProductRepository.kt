package com.example.bagstore.Model.Repository.ProductRepo

import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Model.Data.Ad

interface ProductRepository {
    suspend fun getAllProducts(isInternetConnected: Boolean): List<Product>
    suspend fun getRandomAds(isInternetConnected: Boolean): List<Ad>
    suspend fun getProductsByCategory( category : String ) : List<Product>

}