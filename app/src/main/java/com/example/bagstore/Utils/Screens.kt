package com.example.bagstore.Utils

sealed class Screens(  val rout : String) {
    object Intro : Screens("Intro")
    object SignUp : Screens("SignUp")
    object SignIn : Screens("SignIn")
    object MainScreen : Screens("MainScreen")
    object ProductScreen : Screens("ProductScreen")
    object CardScreen : Screens("CardScreen")
    object ProfileScreen : Screens("ProfileScreen")
    object CategoryScreen : Screens("CategoryScreen")
}
