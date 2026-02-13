package com.huntermobile.app.ui.feedback

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.huntermobile.app.R
import com.huntermobile.app.data.HunterRepository
import com.huntermobile.app.databinding.ActivityFeedbackBinding
import com.huntermobile.app.ui.common.enableBackNavigation
import kotlinx.coroutines.launch

class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding
    private val repository = HunterRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.enableBackNavigation(this)
        binding.buttonSendFeedback.setOnClickListener { sendFeedback() }
    }

    private fun sendFeedback() {
        val name = binding.editName.text?.toString()?.trim().orEmpty()
        val email = binding.editEmail.text?.toString()?.trim().orEmpty()
        val message = binding.editMessage.text?.toString()?.trim().orEmpty()

        if (name.isBlank() || email.isBlank() || message.isBlank()) {
            showToast(getString(R.string.message_required_fields))
            return
        }

        lifecycleScope.launch {
            setLoading(true)
            val result = repository.sendFeedback(name, email, message)
            result.fold(
                onSuccess = { response ->
                    showToast(response)
                    binding.editMessage.text = null
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
        binding.buttonSendFeedback.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
