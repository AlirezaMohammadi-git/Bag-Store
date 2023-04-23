package com.example.bagstore.ui.Features.MainScreen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bagstore.R
import com.example.bagstore.Utils.NetworkChecker
import com.example.bagstore.Utils.TOP_BAR_TITLE
import com.example.bagstore.ui.theme.CardViewBackground
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
    SideEffect {
        sys.setStatusBarColor(Color.White)
    }
    Column(
        modifier = Modifier
    ) {
        TopBar()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp)
        ) {
            Categories()
            ProductsSubject()
            ProductBar()
            ProductsSubject()
            ProductBar()
            AdSlide()
            ProductsSubject()
            ProductBar()
            ProductsSubject()
            ProductBar()
            AdSlide()
        }
    }

}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   TopBar  ++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = TOP_BAR_TITLE) },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Person, contentDescription = null)
            }
        },
        modifier = Modifier.background(color = Color.White)
    )
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   Categories  ++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun Categories() {
    LazyRow(
        contentPadding = PaddingValues(end = 8.dp),
        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
    ) {
        items(count = 10) {
            CategoryItem()
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItem() {
    Column(
        modifier = Modifier
            .padding(end = 10.dp),
    ) {
        Surface(
            color = CardViewBackground,
            shape = Shapes.small
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cat_backpack),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                )
                Text(
                    text = "Hotels",
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
fun ProductsSubject() {
    Text(
        text = "Popular Products",
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   ProductBar  ++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@Composable
fun ProductBar() {
    Column(
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(10) {
                ProductItem()
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem() {
    Card(
        modifier = Modifier
            .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
        onClick = {},
        colors = CardDefaults.cardColors(
            containerColor = CardViewBackground
        )
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(
                id = R.drawable.intero2
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "ProductName", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = "56k Rial", fontSize = 15.sp, fontWeight = FontWeight.Light)
            Text(text = "32 sold", fontSize = 12.sp, fontWeight = FontWeight.ExtraLight)
        }
    }

}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   AdSlide  +++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdSlide() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(200.dp)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp), onClick = {}
    ) {
        Image(
            painter = painterResource(id = R.drawable.intero2),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}




