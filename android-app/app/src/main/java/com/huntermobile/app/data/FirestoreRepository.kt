package com.huntermobile.app.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun fetchProducts(): List<Product> = withContext(Dispatchers.IO) {
        val kabumTask = firestore.collection("produtos_kabum").get()
        val mlTask = firestore.collection("produtos_mercadolivre").get()

        val kabumSnapshot = kabumTask.awaitSafely()
        val mlSnapshot = mlTask.awaitSafely()

        val kabumProducts = kabumSnapshot?.toProductList("Kabum") ?: emptyList()
        val mlProducts = mlSnapshot?.toProductList("Mercado Livre") ?: emptyList()

        (kabumProducts + mlProducts).sortedBy { it.nome.lowercase() }
    }

    private suspend fun <T> com.google.android.gms.tasks.Task<T>.awaitSafely(): T? {
        return try {
            await()
        } catch (ex: Exception) {
            null
        }
    }

    private fun QuerySnapshot.toProductList(origem: String): List<Product> {
        return documents.mapNotNull { doc ->
            val nome = doc.getString("nome") ?: return@mapNotNull null
            val preco = doc.getDouble("preco") ?: 0.0
            val link = doc.getString("link") ?: ""
            val imagemUrl = doc.getString("imagem_url")

            Product(
                id = doc.id,
                nome = nome,
                preco = preco,
                link = link,
                imagemUrl = imagemUrl,
                origem = origem
            )
        }
    }
}
