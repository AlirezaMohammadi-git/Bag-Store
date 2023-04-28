package com.example.bagstore.Model.Repository.CartRepo

interface CardRepository {
    suspend fun addToCard(PID: String) : Boolean
    suspend fun getBudgetNumber() : Int
}