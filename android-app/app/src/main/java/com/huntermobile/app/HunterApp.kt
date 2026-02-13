package com.huntermobile.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.huntermobile.app.core.AuthManager

class HunterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        AuthManager.init(this)
    }
}
