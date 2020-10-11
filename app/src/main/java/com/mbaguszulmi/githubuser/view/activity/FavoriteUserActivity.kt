package com.mbaguszulmi.githubuser.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.model.database.entities.GithubUser
import com.mbaguszulmi.githubuser.view.adapter.GithubUserListAdapter
import com.mbaguszulmi.githubuser.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_favorite_user.*

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var githubUserListAdapter: GithubUserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.init()

        initView()
    }

    private fun initView() {
        initAdapter()

        // ActionBar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_users)

        mainViewModel.setLoadingUsers(true)

        srl_fav.setOnRefreshListener {
            mainViewModel.loadUserAsync(this)
        }

        mainViewModel.registerFavoriteListWatcher(this)
        mainViewModel.loadUserAsync(this)

        mainViewModel.getUserListLiveData().observe(this, {
            githubUserListAdapter.updateData(it)

            if (it.size <= 0) {
                tv_no_user_favorite.visibility = View.VISIBLE
            }
            else {
                tv_no_user_favorite.visibility = View.GONE
            }
        })

        mainViewModel.isLoadingUsersLiveData().observe(this, {
            srl_fav.isRefreshing = it
        })
    }

    private fun initAdapter() {
        githubUserListAdapter = GithubUserListAdapter(this, ArrayList())
        githubUserListAdapter.setOnItemClickListener(object: GithubUserListAdapter.OnItemClickListener {
            override fun onItemClick(user: GithubUser) {
                val moveIntent = Intent(this@FavoriteUserActivity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_GITHUB_USER, user)
                startActivity(moveIntent)
            }
        })

        rv_favorite_users.layoutManager = LinearLayoutManager(this)
        rv_favorite_users.adapter = githubUserListAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}