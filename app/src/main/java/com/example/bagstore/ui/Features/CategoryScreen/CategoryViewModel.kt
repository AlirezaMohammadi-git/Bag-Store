package com.example.bagstore.ui.Features.CategoryScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Model.Repository.ProductRepo.ProductRepository
import com.example.bagstore.Utils.coroutineExceptionHandler
import kotlinx.coroutines.launch

class CategoryViewModel(private val productRepository: ProductRepository) : ViewModel() {
    val products = mutableStateOf<List<Product>>(listOf())
    fun getDataByCategory(category: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val data = productRepository.getProductsByCategory(category)
            products.value = data
        }
    }
}