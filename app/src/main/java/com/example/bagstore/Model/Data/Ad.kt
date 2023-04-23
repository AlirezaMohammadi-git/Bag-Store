package com.example.bagstore.Model.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bagstore.Utils.ADS_TABLE_NAME

@Entity(ADS_TABLE_NAME)
data class Ad(
    @PrimaryKey
    val adId: String,
    val imageURL: String,
    val productId: String
)