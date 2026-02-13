package com.huntermobile.app.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductListResponse(
    val produtos: List<ProductDto>
)

@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: String? = null,
    val nome: String,
    val preco: Double,
    val link: String,
    @Json(name = "imagem_url") val imagemUrl: String? = null,
    val origem: String? = null
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val nome: String,
    val email: String,
    val senha: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String
)

@JsonClass(generateAdapter = true)
data class ApiMessageResponse(
    val message: String? = null,
    val detail: String? = null
)

@JsonClass(generateAdapter = true)
data class AlertRequest(
    val produto: String,
    val preco: Double
)

@JsonClass(generateAdapter = true)
data class FeedbackRequest(
    val nome: String,
    val email: String,
    val feedback: String
)

@JsonClass(generateAdapter = true)
data class UserUpdateRequest(
    val nome: String? = null,
    val email: String? = null,
    val senha: String? = null
)
