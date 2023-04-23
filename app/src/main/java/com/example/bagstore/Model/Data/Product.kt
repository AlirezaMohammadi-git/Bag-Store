package com.example.bagstore.Model.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bagstore.Utils.PRODUCT_TABLE_NAME


@Entity(tableName = PRODUCT_TABLE_NAME)
data class Product(
    val category: String,
    val detailText: String,
    val imgUrl: String,
    val material: String,
    val name: String,
    val price: String,
    @PrimaryKey
    val productId: String,
    val soldItem: String,
    val tags: String
)