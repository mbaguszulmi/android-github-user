package com.mbaguszulmi.githubuser.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.model.database.entities.GithubUser
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

        mainViewModel.fetchAllUsers()

        srl_main.setOnRefreshListener {
            mainViewModel.fetchAllUsers()
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
            mainViewModel.fetchAllUsers()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.mi_change_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)

                return true
            }
            R.id.mi_favorite_users -> {
                val mIntent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(mIntent)

                return true
            }
            R.id.mi_settings -> {
                val mIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(mIntent)

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
