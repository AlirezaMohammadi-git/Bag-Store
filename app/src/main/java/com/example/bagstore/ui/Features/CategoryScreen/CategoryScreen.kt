package com.example.bagstore.ui.Features.CategoryScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bagstore.Model.Data.Product
import com.example.bagstore.Utils.Screens
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Composable
fun CategoryScreen(category: String) {
    val viewModel = getViewModel<CategoryViewModel>()
    viewModel.getDataByCategory(category)
    val navigation = getNavController()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopToolbar(category) {
            navigation.popBackStack()
        }
        val dataProduct = viewModel.products
        Column() {
            ProductsList(dataProduct.value){
                navigation.navigate( Screens.ProductScreen.rout + "/" + it )
            }
        }
    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++   ProductsList  ++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun ProductsList(products: List<Product> , onProductClicked : (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        items(products.size) {
            ProductItem(products[it]){
                onProductClicked.invoke(it)
            }
        }
    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++   ProductItem  +++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(product: Product , onCardClicked : (String) -> Unit ) {
    Card(
        onClick = { onCardClicked.invoke( product.productId ) },
        modifier = Modifier
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            AsyncImage(
                model = product.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
                ,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Top)
                ) {
                    Text(text = product.name , fontWeight = FontWeight.Bold)
                    Text(text = product.price + "Tomans" , style = MaterialTheme.typography.bodySmall)
                }
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier
                        .align( Alignment.Bottom )
                ) {
                    Text(
                        text = product.soldItem + "sold" ,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
    }

}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++   TopToolbar  ++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolbar(category: String, onBackClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = category) },
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        navigationIcon = {
            IconButton(onClick = { onBackClicked.invoke() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}


