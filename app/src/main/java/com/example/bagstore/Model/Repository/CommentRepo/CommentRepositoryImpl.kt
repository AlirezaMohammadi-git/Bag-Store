package com.example.bagstore.Model.Repository.CommentRepo

import com.example.bagstore.Model.Data.Comment
import com.example.bagstore.Model.Net.ApiService
import com.google.gson.JsonObject

class CommentRepositoryImpl(
    private val apiService: ApiService,
) : CommentRepository {
    override suspend fun getAllComments(
        productId: String)
    : List<Comment>
    {
        val data = JsonObject()
        data.apply {
            addProperty("productId", productId)
        }
        val response = apiService.getComments(data)
        if (response.success) {
            return response.comments
        } else {
            return listOf()
        }
    }
    override suspend fun addNewComment(
        productId: String,
        comment: String,
        success: (String) -> Unit
    ) {
        val newComment = JsonObject().apply {
            addProperty("productId", productId)
            addProperty("text", comment)
        }
        val response = apiService.addNewComment(newComment)
        if (response.success) {
            success.invoke(response.message)
        } else {
            success.invoke( "Something went wrong!" )
        }
    }
}