package com.example.bagstore.ui.Features.CartScreen

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.R
import com.example.bagstore.Utils.NO_ADDRESS
import com.example.bagstore.Utils.NO_POSTALCODE
import com.example.bagstore.Utils.Screens
import com.example.bagstore.Utils.TAG
import com.example.bagstore.Utils.stylePrice
import com.example.bagstore.ui.Features.ProductScreen.DotsTyping
import com.example.bagstore.ui.Features.ProfileScreen.ChangLocationDialog
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Composable
fun CardScreen() {
    val navigation = getNavController()
    val viewModel = getViewModel<CardViewModel>()
    val showPurchaseDialog = remember {
        mutableStateOf(false)
    }
    val userAddress = remember { mutableStateOf("") }
    val userPostalCode = remember { mutableStateOf("") }
    val dialogChecked = remember { mutableStateOf(false) }
    val context = LocalContext.current
    viewModel.loadData()
    Box(modifier = Modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp)
        ) {
            CardScreenToolbar(
                OnBackClicked = {
                    navigation.popBackStack()
                },
                OnProfileClicked = {
                    navigation.navigate(Screens.ProfileScreen.rout)
                }
            )
            if (viewModel.products.value.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.large,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "TOTAL : ${stylePrice(viewModel.totalPrice.value.toString())}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Justify
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(viewModel.products.value.size) {
                        CardItems(
                            product = viewModel.products.value[it],
                            OnCardClicked = { PID ->
                                navigation.navigate(Screens.ProductScreen.rout + "/" + PID)
                            },
                            isPending = viewModel.isPending.value,
                            OnAddProduct = { PID ->
                                viewModel.addToCart(PID)
                            },
                            OnRemoveProduct = { PID ->
                                viewModel.removeFromCart(PID)
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(150.dp))
                    }
                }
            } else {
                Animation()
            }
        }
        //<editor-fold desc="FAB">
        if (viewModel.products.value.isNotEmpty()) {
            FloatingActionButton(
                onClick = {
                    if (viewModel.userAddress.value == NO_ADDRESS && viewModel.userPostalCode.value == NO_POSTALCODE) {
                        showPurchaseDialog.value = true
                    } else {
                        viewModel.purchaseAll(
                            viewModel.userAddress.value, viewModel.userPostalCode.value
                        ) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            context.startActivity(intent)
                        }
                    }
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
                            text = "Purchase",
                            modifier = Modifier
                                .padding(end = 8.dp, start = 10.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
        //</editor-fold>
    }
    if (showPurchaseDialog.value) ChangLocationDialog(
        OnDismiss = { showPurchaseDialog.value = false },
        onCheckedChange = { isChecked, address, postalCode ->
            dialogChecked.value = isChecked
            userAddress.value = address
            userPostalCode.value = postalCode
        },
        OnConfirm = {
            userAddress.value = it.first
            userPostalCode.value = it.second
            if (
                userAddress.value.isNotBlank() &&
                userAddress.value.isNotEmpty() &&
                userPostalCode.value.isNotEmpty() &&
                userAddress.value.isNotBlank()
            ) {
                if (dialogChecked.value) {
                    viewModel.saveLocation(it.first, it.second)
                }
                viewModel.purchaseAll(
                    viewModel.userAddress.value,
                    viewModel.userPostalCode.value
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    context.startActivity(intent)
                    showPurchaseDialog.value = false
                }
            }else{
                Toast.makeText(context, "invalid location!", Toast.LENGTH_SHORT).show()
            }
        },
        addToProfile = true,
        checked = dialogChecked.value
    )
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++   CardItems  +++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItems(
    product: Product,
    OnCardClicked: (String) -> Unit,
    isPending: Pair<String, Boolean>,
    OnAddProduct: (String) -> Unit,
    OnRemoveProduct: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        onClick = { OnCardClicked.invoke(product.productId) }
    ) {
        Column {
            AsyncImage(
                model = product.imgUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(250.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row {
                    Text(text = "Name : ", fontWeight = FontWeight.Bold)
                    Text(text = product.name, fontWeight = FontWeight.Light)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(text = "Group : ", fontWeight = FontWeight.Bold)
                    Text(text = product.category, fontWeight = FontWeight.Light)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Product authenticity guarantee")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Available in stock to ship")
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1F),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(
                            text = "total : ${
                                stylePrice(
                                    (
                                            product.price.toInt() *
                                                    (product.quantity ?: "1").toInt()
                                            ).toString()
                                )
                            }",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Card(
                        modifier = Modifier,
                        border = BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                OnRemoveProduct.invoke(product.productId)
                            }) {
                                if (product.quantity == "1") Icon(
                                    Icons.Default.Delete,
                                    null
                                ) else Icon(
                                    painter = painterResource(id = R.drawable.ic_minus),
                                    null
                                )
                            }
                            if (isPending.first == product.productId && isPending.second)
                                CartDotsTyping() else
                                Text(
                                    text = product.quantity ?: "1",
                                    modifier = Modifier
                                )
                            IconButton(onClick = {
                                OnAddProduct.invoke(product.productId)
                            }) {
                                Icon(Icons.Default.Add, null)
                            }
                        }

                    }

                }

            }

        }
    }
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++   CardScreenToolbar  +++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreenToolbar(
    OnBackClicked: () -> Unit,
    OnProfileClicked: () -> Unit,
) {

    CenterAlignedTopAppBar(
        title = {
            Text(text = "My Cart")
        },
        navigationIcon = {
            IconButton(onClick = { OnBackClicked.invoke() }) {
                Icon(Icons.Default.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = { OnProfileClicked.invoke() }) {
                Icon(Icons.Default.Person, null)
            }
        }
    )


}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++   CartDotsTyping  ++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun CartDotsTyping() {

    val dotSize = 5.dp
    val delayUnit = 120
    val maxOffset = 20f

    @Composable
    fun Dot(
        offset: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp
        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   Animation  +++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="Animation">
@Composable
fun Animation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.no_data)
    )
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
}

//</editor-fold>
