package com.huntermobile.app.data

import com.huntermobile.app.core.AuthManager
import com.huntermobile.app.network.ApiClient
import com.huntermobile.app.network.model.AlertRequest
import com.huntermobile.app.network.model.FeedbackRequest
import com.huntermobile.app.network.model.RegisterRequest
import com.huntermobile.app.network.model.UserUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HunterRepository {

    private val api = ApiClient.api

    suspend fun searchProducts(query: String): List<Product> = withContext(Dispatchers.IO) {
        val response = api.searchProducts(query)
        response.produtos.map { dto ->
            Product(
                id = dto.id ?: dto.link,
                nome = dto.nome,
                preco = dto.preco,
                link = dto.link,
                imagemUrl = dto.imagemUrl,
                origem = dto.origem ?: "Hunter"
            )
        }
    }

    suspend fun register(nome: String, email: String, senha: String): Result<String> = runCatching {
        val response = api.register(RegisterRequest(nome, email, senha))
        response.message ?: "Cadastro realizado com sucesso"
    }

    suspend fun login(email: String, senha: String): Result<Unit> = runCatching {
        val response = api.login(email, senha)
        AuthManager.token = "${response.tokenType} ${response.accessToken}"
    }

    suspend fun createAlert(produto: String, preco: Double): Result<String> = withAuthHeader { token ->
        val response = api.createAlert(token, AlertRequest(produto, preco))
        response.message ?: "Alerta criado com sucesso"
    }

    suspend fun sendFeedback(nome: String, email: String, feedback: String): Result<String> = withAuthHeader { token ->
        val response = api.sendFeedback(token, FeedbackRequest(nome, email, feedback))
        response.message ?: "Feedback enviado com sucesso"
    }

    suspend fun updateUser(nome: String?, email: String?, senha: String?): Result<String> = withAuthHeader { token ->
        val response = api.updateUser(token, UserUpdateRequest(nome, email, senha))
        response.message ?: "Dados atualizados com sucesso"
    }

    suspend fun deleteUser(): Result<String> = withAuthHeader { token ->
        val response = api.deleteUser(token)
        AuthManager.clear()
        response.message ?: "Conta deletada com sucesso"
    }

    private inline fun <T> withAuthHeader(block: (String) -> T): Result<T> {
        val token = AuthManager.token
        return if (token.isNullOrBlank()) {
            Result.failure(IllegalStateException("Usuário não autenticado."))
        } else {
            runCatching { block(token) }
        }
    }
}
