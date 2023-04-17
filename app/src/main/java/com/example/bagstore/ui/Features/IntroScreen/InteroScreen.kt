package com.example.bagstore.ui.Features.IntroScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.bagstore.R
import com.example.bagstore.Utils.Screens
import com.example.bagstore.ui.theme.beige
import com.example.bagstore.ui.theme.brown
import com.example.bagstore.ui.theme.lightBlack
import com.example.bagstore.ui.theme.lighterBeige
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController


@Composable
fun InteroScreen() {

    val navigation = getNavController()
    val sys = rememberSystemUiController()
    SideEffect {
        sys.setStatusBarColor(Color.White)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.intero2),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                onClick = {
                    navigation.navigate(Screens.SignIn.rout)
                }
            ) {
                Text(text = "Sign In", fontWeight = FontWeight.Bold)
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(containerColor = beige),
                onClick = {
                    navigation.navigate(Screens.SignUp.rout)
                }) {
                Text(text = "Sign Up", color = lightBlack, fontWeight = FontWeight.Bold)
            }
        }

    }


}