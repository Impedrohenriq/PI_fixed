package com.huntermobile.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huntermobile.app.data.HunterRepository
import com.huntermobile.app.data.Product
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: HunterRepository = HunterRepository()
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>(emptyList())
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentQuery: String? = null

    fun search(query: String) {
        if (query.isBlank()) {
            _products.value = emptyList()
            _error.value = null
            currentQuery = null
            return
        }

        currentQuery = query
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            runCatching { repository.searchProducts(query) }
                .onSuccess {
                    _products.value = it
                    _isLoading.value = false
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Erro desconhecido"
                    _products.value = emptyList()
                    _isLoading.value = false
                }
        }
    }

    fun refresh() {
        currentQuery?.let { search(it) }
    }
}
