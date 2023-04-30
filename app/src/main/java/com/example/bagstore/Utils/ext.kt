package com.example.bagstore.Utils

import android.util.Log
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineExceptionHandler


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   coroutineExceptionHandler  +++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
val coroutineExceptionHandler = CoroutineExceptionHandler { context , throwable ->
    Log.e(TAG.Error.tag, "coroutineError: $throwable ")
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++   stylePrice  ++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
fun stylePrice( price : String ) : String {
    return if ( price.length > 3 ){
        val reversedPrice = price.reversed()
        var newPrice = ""
        for ( i in reversedPrice.indices ){
            if (i % 3 == 0) newPrice += ","
            newPrice += reversedPrice[i].toString()
        }
        if (newPrice.first() == ','){
            newPrice = newPrice.substring(startIndex = 1)
        }
        newPrice.reversed() + "T"
    }else{
        price + "T"
    }
}
