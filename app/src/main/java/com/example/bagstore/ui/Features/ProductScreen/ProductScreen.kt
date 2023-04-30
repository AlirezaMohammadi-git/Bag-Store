package com.example.bagstore.ui.Features.ProductScreen

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.R
import com.example.bagstore.Utils.NetworkChecker
import com.example.bagstore.Utils.Screens
import com.example.bagstore.Utils.stylePrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   ProductScreen  +++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="ProductScreen">
@Composable
fun ProductScreen(productId: String) {
    //<editor-fold desc="Variables">
    val navigation = getNavController()
    val context = LocalContext.current
    val isInternetConnected = NetworkChecker(context).isInternetConnected
    val viewModel = getViewModel<ProductScreenViewModel>()
    val showCommentDialog = remember { mutableStateOf(false) }
    val showAddProductResultDialog = remember { mutableStateOf(false) }
    viewModel.loadData(productId, isInternetConnected)
    val product = viewModel.thisProduct

    //</editor-fold>
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            //<editor-fold desc="Topbar">
            TopToolbar(productName = product.value.name,
                onBackClicked = { navigation.popBackStack() },
                badgeNumber = viewModel.budgetNumber.value,
                onCardClicked = {
                    navigation.navigate(Screens.CardScreen.rout)
                }
            )
            //</editor-fold>
            //<editor-fold desc="Product detail">
            ProductDetails(product.value) {
                navigation.navigate(Screens.CategoryScreen.rout + "/" + it)
            }
            //</editor-fold>
            if (isInternetConnected) {
                val comments = viewModel.comments
                //<editor-fold desc="Product Header">
                ProductCommentHeader(
                    comments.value.size.toString(),
                    product.value.material,
                    product.value.soldItem,
                    product.value.tags
                ) {
                    showCommentDialog.value = true
                }
                //</editor-fold>
                //<editor-fold desc="Comment body">
                for (i in comments.value.indices.reversed()) {
                    CommentBody(
                        userEmail = comments.value[i].userEmail,
                        text = comments.value[i].text
                    )
                }
                //</editor-fold>
            } else {
                Spacer(modifier = Modifier.height(200.dp))
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
        //<editor-fold desc="FAB">
        FloatingActionButton(
            onClick = {
                viewModel.addToCard(productId) { res ->
                    if (res) {
                        showAddProductResultDialog.value = true
                        viewModel.loadData(productId, isInternetConnected)
                    } else {
                        Toast.makeText(context, "Something went wrong !", Toast.LENGTH_SHORT).show()
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
                        text = "Add to card",
                        modifier = Modifier
                            .padding(end = 8.dp, start = 10.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }
        }
        //</editor-fold>
        if (showCommentDialog.value) {
            AddCommentDialog(
                OnDismiss = { showCommentDialog.value = false },
                Onconfirm = { comment ->
                    viewModel.addNewComment(
                        isInternetConnected,
                        product.value.productId,
                        comment
                    ) { res ->
                        Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                        showCommentDialog.value = false
                    }
                }
            )
        }
        if (showAddProductResultDialog.value) {
            SimpleDialog(
                text = "'${product.value.name}' added to cart successfully",
                Onconfirm = {
                    showAddProductResultDialog.value = false
                }
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
    productName: String, onBackClicked: () -> Unit, badgeNumber: Int, onCardClicked: () -> Unit
) {
    CenterAlignedTopAppBar(title = {
        Text(
            text = productName,
        )
    }, navigationIcon = {
        IconButton(onClick = { onBackClicked.invoke() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
    }, actions = {
        if (badgeNumber.equals(0)) {
            Icon(imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier
                    .clickable() {
                        onCardClicked.invoke()
                    }
                    .padding(end = 16.dp)
            )
        } else {
            BadgedBox(
                badge = { Badge { Text(text = badgeNumber.toString()) } },
                modifier = Modifier.padding(end = 20.dp)
            ) {
                Icon(imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.clickable() {
                        onCardClicked.invoke()
                    })
            }
        }
    })
}

//</editor-fold>
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   ProductDetails  ++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="ProductDetails">
@Composable
fun ProductDetails(product: Product, onCategoryClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        AsyncImage(
            model = product.imgUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(200.dp)
                .padding(top = 8.dp)
                .clip(shape = MaterialTheme.shapes.medium)
        )
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Price : ${stylePrice(product.price)}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )

                Text(
                    text = product.detailText,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextButton(
                    onClick = { onCategoryClicked.invoke(product.category) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "#${product.category}")
                }
            }
        }





        MyDivider()

    }
}

//</editor-fold>
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   ProductComments  +++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="Comment Header">
@Composable
fun ProductCommentHeader(
    commentNum: String,
    productMaterial: String,
    productSold: String,
    productTag: String,
    onAddCommentClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        // show comments count
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_details_comment),
                contentDescription = null
            )
            Text(
                text = "$commentNum Comments",
                modifier = Modifier.padding(start = 4.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        //show product material
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_details_material),
                contentDescription = null
            )
            Text(
                text = productMaterial,
                modifier = Modifier.padding(start = 4.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        //show number of sold
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_details_sold),
                    contentDescription = null
                )
                Text(
                    text = "$productSold sold",
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = productTag,
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        MyDivider()
        //showing comments
        if (commentNum.toInt() <= 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                TextButton(
                    onClick = {
                        onAddCommentClicked.invoke()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "add comment",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = {
                    onAddCommentClicked.invoke()
                }) {
                    Text(
                        text = "add comment",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

//</editor-fold>
//<editor-fold desc="Comment body">
@Composable
fun CommentBody(userEmail: String, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = userEmail,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Light
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

}

//</editor-fold>
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++   MyDivider  +++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="divider">
@Composable
fun MyDivider() {
    Divider(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp),
        thickness = DividerDefaults.Thickness,
        color = DividerDefaults.color
    )
}

//</editor-fold>
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++   AddCommentDialog  +++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//<editor-fold desc="AddCommentDialog">
@Composable
fun AddCommentDialog(
    OnDismiss: () -> Unit,
    Onconfirm: (String) -> Unit,
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
            shape = RoundedCornerShape(corner = CornerSize(25.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add Comment",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                DialogTF(
                    commentValue = userComment,
                    onValueChange = { userComment.value = it },
                    hint = "Comment..."
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
                            Onconfirm.invoke(userComment.value)
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
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++   SimpleDialog  +++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun SimpleDialog(
    text: String,
    Onconfirm: () -> Unit,
) {
    Dialog(
        onDismissRequest = { Onconfirm.invoke() },
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
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineSmall ,
                    textAlign = TextAlign.Center ,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(modifier = Modifier.align(Alignment.BottomCenter)) {
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
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++   DotsTyping  +++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun DotsTyping() {

    val dotSize = 10.dp
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
                color = Color.White,
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