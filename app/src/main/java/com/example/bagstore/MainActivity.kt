package com.example.bagstore

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bagstore.Model.Local.TokenInMemory
import com.example.bagstore.Model.Repository.UserRepo.UserRepository
import com.example.bagstore.Utils.CATEGORY_KEY
import com.example.bagstore.Utils.PRODUCT_KEY
import com.example.bagstore.Utils.Screens
import com.example.bagstore.di.MyModules
import com.example.bagstore.ui.Features.CartScreen.CardScreen
import com.example.bagstore.ui.Features.CategoryScreen.CategoryScreen
import com.example.bagstore.ui.Features.IntroScreen.InteroScreen
import com.example.bagstore.ui.Features.MainScreen.MainScreenUI
import com.example.bagstore.ui.Features.ProductScreen.ProductScreen
import com.example.bagstore.ui.Features.ProfileScreen.ProfileScreen
import com.example.bagstore.ui.Features.SignIn.SignInUI
import com.example.bagstore.ui.Features.SignUp.SignUpUI
import com.example.bagstore.ui.theme.BagStoreTheme
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //force Right to left:
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        super.onCreate(savedInstanceState)
        setContent {
            Koin(
                appDeclaration = {
                    androidContext(this@MainActivity)
                    modules(MyModules)
                }) {
                BagStoreTheme() {
                    Surface(
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainScreen()
                    }
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
            composable(Screens.CardScreen.rout) {
                CardScreen()
            }
            composable(Screens.ProfileScreen.rout) {
                ProfileScreen()
            }
            composable(
                route = Screens.ProductScreen.rout + "/" + "{$PRODUCT_KEY}",
                arguments = listOf(navArgument(PRODUCT_KEY) {
                    type = NavType.StringType
                })
            ) {
                ProductScreen( it.arguments!!.getString(PRODUCT_KEY , "null") )
            }
            composable(
                route = Screens.CategoryScreen.rout + "/" + "{$CATEGORY_KEY}",
                arguments = listOf(navArgument(CATEGORY_KEY) {
                    type = NavType.StringType
                })
            ) {
                CategoryScreen(it.arguments!!.getString(CATEGORY_KEY, "null"))
            }
        })
    }
}

