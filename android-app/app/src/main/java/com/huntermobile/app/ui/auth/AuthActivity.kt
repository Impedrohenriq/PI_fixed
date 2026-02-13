package com.huntermobile.app.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.huntermobile.app.R
import com.huntermobile.app.data.HunterRepository
import com.huntermobile.app.databinding.ActivityAuthBinding
import com.huntermobile.app.ui.common.enableBackNavigation
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val repository = HunterRepository()
    private var isLoginMode: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.enableBackNavigation(this)
        binding.buttonSubmit.setOnClickListener { submit() }
        binding.buttonToggleMode.setOnClickListener { toggleMode() }

        updateMode()
    }

    private fun toggleMode() {
        isLoginMode = !isLoginMode
        updateMode()
    }

    private fun updateMode() {
        binding.inputName.isVisible = !isLoginMode
        binding.buttonSubmit.text = if (isLoginMode) {
            getString(R.string.auth_button_login)
        } else {
            getString(R.string.auth_button_register)
        }
        binding.textMode.text = if (isLoginMode) {
            getString(R.string.auth_login_description)
        } else {
            getString(R.string.auth_register_description)
        }
        binding.buttonToggleMode.text = if (isLoginMode) {
            getString(R.string.auth_toggle_register)
        } else {
            getString(R.string.auth_toggle_login)
        }
        binding.inputName.editText?.text = null
        binding.editPassword.text = null
    }

    private fun submit() {
        val email = binding.editEmail.text?.toString()?.trim().orEmpty()
        val password = binding.editPassword.text?.toString()?.trim().orEmpty()

        if (email.isBlank() || password.isBlank()) {
            showToast(getString(R.string.message_required_fields))
            return
        }

        if (isLoginMode) {
            performLogin(email, password)
        } else {
            val name = binding.editName.text?.toString()?.trim().orEmpty()
            if (name.isBlank()) {
                showToast(getString(R.string.message_required_fields))
                return
            }
            performRegister(name, email, password)
        }
    }

    private fun performLogin(email: String, password: String) {
        lifecycleScope.launch {
            setLoading(true)
            val result = repository.login(email, password)
            result.fold(
                onSuccess = {
                    showToast(getString(R.string.auth_message_login_success))
                    setResult(RESULT_OK)
                    finish()
                },
                onFailure = { error ->
                    showToast(error.message ?: getString(R.string.message_generic_error))
                }
            )
            setLoading(false)
        }
    }

    private fun performRegister(name: String, email: String, password: String) {
        lifecycleScope.launch {
            setLoading(true)
            val result = repository.register(name, email, password)
            result.fold(
                onSuccess = { message ->
                    showToast(message)
                    isLoginMode = true
                    updateMode()
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
        binding.buttonSubmit.isEnabled = !isLoading
        binding.buttonToggleMode.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
