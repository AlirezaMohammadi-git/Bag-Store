package com.example.bagstore.ui.Features.ProductScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagstore.Model.Data.Comment
import com.example.bagstore.Model.Repository.CartRepo.CardRepository
import com.example.bagstore.Model.Repository.CommentRepo.CommentRepository
import com.example.bagstore.Model.Repository.ProductRepo.ProductRepository
import com.example.bagstore.Utils.EMPTY_PRODUCT
import com.example.bagstore.Utils.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductScreenViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cardRepository: CardRepository
) : ViewModel() {

    val thisProduct = mutableStateOf(EMPTY_PRODUCT)
    val comments = mutableStateOf(listOf<Comment>())
    val budgetNumber = mutableStateOf(0)
    val showAnimation = mutableStateOf(false)

    fun loadData(PID: String, isInternetConnected: Boolean) {
        loadDataFromCache(PID)
        if (isInternetConnected){
            getProduct(PID)
            getComments(PID, isInternetConnected)
            getBudgetNumber()
        }
    }

    private fun getProduct(PID: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value = productRepository.getProductById(PID)
        }
    }

    private fun getComments(PID: String, isInternetConnected: Boolean) {
        if (isInternetConnected) {
            viewModelScope.launch(coroutineExceptionHandler) {
                comments.value = commentRepository.getAllComments(PID)
            }
        }
    }

    fun addNewComment(
        isInternetConnected: Boolean,
        PID: String,
        comment: String,
        success: (String) -> Unit
    ) {
        if (isInternetConnected) {
            viewModelScope.launch(coroutineExceptionHandler) {
                commentRepository.addNewComment(PID, comment) { result ->
                    success.invoke(result)
                }
                loadData(PID, true)
            }
        }
    }

    private fun getBudgetNumber() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val userCard = cardRepository.getBudgetNumber()
            budgetNumber.value = userCard
        }
    }
    fun addToCard(PID: String, result: (Boolean) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            showAnimation.value = true
            val response = cardRepository.addToCard(PID)
            delay(800)
            showAnimation.value = false
            result.invoke(response)
        }
    }

    private fun loadDataFromCache(PID: String ){
        getProduct( PID = PID )
    }
}