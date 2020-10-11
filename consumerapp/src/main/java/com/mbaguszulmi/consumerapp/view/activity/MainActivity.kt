package com.mbaguszulmi.consumerapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbaguszulmi.consumerapp.R
import com.mbaguszulmi.consumerapp.model.GithubUser
import com.mbaguszulmi.consumerapp.view.adapter.GithubUserListAdapter
import com.mbaguszulmi.consumerapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var githubUserListAdapter: GithubUserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.init()

        initView()
    }

    private fun initView() {
        initAdapter()

        // ActionBar
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
                val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_GITHUB_USER, user)
                startActivity(moveIntent)
            }
        })

        rv_favorite_users.layoutManager = LinearLayoutManager(this)
        rv_favorite_users.adapter = githubUserListAdapter
    }
}
