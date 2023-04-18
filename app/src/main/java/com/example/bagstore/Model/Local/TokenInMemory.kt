package com.example.bagstore.Model.Local

object TokenInMemory {
    var token: String? = null
        private set

    fun refreshToken(token: String? ) {
        this.token = token
    }
}
