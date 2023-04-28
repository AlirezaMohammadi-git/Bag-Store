package com.example.bagstore.ui.Features.SignIn

import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bagstore.R
import com.example.bagstore.Utils.PASSWORD_CHAR_LIMIT
import com.example.bagstore.Utils.SUCCESS_VALUE
import com.example.bagstore.Utils.Screens
import com.example.bagstore.ui.Features.SignUp.EmailTF
import com.example.bagstore.ui.Features.SignUp.PasswordTF
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Composable
fun SignInUI() {
    val sys = rememberSystemUiController()
    sys.setStatusBarColor(MaterialTheme.colorScheme.primary)
    val context = LocalContext.current
    val viewModel = getViewModel<SignInViewModel>()
    val navigation = getNavController()
    MainCard(viewModel) {
        viewModel.signIn {
            if (SUCCESS_VALUE == it) {
                navigation.navigate(route = Screens.MainScreen.rout) {
                    popUpTo(route = Screens.Intro.rout) {
                        inclusive = true
                    }
                }
            } else Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun MainCard(viewModel: SignInViewModel, signInEvent: () -> Unit) {
    val navigation = getNavController()
    val email = remember { viewModel.email }
    val password = remember { viewModel.password }
    val emailF = remember { viewModel.emailF }
    val passF = remember { viewModel.passF }
    val passVisibility = remember { viewModel.passVisibility }
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .background(MaterialTheme.colorScheme.primary),
            ) {
// colored box for background
            }
            Column(
                modifier = Modifier
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .padding(horizontal = 16.dp)
                        .padding(top = 160.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(),
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
                            },
                            enabled = true
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
                            onClick = {
                                signInEvent.invoke()
                            },
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
                                navigation.navigate(route = Screens.SignUp.rout) {
                                    popUpTo(route = Screens.SignIn.rout) {
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
        }
    }
}