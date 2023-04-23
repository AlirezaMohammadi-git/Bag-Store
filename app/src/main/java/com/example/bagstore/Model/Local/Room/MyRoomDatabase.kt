package com.example.bagstore.Model.Local.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Model.Data.Ad
import com.example.bagstore.Utils.DATABASE_VERSION

@Database(
    entities = [ Product::class , Ad::class ] ,
    version = DATABASE_VERSION,
    exportSchema = false,
)
abstract class MyRoomDatabase : RoomDatabase(){
    abstract fun roomDao() : RoomDao
}