package com.mbaguszulmi.githubuser

import android.app.Application
import android.util.Log
import com.mbaguszulmi.githubuser.model.database.AppDb

class App: Application() {
    companion object {
        lateinit var instance: App
        lateinit var appDb: AppDb
        private val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        appDb = AppDb.getInstance(this.applicationContext)
        Log.d(TAG, "onCreate Application Class")
    }
}