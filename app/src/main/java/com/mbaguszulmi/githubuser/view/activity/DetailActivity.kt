package com.mbaguszulmi.githubuser.view.activity

import android.content.Intent
import android.icu.text.CompactDecimalFormat
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.model.GithubUser
import com.mbaguszulmi.githubuser.model.getUrl

import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.math.round

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_GITHUB_USER = "__extra_github_user__"
    }

    private lateinit var user: GithubUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        user = intent.getParcelableExtra(EXTRA_GITHUB_USER) as GithubUser

        initView()
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

    private fun getCompactNumber(n: Int) : String {
        var value = n.toDouble();
        var suffix = "";
        if (value >= 1000000) {
            value /= 1000000
            suffix = "m"
        }
        else if (value >= 1000) {
            value /= 1000
            suffix = "k"
        }
        value.round(2);
        val valueStr = value.toString() + suffix
        return valueStr
    }

    private fun initView() {

        // ActionBar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(user.name)


        // Set value
        tv_full_name.text = user.name
        val username = "@" + user.username
        tv_username.text = username
        tv_bio.text = user.bio

        tv_repo_count.text = getCompactNumber(user.repoCount)
        tv_followers.text = getCompactNumber(user.followers)
        tv_following.text = getCompactNumber(user.following)

        Glide.with(this)
            .load(user.avatarUrl)
            .into(iv_avatar)

        btn_visit.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.setData(Uri.parse(user.getUrl()))
            startActivity(browserIntent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}