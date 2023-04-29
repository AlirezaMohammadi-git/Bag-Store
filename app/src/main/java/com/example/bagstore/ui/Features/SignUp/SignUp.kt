package com.example.bagstore.ui.Features.SignUp

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bagstore.R
import com.example.bagstore.Utils.NAME_CHAR_LIMIT
import com.example.bagstore.Utils.PASSWORD_CHAR_LIMIT
import com.example.bagstore.Utils.SUCCESS_VALUE
import com.example.bagstore.Utils.Screens
import com.example.bagstore.ui.theme.CardViewBackground
import com.example.bagstore.ui.theme.brown400
import com.example.bagstore.ui.theme.brown700
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Composable
fun SignUpUI() {
    val context = LocalContext.current
    val navigation = getNavController()
    val viewModel = getViewModel<SignUpViewModel>()
    val sys = rememberSystemUiController()
    sys.setStatusBarColor(MaterialTheme.colorScheme.primary)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .background(color = MaterialTheme.colorScheme.primary),
    )
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        MainCard(viewModel = viewModel, signUpEvent = {
            viewModel.signUp {
                if (it == SUCCESS_VALUE) {
                    navigation.navigate(route = Screens.MainScreen.rout) {
                        popUpTo(Screens.Intro.rout) {
                            inclusive = true
                        }
                    }
                } else {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }, navigation = navigation)
        Spacer(modifier = Modifier.height(150.dp))
    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   MainCard  ++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun MainCard(signUpEvent: () -> Unit, viewModel: SignUpViewModel, navigation: NavHostController) {
    val name = viewModel.name
    val email = viewModel.email
    val password = viewModel.password
    val confirmPass = viewModel.confirmPass
    val emailF = viewModel.emailF
    val passF = viewModel.passF
    val confPassF = viewModel.confPassF
    val passVisibility = viewModel.passVisibility
    val confirmPassVisibility = viewModel.confirmPassVisibility
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .padding(horizontal = 16.dp)
            .padding(top = 80.dp),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Sign Up",
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp
            )
            NameTF(
                value = name.value,
                onValueChange = {
                    name.value = it
                },
                placeHolder = { Text(text = "Name") },
                charLimit = NAME_CHAR_LIMIT,
                SupportText = { Text(text = "limit : ${name.value.length}/$NAME_CHAR_LIMIT") },
                EmptySupportText = { },
                leadingIC = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = null
                    )
                })
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
                SupportText = { Text(text = "${password.value.length}/$PASSWORD_CHAR_LIMIT ") },
                passChar = PASSWORD_CHAR_LIMIT,
                isFocuse = passF,
                EmptySupportText = {},
                isError = password.value != confirmPass.value,
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
                visualTransformation = if (passVisibility.value) VisualTransformation.None else PasswordVisualTransformation() ,
            )

            PasswordTF(
                value = confirmPass.value,
                onValueChange = {
                    confirmPass.value = it
                },
                placeHolder = { Text(text = "Confirm Password") },
                SupportText = { Text(text = "${confirmPass.value.length}/$PASSWORD_CHAR_LIMIT ") },
                passChar = PASSWORD_CHAR_LIMIT,
                isFocuse = confPassF,
                EmptySupportText = {},
                isError = password.value != confirmPass.value,
                leadingIC = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = null
                    )
                },
                trailingIC = {
                    val image =
                        if (confirmPassVisibility.value) R.drawable.ic_unlock_eye else R.drawable.ic_lock_eye
                    Icon(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                confirmPassVisibility.value = !confirmPassVisibility.value
                            }
                    )
                },
                visualTransformation = if (confirmPassVisibility.value) VisualTransformation.None else PasswordVisualTransformation() ,
            )
            Button(
                modifier = Modifier
                    .padding(4.dp),
                onClick = {
                    signUpEvent.invoke()
                },
            ) {
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = "Sign Up",
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Already have an account?")
                TextButton(onClick = {
                    navigation.navigate(route = Screens.SignIn.rout) {
                        popUpTo(route = Screens.SignUp.rout) {
                            inclusive = true
                        }
                    }
                }) {
                    Text(text = "Sign In")
                }
            }
        }
    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   NameTF  ++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameTF(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: @Composable () -> Unit,
    charLimit: Int,
    SupportText: @Composable () -> Unit,
    EmptySupportText: @Composable () -> Unit,
    leadingIC: @Composable () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeHolder,
        modifier = Modifier
            .padding(top = 16.dp),
        label = placeHolder,
        isError = value.length > charLimit,
        supportingText = if (value.length > charLimit) SupportText else EmptySupportText,
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        leadingIcon = leadingIC
    )
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   EmailTF  +++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTF(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: @Composable () -> Unit,
    SupportText: @Composable () -> Unit,
    EmptySupportText: @Composable () -> Unit,
    leadingIC: @Composable () -> Unit,
    isFocused: MutableState<Boolean>,
    enabled : Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(top = 16.dp)
            .onFocusChanged {
                isFocused.value = it.isFocused
            },
        placeholder = placeHolder,
        label = placeHolder,
        shape = MaterialTheme.shapes.large,
        isError = !Patterns.EMAIL_ADDRESS.matcher(value).matches() && isFocused.value,
        singleLine = false,
        supportingText = if (!Patterns.EMAIL_ADDRESS.matcher(value)
                .matches() && isFocused.value
        ) SupportText else EmptySupportText,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = leadingIC,
        enabled = enabled
    )
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   PasswordTF  ++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTF(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: @Composable () -> Unit,
    SupportText: @Composable () -> Unit,
    EmptySupportText: @Composable () -> Unit,
    passChar: Int,
    isFocuse: MutableState<Boolean>,
    isError: Boolean,
    leadingIC: @Composable () -> Unit,
    trailingIC: @Composable () -> Unit,
    visualTransformation: VisualTransformation,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(top = 16.dp)
            .onFocusChanged {
                isFocuse.value = it.isCaptured
            },
        placeholder = placeHolder,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        label = placeHolder,
        supportingText = if (value.length + 1 < passChar) SupportText else EmptySupportText,
        leadingIcon = leadingIC,
        trailingIcon = trailingIC,
        visualTransformation = visualTransformation,
        shape = MaterialTheme.shapes.large,

    )

}