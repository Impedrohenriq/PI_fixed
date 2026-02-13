package com.huntermobile.app.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.huntermobile.app.R
import com.huntermobile.app.data.HunterRepository
import com.huntermobile.app.databinding.ActivitySettingsBinding
import com.huntermobile.app.ui.common.enableBackNavigation
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val repository = HunterRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.enableBackNavigation(this)
        binding.buttonUpdate.setOnClickListener { updateUser() }
        binding.buttonDeleteAccount.setOnClickListener { confirmAccountDeletion() }
    }

    private fun updateUser() {
        val name = binding.editName.text?.toString()?.trim().takeIf { it?.isNotEmpty() == true }
        val email = binding.editEmail.text?.toString()?.trim().takeIf { it?.isNotEmpty() == true }
        val password = binding.editPassword.text?.toString()?.trim().takeIf { it?.isNotEmpty() == true }

        if (name == null && email == null && password == null) {
            showToast(getString(R.string.message_required_fields))
            return
        }

        lifecycleScope.launch {
            setUpdateLoading(true)
            val result = repository.updateUser(name, email, password)
            result.fold(
                onSuccess = { message ->
                    showToast(message)
                    binding.editPassword.text = null
                },
                onFailure = { error ->
                    showToast(error.message ?: getString(R.string.message_generic_error))
                }
            )
            setUpdateLoading(false)
        }
    }

    private fun confirmAccountDeletion() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.settings_button_delete)
            .setMessage(R.string.settings_confirm_delete)
            .setPositiveButton(R.string.settings_action_delete) { _, _ ->
                deleteAccount()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun deleteAccount() {
        lifecycleScope.launch {
            setDeleteLoading(true)
            val result = repository.deleteUser()
            result.fold(
                onSuccess = { message ->
                    showToast(message)
                    finish()
                },
                onFailure = { error ->
                    showToast(error.message ?: getString(R.string.message_generic_error))
                }
            )
            setDeleteLoading(false)
        }
    }

    private fun setUpdateLoading(isLoading: Boolean) {
        binding.progressUpdate.isVisible = isLoading
        binding.buttonUpdate.isEnabled = !isLoading
    }

    private fun setDeleteLoading(isLoading: Boolean) {
        binding.progressDelete.isVisible = isLoading
        binding.buttonDeleteAccount.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
