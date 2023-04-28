package com.example.bagstore.ui.Features.MainScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagstore.Model.Data.Ad
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Model.Repository.CartRepo.CardRepository
import com.example.bagstore.Model.Repository.ProductRepo.ProductRepository
import com.example.bagstore.Utils.coroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainScreenViewModel(
    private val productRepository: ProductRepository,
    isInternetConnected: Boolean,
    private val cardRepository: CardRepository
) : ViewModel() {
    val products = mutableStateOf<List<Product>>(listOf())
    val ads = mutableStateOf<List<Ad>>(listOf())
    val showProgressBar = mutableStateOf(false)
    val budgetNumber = mutableStateOf(0)

    init {
        refreshDataFromNet(isInternetConnected)
    }

    private fun refreshDataFromNet(isInternetConnected: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {
                showProgressBar.value = true
                delay(1000)
                val newProduct = async {
                    productRepository.getAllProducts(isInternetConnected)
                }
                val newAds = async {
                    productRepository.getRandomAds(isInternetConnected)
                }
                refreshData(newProduct.await(), newAds.await())
                showProgressBar.value = false
        }
    }

    private fun refreshData(product: List<Product>, ads: List<Ad>) {
        this.products.value = product
        this.ads.value = ads
    }

     fun getBudgetNumber() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val userCard = cardRepository.getBudgetNumber()
            budgetNumber.value = userCard
        }
    }

}