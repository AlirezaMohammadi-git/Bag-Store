package com.example.bagstore.ui.Features.MainScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.bagstore.Model.Local.TokenInMemory

@Composable
fun MainScreenUI() {

    TokenInMemory.token?.let { Text(text = it) }

}