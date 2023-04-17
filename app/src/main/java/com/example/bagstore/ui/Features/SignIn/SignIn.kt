package com.example.bagstore.ui.Features.SignIn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bagstore.R
import com.example.bagstore.Utils.PASSWORD_CHAR_LIMIT
import com.example.bagstore.Utils.Screens
import com.example.bagstore.ui.Features.SignUp.EmailTF
import com.example.bagstore.ui.Features.SignUp.PasswordTF
import com.example.bagstore.ui.theme.brown
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController

@Composable
fun SignInUI() {

    val navigation = getNavController()
    val sys = rememberSystemUiController()
    SideEffect {
        sys.setStatusBarColor(brown)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .background(color = brown),
    )

    MainCard()

}


@Composable
fun MainCard() {
    val navigation = getNavController()
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val emailF = remember { mutableStateOf(false) }
    val passF = remember { mutableStateOf(false) }
    val passVisibility = remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)
            .padding(horizontal = 16.dp)
            .padding(top = 80.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp),
                text = "Sign In",
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp
            )

            EmailTF(
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                placeHolder = { Text(text = "Email") },
                SupportText = { Text(text = "Please enter a valid email") },
                isFocused = emailF,
                EmptySupportText = { },
                leadingIC = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = null
                    )
                }
            )
            PasswordTF(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                placeHolder = { Text(text = "Password") },
                SupportText = { },
                passChar = PASSWORD_CHAR_LIMIT,
                isFocuse = passF,
                EmptySupportText = {},
                isError = false,
                leadingIC = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = null
                    )
                },
                trailingIC = {
                    val image =
                        if (passVisibility.value) R.drawable.ic_unlock_eye else R.drawable.ic_lock_eye
                    Icon(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { passVisibility.value = !passVisibility.value }
                    )
                },
                visualTransformation = if (passVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
            )

            Button(
                modifier = Modifier
                    .padding(top = 16.dp),
                onClick = { }
            ) {
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = "Sign In",
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Don't have an account?")
                TextButton(onClick = {
                    navigation.navigate( route = Screens.SignUp.rout ){
                        popUpTo( route = Screens.SignIn.rout ){
                            inclusive = true
                        }
                    }
                }) {
                    Text(text = "Sign Up")
                }
            }

        }
    }
}