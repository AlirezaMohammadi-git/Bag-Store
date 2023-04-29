package com.example.bagstore.ui.Features.CartScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Model.Repository.CartRepo.CardRepository
import com.example.bagstore.Model.Repository.ProductRepo.ProductRepository
import com.example.bagstore.Model.Repository.UserRepo.UserRepository
import com.example.bagstore.Utils.TAG
import com.example.bagstore.Utils.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CardViewModel(
    private val cartRepository: CardRepository ,
    private val userRepository: UserRepository
    ) : ViewModel() {
    val products = mutableStateOf<List<Product>>(listOf())
    val totalPrice = mutableStateOf(0)
    val isPending = mutableStateOf(Pair< String , Boolean >("" , false ))
    private val waitingDelay = 400
    val userAddress = mutableStateOf("")
    val userPostalCode = mutableStateOf("")
    val showAnimation = mutableStateOf(false)


    fun loadData(){
        getProduct()
        getTotalPrice()
        getLocation()
    }
    private fun getProduct(){
       viewModelScope.launch(coroutineExceptionHandler){
           products.value = cartRepository.getCartProduct()
       }
    }
    private fun getTotalPrice(){
        viewModelScope.launch(coroutineExceptionHandler){
            totalPrice.value = cartRepository.getTotalPrice()
        }
    }
    fun addToCart(PID :String){
        viewModelScope.launch(coroutineExceptionHandler){
            isPending.value = isPending.value.copy( PID , true )
            val res = cartRepository.addToCard(PID)
            delay(timeMillis = waitingDelay.toLong() )
            isPending.value = isPending.value.copy( PID , false )
            if (res) loadData()
        }
    }
    fun removeFromCart(PID :String){
        viewModelScope.launch(coroutineExceptionHandler){
            isPending.value = isPending.value.copy( PID , true )
            val res = cartRepository.removeFromCart(PID)
            delay(timeMillis = waitingDelay.toLong())
            isPending.value = isPending.value.copy( PID , false )
            if (res) loadData()
        }
    }
    fun purchaseAll( address : String , postalCode :String , result:(String)-> Unit) {
        viewModelScope.launch(coroutineExceptionHandler){
            showAnimation.value = true
            val response = cartRepository.submitOrder( address , postalCode )
            delay(300)
            showAnimation.value = false
            result.invoke(response)
        }
    }
    private fun getLocation(){
        userAddress.value = userRepository.getUserAddress()
        userPostalCode.value = userRepository.getUserPostalCode()
    }
    fun saveLocation( address: String , postalCode: String ){
        userRepository.saveAddress(address)
        userRepository.savePostalCode(postalCode)
    }

}