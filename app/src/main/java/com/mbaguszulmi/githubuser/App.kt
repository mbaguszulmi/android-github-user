package com.mbaguszulmi.githubuser

import android.app.Application
import com.mbaguszulmi.githubuser.model.database.AppDb

class App: Application() {
    companion object {
        lateinit var instance: App
        lateinit var appDb: AppDb
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        appDb = AppDb.getInstance(this)
    }
}