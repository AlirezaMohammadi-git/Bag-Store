package com.example.bagstore.di

import android.content.Context
import com.example.bagstore.Model.Net.createApiService
import com.example.bagstore.Model.Repository.UserRepo.UserRepository
import com.example.bagstore.Model.Repository.UserRepo.UserRepositoryImpl
import com.example.bagstore.ui.Features.SignIn.SignInViewModel
import com.example.bagstore.ui.Features.SignUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MyModules = module {

    single { createApiService() }
    single { androidContext().getSharedPreferences("tokenData", Context.MODE_PRIVATE) }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }

}