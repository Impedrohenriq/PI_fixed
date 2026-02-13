package com.huntermobile.app.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.huntermobile.app.R
import com.huntermobile.app.databinding.ActivityMainBinding
import com.huntermobile.app.core.AuthManager
import com.huntermobile.app.ui.alerts.AlertsActivity
import com.huntermobile.app.ui.auth.AuthActivity
import com.huntermobile.app.ui.feedback.FeedbackActivity
import com.huntermobile.app.ui.home.HomeActivity
import com.huntermobile.app.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), ProductAdapter.OnProductClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = ProductAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecycler()
        setupSwipeRefresh()
        setupSearch()
        setupNavigation()
        observeViewModel()
    }

    private fun setupRecycler() {
        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerProducts.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupSearch() {
        binding.buttonSearch.setOnClickListener {
            performSearch()
        }

        binding.editSearch.setOnEditorActionListener { _, actionId, event ->
            val isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            if (isSearchAction) {
                performSearch()
                true
            } else {
                false
            }
        }
    }

    private fun setupNavigation() {
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.navSearch.isEnabled = false

        binding.navAlerts.setOnClickListener { navigateOrAuth(AlertsActivity::class.java) }
        binding.navFeedback.setOnClickListener { navigateOrAuth(FeedbackActivity::class.java) }
        binding.navSettings.setOnClickListener { navigateOrAuth(SettingsActivity::class.java) }

        updateAuthState()
    }

    private fun updateAuthState() {
        if (AuthManager.token.isNullOrBlank()) {
            binding.navLogin.text = getString(R.string.nav_login)
            binding.navLogin.setOnClickListener {
                startActivity(Intent(this, AuthActivity::class.java))
            }
        } else {
            binding.navLogin.text = getString(R.string.nav_logout)
            binding.navLogin.setOnClickListener {
                AuthManager.clear()
                showToast(getString(R.string.message_logged_out))
                updateAuthState()
            }
        }
    }

    private fun navigateOrAuth(destination: Class<*>) {
        if (AuthManager.token.isNullOrBlank()) {
            startActivity(Intent(this, AuthActivity::class.java))
        } else {
            startActivity(Intent(this, destination))
        }
    }

    private fun performSearch() {
        val query = binding.editSearch.text?.toString()?.trim().orEmpty()
        updateCategoryImage(query)
        hideKeyboard()
        if (query.isNotEmpty()) {
            viewModel.search(query)
        } else {
            viewModel.search("")
        }
    }

    private fun updateCategoryImage(searchTerm: String) {
        val normalized = searchTerm.lowercase()
        val (drawableRes, contentDescription) = when {
            "mouse" in normalized -> R.drawable.ic_category_mouse to "Mouse"
            "teclado" in normalized -> R.drawable.ic_category_keyboard to "Teclado"
            "monitor" in normalized -> R.drawable.ic_category_monitor to "Monitor"
            "pendrive" in normalized || "usb" in normalized -> R.drawable.ic_category_usb to "Pendrive"
            else -> null
        } ?: run {
            binding.imageCategory.isVisible = false
            return
        }

        binding.imageCategory.setImageResource(drawableRes)
        binding.imageCategory.contentDescription = contentDescription
        binding.imageCategory.isVisible = true
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.editSearch.windowToken, 0)
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        viewModel.products.observe(this) { products ->
            adapter.submitList(products)
            binding.emptyState.root.isVisible = products.isEmpty()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            val isSwipeRefreshing = binding.swipeRefresh.isRefreshing
            binding.progressBar.isVisible = isLoading && !isSwipeRefreshing
            if (!isLoading && isSwipeRefreshing) {
                binding.swipeRefresh.isRefreshing = false
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            binding.errorState.root.isVisible = errorMessage != null
            if (errorMessage != null) {
                binding.errorState.textError.text = errorMessage
            }
        }
    }

    override fun onProductClicked(link: String) {
        if (link.isNotBlank()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
    }
}
