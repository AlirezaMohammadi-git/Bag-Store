package com.example.bagstore.ui.Features.ProfileScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagstore.Model.Data.Comment
import com.example.bagstore.Model.Repository.CartRepo.CardRepository
import com.example.bagstore.Model.Repository.CommentRepo.CommentRepository
import com.example.bagstore.Model.Repository.ProductRepo.ProductRepository
import com.example.bagstore.Model.Repository.UserRepo.UserRepository
import com.example.bagstore.Utils.EMPTY_PRODUCT
import com.example.bagstore.Utils.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Address

class ProfileScreenViewModel(
private val userRepository: UserRepository
) : ViewModel() {
    val showAnimation = mutableStateOf(false)
    val email = mutableStateOf("")
    val emailF = mutableStateOf(false)
    var address = mutableStateOf("")
    var postalCode = mutableStateOf("")
    var loginTime = mutableStateOf("")

    fun signOut(){
        viewModelScope.launch(coroutineExceptionHandler){
            showAnimation.value = true
            userRepository.signOut()
            delay(800)
            showAnimation.value = false
        }
    }
    fun loadUserData(){
        email.value = userRepository.getEmail()
        address.value = userRepository.getUserAddress()
        postalCode.value = userRepository.getUserPostalCode()
        loginTime.value = userRepository.getLoginTime()
    }
    fun saveLocation( address: String , postalCode : String ){
        userRepository.saveAddress(address)
        userRepository.savePostalCode(postalCode)
    }
}