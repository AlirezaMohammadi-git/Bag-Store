package com.example.bagstore.ui.Features.SignUp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bagstore.Model.Repository.UserRepo.UserRepository
import com.example.bagstore.Utils.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignUpViewModel(val userRepository: UserRepository) : ViewModel() {

    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPass = mutableStateOf("")
    val emailF = mutableStateOf(false)
    val passF = mutableStateOf(false)
    val confPassF = mutableStateOf(false)
    val passVisibility = mutableStateOf(false)
    val confirmPassVisibility = mutableStateOf(false)

    fun signUp(signUpEvent: (String) -> Unit) {
        viewModelScope.launch (coroutineExceptionHandler) {
            signUpEvent(userRepository.signUp(name.value, email.value, password.value))
        }
    }

}