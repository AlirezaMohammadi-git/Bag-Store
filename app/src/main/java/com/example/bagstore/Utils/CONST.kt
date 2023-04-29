package com.example.bagstore.Utils

import com.example.bagstore.Model.Data.Product
import com.example.bagstore.R

const val CATEGORY_KEY = "CATEGORY_KEY"
const val PRODUCT_KEY = "PRODUCT_KEY"
const val NAME_CHAR_LIMIT = 16
const val PASSWORD_CHAR_LIMIT = 8
const val BASE_URL = "https://dunijet.ir/Projects/DuniBazaar/"
const val SUCCESS_VALUE = "SUCCESS_VALUE"
const val PRODUCT_TABLE_NAME = "PRODUCT_TABLE"
const val ADS_TABLE_NAME = "ADS_TABLE"
const val DATABASE_NAME = "room-database.db"
const val DATABASE_VERSION = 1
const val TOP_BAR_TITLE = "Bag Store"
val CATEGORY = listOf(
    Pair("Backpack", R.drawable.ic_cat_backpack),
    Pair("Handbag", R.drawable.ic_cat_handbag),
    Pair("Shopping", R.drawable.ic_cat_shopping),
    Pair("Tote", R.drawable.ic_cat_tote),
    Pair("Satchel", R.drawable.ic_cat_satchel),
    Pair("Clutch", R.drawable.ic_cat_clutch),
    Pair("Wallet", R.drawable.ic_cat_wallet),
    Pair("Sling", R.drawable.ic_cat_sling),
    Pair("Bucket", R.drawable.ic_cat_bucket),
    Pair("Quilted", R.drawable.ic_cat_quilted)
)
val TAGS = listOf(
    "Newest",
    "Best Sellers",
    "Most Visited",
    "Highest Quality"
)
val EMPTY_PRODUCT = Product( "", "", "", "", "", "", "", "","","")
sealed class TAG(val tag : String) {
    object Error : TAG( "ERROR" )
    object Info : TAG( "INFO" )
    object Warning : TAG( "WARNING" )
}
const val NO_ADDRESS = "No Address"
const val NO_POSTALCODE = "No Postal Code"
const val PAYMENT_SUCCESS = 1
const val PAYMENT_PENDING = 0
const val PAYMENT_FAIL = -1
const val NO_PAYMENT = -2