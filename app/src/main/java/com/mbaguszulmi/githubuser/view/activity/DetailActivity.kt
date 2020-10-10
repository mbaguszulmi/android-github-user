package com.mbaguszulmi.githubuser.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.model.database.entities.GithubUser
import com.mbaguszulmi.githubuser.model.database.entities.getUrl
import com.mbaguszulmi.githubuser.view.adapter.DetailFollowAdapter
import com.mbaguszulmi.githubuser.view.fragment.FollowersFragment
import com.mbaguszulmi.githubuser.view.fragment.FollowingFragment
import com.mbaguszulmi.githubuser.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.math.round

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_GITHUB_USER = "__extra_github_user__"
    }

    private lateinit var user: GithubUser
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        user = intent.getParcelableExtra(EXTRA_GITHUB_USER) as GithubUser
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.setUser(user)

        initView()
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

    private fun getCompactNumber(n: Int) : String {
        var value = n.toDouble()
        var suffix = ""
        if (value >= 1000000) {
            value /= 1000000
            suffix = "m"
        }
        else if (value >= 1000) {
            value /= 1000
            suffix = "k"
        }
        value = value.round(1)
        if ((value - value.toInt()) == 0.0) return value.toInt().toString() + suffix
        return value.toString() + suffix
    }

    private fun initView() {

        // ActionBar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = user.name

        val username = user.username as String
        mainViewModel.fetchUser(username)

        srl_detail.setOnRefreshListener {
            mainViewModel.fetchUser(username)
        }

        mainViewModel.getUserLiveData().observe(this, Observer {
            supportActionBar?.title = it.name

            tv_full_name.text = it.name
            tv_username.text = getString(R.string.username_content, username)
            tv_bio.text = it.bio

            tv_repo_count.text = getCompactNumber(it.repoCount)
            tv_followers.text = getCompactNumber(it.followers)
            tv_following.text = getCompactNumber(it.following)

            Glide.with(this)
                .load(it.avatarUrl)
                .into(iv_avatar)
        })

        mainViewModel.isLoadingDetailLiveData().observe(this, Observer {
            srl_detail.isRefreshing = it
        })

        btn_visit.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(user.getUrl())
            startActivity(browserIntent)
        }

        @StringRes
        val tabTitles = intArrayOf(R.string.tab_followers, R.string.tab_following)
        val detailFollowAdapter = DetailFollowAdapter(this, this,
            arrayOf(FollowersFragment.newInstance(username), FollowingFragment.newInstance(username)))
        vp_detail_main.adapter = detailFollowAdapter

        TabLayoutMediator(tl_detail_main, vp_detail_main) {
            tab, position ->
            tab.text = getString(tabTitles[position])
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}