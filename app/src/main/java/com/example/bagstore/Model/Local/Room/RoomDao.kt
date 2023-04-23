package com.example.bagstore.Model.Local.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Model.Data.Ad


@Dao
interface RoomDao  {
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertOrUpdateProduct(product : List<Product> )

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertOrUpdateAd(ad : List<Ad> )

    @Query( "SELECT * FROM PRODUCT_TABLE" )
    suspend fun getAllProducts() : List<Product>

    @Query( "SELECT * FROM PRODUCT_TABLE WHERE productId = :id" )
    suspend fun getProductById(id : Int ) : Product

    @Query( "SELECT * FROM ADS_TABLE" )
    suspend fun getAllAds() : List<Ad>

}