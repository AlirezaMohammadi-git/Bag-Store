package com.example.bagstore.ui.Features.SignIn

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagstore.Model.Repository.UserRepo.UserRepository
import com.example.bagstore.Utils.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignInViewModel( val userRepository: UserRepository ) : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val emailF = mutableStateOf(false)
    val passF = mutableStateOf(false)
    val passVisibility = mutableStateOf(false)

    fun signIn( signInEvent : (String) -> Unit ) {
        viewModelScope.launch (coroutineExceptionHandler) {
            signInEvent( userRepository.signIn( email.value , password.value ) )
            userRepository.saveEmail(email.value)
            userRepository.saveLoginTime(System.currentTimeMillis())
        }
    }


}