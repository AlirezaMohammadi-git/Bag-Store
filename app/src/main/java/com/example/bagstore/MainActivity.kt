package com.example.bagstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bagstore.Model.Local.TokenInMemory
import com.example.bagstore.Model.Repository.UserRepo.UserRepository
import com.example.bagstore.Utils.Screens
import com.example.bagstore.di.MyModules
import com.example.bagstore.ui.Features.IntroScreen.InteroScreen
import com.example.bagstore.ui.Features.MainScreen.MainScreenUI
import com.example.bagstore.ui.Features.SignIn.SignInUI
import com.example.bagstore.ui.Features.SignUp.SignUpUI
import com.example.bagstore.ui.theme.BagStoreTheme
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Koin(
                appDeclaration = {
                    androidContext(this@MainActivity)
                    modules(MyModules)
                }) {
                BagStoreTheme() {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    private fun MainScreen() {
        val navigation = rememberNavController()
        KoinNavHost(navController = navigation, startDestination = Screens.Intro.rout, builder = {
            composable(route = Screens.Intro.rout) {
                val userRepository: UserRepository = get()
                userRepository.loadTokenFromSharePref()
                if (TokenInMemory.token == null) InteroScreen() else MainScreenUI()
            }
            composable(route = Screens.SignIn.rout) {
                SignInUI()
            }
            composable(route = Screens.SignUp.rout) {
                SignUpUI()
            }
            composable(Screens.MainScreen.rout) {
                MainScreenUI()
            }
        })
    }
}

