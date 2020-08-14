package com.mbaguszulmi.githubuser.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbaguszulmi.githubuser.R

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_GITHUB_USER = "__extra_github_user__"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}