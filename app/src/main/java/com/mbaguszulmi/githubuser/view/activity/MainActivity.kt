package com.mbaguszulmi.githubuser.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.model.GithubUser
import com.mbaguszulmi.githubuser.view.adapter.GithubUserListAdapter
import com.mbaguszulmi.githubuser.viewmodel.MainViewModel

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

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

        mainViewModel.getAllUsers()

        srl_main.setOnRefreshListener {
            mainViewModel.getAllUsers()
        }

        mainViewModel.getUserListLiveData().observe(this, Observer {
                list ->
            Log.d(TAG, "Update data " + list.size)
            githubUserListAdapter.updateData(list)
        })

        mainViewModel.isLoadingUsersLiveData().observe(this, Observer {
            isLoadingUser -> srl_main.isRefreshing = isLoadingUser
        })

        mainViewModel.getQueryLiveData().observe(this, Observer {
            Log.d(TAG, "Update data $it")
            mainViewModel.getAllUsers()
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
        rv_github_users.layoutManager = LinearLayoutManager(this)
        rv_github_users.adapter = githubUserListAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.mi_search)
        val searchView = searchMenuItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.find_users)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(q: String?): Boolean {
                mainViewModel.setQuery(q ?: "")

                return true
            }
            override fun onQueryTextChange(q: String?): Boolean {
                return false
            }
        })

        searchMenuItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                mainViewModel.setQuery("")

                return true
            }
        })

        return true
    }
}
