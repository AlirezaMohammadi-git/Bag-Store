package com.example.bagstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bagstore.Utils.Screens
import com.example.bagstore.ui.Features.IntroScreen.InteroScreen
import com.example.bagstore.ui.Features.SignIn.SignInUI
import com.example.bagstore.ui.Features.SignUp.SignUpUI
import com.example.bagstore.ui.theme.BagStoreTheme
import dev.burnoo.cokoin.navigation.KoinNavHost
import dev.burnoo.cokoin.navigation.getNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BagStoreTheme() {
                MainScreen()
            }
        }
    }

    @Composable
    private fun MainScreen() {
        val navigation = rememberNavController()
        KoinNavHost(navController = navigation, startDestination = Screens.Intro.rout, builder = {
            composable( route = Screens.Intro.rout ){
                InteroScreen()
            }
            composable(route = Screens.SignIn.rout){
                SignInUI()
            }
            composable(route = Screens.SignUp.rout){
                SignUpUI()
            }
        })
    }
}

