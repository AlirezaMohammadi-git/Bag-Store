package com.example.bagstore.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.example.bagstore.Model.Local.Room.MyRoomDatabase
import com.example.bagstore.Model.Net.createApiService
import com.example.bagstore.Model.Repository.CartRepo.CardRepository
import com.example.bagstore.Model.Repository.CartRepo.CardRepositoryImpl
import com.example.bagstore.Model.Repository.CommentRepo.CommentRepository
import com.example.bagstore.Model.Repository.CommentRepo.CommentRepositoryImpl
import com.example.bagstore.Model.Repository.ProductRepo.ProductRepository
import com.example.bagstore.Model.Repository.ProductRepo.ProductRepositoryImpl
import com.example.bagstore.Model.Repository.UserRepo.UserRepository
import com.example.bagstore.Model.Repository.UserRepo.UserRepositoryImpl
import com.example.bagstore.Utils.DATABASE_NAME
import com.example.bagstore.ui.Features.CategoryScreen.CategoryViewModel
import com.example.bagstore.ui.Features.MainScreen.MainScreenViewModel
import com.example.bagstore.ui.Features.ProductScreen.ProductScreenViewModel
import com.example.bagstore.ui.Features.ProfileScreen.ProfileScreenViewModel
import com.example.bagstore.ui.Features.SignIn.SignInViewModel
import com.example.bagstore.ui.Features.SignUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val MyModules = module {

    single { createApiService() }
    single { androidContext().getSharedPreferences("tokenData", Context.MODE_PRIVATE) }
    single {
        Room.databaseBuilder(
            androidContext(),
            MyRoomDatabase::class.java,
            DATABASE_NAME
        )
            .build()
    }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ProductRepository> {
        ProductRepositoryImpl(
            apiService = get(),
            roomDao = get<MyRoomDatabase>().roomDao()
        )
    }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<CardRepository> { CardRepositoryImpl(get()) }

    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { (isNetConnected: Boolean) -> MainScreenViewModel(get(), isNetConnected , get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductScreenViewModel(get(), get(), get()) }
    viewModel { ProfileScreenViewModel(get()) }

}