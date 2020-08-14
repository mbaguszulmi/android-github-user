package com.mbaguszulmi.githubuser.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.model.GithubUser
import com.mbaguszulmi.githubuser.repo.GithubUsersData
import com.mbaguszulmi.githubuser.view.adapter.GithubUserListAdapter

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var githubUsers : ArrayList<GithubUser>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        initView()
    }

    private fun initData() {
        githubUsers = GithubUsersData.githubUsers
    }

    private fun initView() {
        initAdapter()
    }

    private fun initAdapter() {
        val githubUserListAdapter = GithubUserListAdapter(githubUsers)
        githubUserListAdapter.setOnItemClickListener(object: GithubUserListAdapter.OnItemClickListener {
            override fun onItemClick(user: GithubUser) {
                val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_GITHUB_USER, user)
                startActivity(moveIntent)
            }
        })
        rv_github_users.layoutManager = LinearLayoutManager(this)
        rv_github_users.adapter = githubUserListAdapter
    }
}
