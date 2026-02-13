package com.huntermobile.app.network

import com.huntermobile.app.network.model.AlertRequest
import com.huntermobile.app.network.model.ApiMessageResponse
import com.huntermobile.app.network.model.FeedbackRequest
import com.huntermobile.app.network.model.LoginResponse
import com.huntermobile.app.network.model.ProductListResponse
import com.huntermobile.app.network.model.RegisterRequest
import com.huntermobile.app.network.model.UserUpdateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface HunterApiService {

    @GET("buscar-produtos")
    suspend fun searchProducts(
        @Query("nome") query: String
    ): ProductListResponse

    @POST("cadastrar")
    suspend fun register(
        @Body request: RegisterRequest
    ): ApiMessageResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("alerta-preco")
    suspend fun createAlert(
        @Header("Authorization") token: String,
        @Body request: AlertRequest
    ): ApiMessageResponse

    @POST("feedback")
    suspend fun sendFeedback(
        @Header("Authorization") token: String,
        @Body request: FeedbackRequest
    ): ApiMessageResponse

    @PUT("usuario")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body request: UserUpdateRequest
    ): ApiMessageResponse

    @DELETE("usuario")
    suspend fun deleteUser(
        @Header("Authorization") token: String
    ): ApiMessageResponse
}
