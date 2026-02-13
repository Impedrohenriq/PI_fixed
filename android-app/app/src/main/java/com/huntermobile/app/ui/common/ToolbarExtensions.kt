package com.huntermobile.app.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.huntermobile.app.R

fun MaterialToolbar.enableBackNavigation(activity: AppCompatActivity) {
    navigationIcon = AppCompatResources.getDrawable(context, androidx.appcompat.R.drawable.abc_ic_ab_back_material)?.apply {
        setTint(ContextCompat.getColor(context, R.color.secondaryColor))
    }
    setNavigationOnClickListener {
        activity.onBackPressedDispatcher.onBackPressed()
    }
}
