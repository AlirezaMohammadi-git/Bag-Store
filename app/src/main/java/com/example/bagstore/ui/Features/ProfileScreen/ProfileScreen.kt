package com.example.bagstore.ui.Features.ProfileScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bagstore.R
import com.example.bagstore.Utils.Screens
import com.example.bagstore.ui.Features.ProductScreen.DotsTyping
import com.example.bagstore.ui.Features.SignUp.EmailTF
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

//<editor-fold desc="ProfileScreen">
@Composable
fun ProfileScreen() {
    val navigation = getNavController()
    val viewModel = getViewModel<ProfileScreenViewModel>()
    val showConfirmExitDialog = remember { mutableStateOf(false) }
    val showEditLocationDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val dialogChecked = remember {
        mutableStateOf(false)
    }
    viewModel.loadUserData()
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TopToolbar(
                OnEditClicked = {
                    showEditLocationDialog.value = true
                },
                onBackClicked = {
                    navigation.popBackStack()
                })
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp), horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Animation()
                }
                EmailTF(
                    value = viewModel.email.value,
                    onValueChange = {

                    },
                    placeHolder = { Text(text = "Email") },
                    SupportText = { },
                    EmptySupportText = { },
                    leadingIC = {},
                    isFocused = viewModel.emailF,
                    enabled = false
                )
                AddressField(
                    value = viewModel.address.value,
                    onValueChange = {
                        viewModel.address.value = it
                    },
                    placeHolder = { Text(text = "Address") },
                    enabled = false,
                    number = true
                )
                AddressField(
                    value = viewModel.postalCode.value,
                    onValueChange = {
                        viewModel.postalCode.value = it
                    },
                    placeHolder = { Text(text = "Postal code") },
                    enabled = false,
                    number = true
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "login time:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                )
                Text(
                    text = viewModel.loginTime.value,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Light,
                )
                Spacer(modifier = Modifier.height(150.dp))

            }
        }
        //<editor-fold desc="FAB">
        FloatingActionButton(
            onClick = {
                showConfirmExitDialog.value = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (viewModel.showAnimation.value) {
                    DotsTyping()
                } else {
                    Text(
                        text = "Sign Out",
                        modifier = Modifier
                            .padding(end = 8.dp, start = 10.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        //</editor-fold>
        if (showConfirmExitDialog.value) {
            com.example.bagstore.ui.Features.ProfileScreen.SimpleDialog(
                text = "are you sure to sign out?",
                Onconfirm = {
                    viewModel.signOut()
                    navigation.navigate(Screens.Intro.rout) {
                        popUpTo(Screens.MainScreen.rout) {
                            inclusive = true
                        }
                    }
                },
                OnDismiss = {
                    showConfirmExitDialog.value = false
                })


        }
        if (showEditLocationDialog.value) {
            ChangLocationDialog(
                OnDismiss = {
                    showEditLocationDialog.value = false
                },
                OnConfirm = { info ->
                    if (
                        info.first.isNotEmpty() &&
                        info.first.isNotBlank() &&
                        info.second.isNotBlank() &&
                        info.second.isNotEmpty()
                    ) {
                        viewModel.saveLocation(info.first, info.second)
                        viewModel.loadUserData()
                        showEditLocationDialog.value = false
                    } else {
                        Toast.makeText(
                            context,
                            "Please use a valid location info",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } ,
                onCheckedChange = { _, _, _ ->
                } ,
                addToProfile = false ,
                checked = dialogChecked.value
            )
        }
    }
}

//</editor-fold>
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   TopToolbar  ++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="ToolBar">
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolbar(
    onBackClicked: () -> Unit,
    OnEditClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "Profile",
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClicked.invoke() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }, actions = {
            TextButton(onClick = {
                OnEditClicked.invoke()
            }) {
                Text(text = "Edit info")
            }
        }
    )
}

//</editor-fold>
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   Animation  +++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="Animation">
@Composable
fun Animation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.profile_anim)
    )
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .size(250.dp)
    )
}

//</editor-fold>
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   AddressField  ++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="AddressField">
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressField(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: @Composable () -> Unit,
    enabled: Boolean,
    number: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeHolder,
        modifier = Modifier
            .padding(top = 16.dp),
        label = placeHolder,
        shape = MaterialTheme.shapes.large,
        enabled = enabled,
        singleLine = false,
        keyboardOptions = if (number) KeyboardOptions(keyboardType = KeyboardType.Phone) else
            KeyboardOptions(keyboardType = KeyboardType.Text),
    )
}

//</editor-fold>
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   SimpleDialog  ++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="SimpleDialog">
@Composable
fun SimpleDialog(
    text: String,
    Onconfirm: () -> Unit,
    OnDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { OnDismiss.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            decorFitsSystemWindows = true
        )
    ) {
        val context = LocalContext.current
        val userComment = remember { mutableStateOf("") }
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(modifier = Modifier.align(Alignment.BottomEnd)) {
                        TextButton(onClick = {
                            OnDismiss.invoke()
                        }) {
                            Text(text = "CANCEL")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            Onconfirm.invoke()
                        }) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }

}

//</editor-fold>
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   ChangLocationDialog  +++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="ChangLocationDialog">
@Composable
fun ChangLocationDialog(
    OnDismiss: () -> Unit,
    onCheckedChange: (Boolean  , String , String ) -> Unit,
    OnConfirm: (Pair<String, String>) -> Unit,
    addToProfile: Boolean ,
    checked : Boolean
) {
    Dialog(
        onDismissRequest = { OnDismiss.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            decorFitsSystemWindows = true
        )
    ) {
        val context = LocalContext.current
        val address = remember { mutableStateOf("") }
        val postalCode = remember { mutableStateOf("") }
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "add location",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                DialogTF(
                    commentValue = address,
                    onValueChange = { address.value = it },
                    hint = "Address..."
                )
                Spacer(modifier = Modifier.height(8.dp))
                DialogTF(
                    commentValue = postalCode,
                    onValueChange = { postalCode.value = it },
                    hint = "postal code..."
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (addToProfile) {
                    Row(
                        horizontalArrangement = Arrangement.Start ,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = checked , onCheckedChange = {
                            onCheckedChange.invoke(it , address.value , postalCode.value)
                        })
                        Spacer(modifier = Modifier.width(1.dp))
                        Text(text = "add to profile")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(modifier = Modifier.align(Alignment.BottomEnd)) {
                        TextButton(onClick = {
                            OnDismiss.invoke()
                        }) {
                            Text(text = "CANCEL")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            OnConfirm.invoke(Pair(address.value, postalCode.value))
                        }) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }

}

//</editor-fold>
//<editor-fold desc="DialogTF">
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogTF(
    commentValue: MutableState<String>,
    onValueChange: (String) -> Unit,
    hint: String,
) {
    OutlinedTextField(
        value = commentValue.value,
        onValueChange = { onValueChange.invoke(it) },
        placeholder = {
            Text(text = hint)
        },
        shape = MaterialTheme.shapes.large
    )
}
//</editor-fold>