package com.huntermobile.app.data

data class Product(
    val id: String = "",
    val nome: String = "",
    val preco: Double = 0.0,
    val link: String = "",
    val imagemUrl: String? = null,
    val origem: String = ""
)
