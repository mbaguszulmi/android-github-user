package com.mbaguszulmi.githubuser.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.view.preferences.MainPreference

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initView()
    }

    private fun initView() {
        // App bar
        supportActionBar?.title = resources.getString(R.string.settings)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_settings_container, MainPreference())
            .commit()
    }
}