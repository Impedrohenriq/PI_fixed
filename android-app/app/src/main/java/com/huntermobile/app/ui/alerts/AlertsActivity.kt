package com.huntermobile.app.ui.alerts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.huntermobile.app.R
import com.huntermobile.app.data.HunterRepository
import com.huntermobile.app.databinding.ActivityAlertsBinding
import com.huntermobile.app.ui.common.enableBackNavigation
import kotlinx.coroutines.launch

class AlertsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlertsBinding
    private val repository = HunterRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.enableBackNavigation(this)
        binding.buttonCreateAlert.setOnClickListener { createAlert() }
    }

    private fun createAlert() {
        val product = binding.editProduct.text?.toString()?.trim().orEmpty()
        val desiredPriceText = binding.editPrice.text?.toString()?.trim().orEmpty()

        if (product.isBlank() || desiredPriceText.isBlank()) {
            showToast(getString(R.string.message_required_fields))
            return
        }

        val normalizedPrice = desiredPriceText.replace(',', '.')
        val desiredPrice = normalizedPrice.toDoubleOrNull()
        if (desiredPrice == null) {
            showToast(getString(R.string.message_invalid_price))
            return
        }

        lifecycleScope.launch {
            setLoading(true)
            val result = repository.createAlert(product, desiredPrice)
            result.fold(
                onSuccess = { message ->
                    showToast(message)
                    binding.editProduct.text = null
                    binding.editPrice.text = null
                },
                onFailure = { error ->
                    showToast(error.message ?: getString(R.string.message_generic_error))
                }
            )
            setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.buttonCreateAlert.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
