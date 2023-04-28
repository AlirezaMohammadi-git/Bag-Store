package com.example.bagstore.Model.Repository.CommentRepo

import com.example.bagstore.Model.Data.Comment

interface CommentRepository {
    suspend fun getAllComments(productId: String): List<Comment>
    suspend fun addNewComment(productId: String, comment: String, success: (String) -> Unit )
}