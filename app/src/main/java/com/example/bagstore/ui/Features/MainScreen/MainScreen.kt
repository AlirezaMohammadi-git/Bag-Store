package com.example.bagstore.ui.Features.MainScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bagstore.Model.Data.Ad
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Utils.CATEGORY
import com.example.bagstore.Utils.NetworkChecker
import com.example.bagstore.Utils.Screens
import com.example.bagstore.Utils.TAGS
import com.example.bagstore.Utils.TOP_BAR_TITLE
import com.example.bagstore.ui.theme.CardViewBackground
import com.example.bagstore.ui.theme.CategoriesBackground
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import ir.dunijet.dunibazaar.ui.theme.Shapes
import org.koin.core.parameter.parametersOf

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++   MainScreen  ++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun MainScreenUI() {
    val context: Context = LocalContext.current
    val viewModel = getViewModel<MainScreenViewModel>(parameters = {
        parametersOf(
            NetworkChecker(
                context
            ).isInternetConnected
        )
    })
    val scrollState = rememberScrollState()
    val sys = rememberSystemUiController()
    sys.setStatusBarColor(MaterialTheme.colorScheme.background)
    val navigation = getNavController()

    Column(
        modifier = Modifier
    ) {
        if (viewModel.showProgressBar.value) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(8.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp, top = 8.dp)
        ) {
            TopBar(
                onCardClicked = {
                    navigation.navigate(Screens.CardScreen.rout)
                },
                onPersonClicked = {
                    navigation.navigate(Screens.ProfileScreen.rout)
                }
            )
            Categories(CATEGORY) {
                navigation.navigate(Screens.CategoryScreen.rout + "/" + it)
            }


            val productsState = viewModel.products
            val ads = viewModel.ads
            if (!NetworkChecker(context).isInternetConnected) {
                Toast.makeText(context, "Can not  access to internet!", Toast.LENGTH_SHORT).show()
            }
            ProductBars(
                TAGS,
                productsState,
                ads ,
            onProductClicked = {
                navigation.navigate(Screens.ProductScreen.rout + "/" + it)
            },
            onAdClicked = {
                navigation.navigate(Screens.ProductScreen.rout + "/" + it)
            })
        }
    }
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   ProductBars  +++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun ProductBars(
    tags: List<String>,
    products: MutableState<List<Product>>,
    ads: MutableState<List<Ad>>,
    onProductClicked: (String) -> Unit,
    onAdClicked: (String) -> Unit,
) {
    val context: Context = LocalContext.current
    if (products.value.isNotEmpty()) {
        tags.forEachIndexed { index, s ->
            val filteredData = products.value.filter { product ->
                product.tags == tags[index]
            }
            ProductsSubject(s)
            ProductBar(filteredData.shuffled()) {
                onProductClicked(it)
            }
            if (ads.value.size >= 2 && (index == 1 || index == 2) && NetworkChecker(context).isInternetConnected) {
                AdSlide(ads.value[index - 1]){
                    onAdClicked.invoke(it)
                }
            }
        }
    } else if (!NetworkChecker(context).isInternetConnected && products.value.isNotEmpty()) {
        Toast.makeText(context, "No Internet...", Toast.LENGTH_SHORT).show()
        //show dialog
    }
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   TopBar  ++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onCardClicked: () -> Unit, onPersonClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = TOP_BAR_TITLE) },
        actions = {
            IconButton(
                onClick = { onCardClicked.invoke() },
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
            }
            IconButton(
                onClick = { onPersonClicked.invoke() },
            ) {
                Icon(Icons.Default.Person, contentDescription = null)
            }
        },
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   Categories  ++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun Categories(categories: List<Pair<String, Int>>, onCategoryClicked: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(end = 8.dp),
        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
    ) {
        items(count = categories.size) {
            CategoryItem(categories[it]) {
                onCategoryClicked.invoke(it)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItem(pair: Pair<String, Int>, onCategoryClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(end = 10.dp),
    ) {
        Surface(
            color = CategoriesBackground,
            shape = Shapes.small,
            onClick = { onCategoryClicked.invoke(pair.first) }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = pair.second),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                )
                Text(
                    text = pair.first,
                    modifier = Modifier.padding(2.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   ProductsSubject  +++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun ProductsSubject(sub: String) {
    Text(
        text = sub,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   ProductBar  ++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun ProductBar(products: List<Product>, onProductClicked: (String) -> Unit) {
    Column(
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(products.size) {
                ProductItem(products[it]) {
                    onProductClicked(it)
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(product: Product, onProductClicked: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
        onClick = { onProductClicked.invoke(product.productId) },
        colors = CardDefaults.cardColors(
            containerColor = CardViewBackground
        ),
    ) {
        AsyncImage(
            modifier = Modifier.size(200.dp),
            model = product.imgUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = product.price + "Tomans", fontSize = 15.sp, fontWeight = FontWeight.Light)
            Text(
                text = product.soldItem + "Sold",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }
    }

}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   AdSlide  +++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdSlide(ad: Ad , onAdClicked : (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(200.dp)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp), onClick = { onAdClicked.invoke(ad.productId) }
    ) {
        AsyncImage(
            model = ad.imageURL,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}




