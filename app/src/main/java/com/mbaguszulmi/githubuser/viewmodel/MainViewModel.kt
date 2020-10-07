package com.mbaguszulmi.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mbaguszulmi.githubuser.api.GithubAPI
import com.mbaguszulmi.githubuser.model.GithubUser
import com.mbaguszulmi.githubuser.model.network.UserSearchResponse
import com.mbaguszulmi.githubuser.view.adapter.GithubUserListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val isLoadingUsers = MutableLiveData<Boolean>()
    private val isLoadingFollowers = MutableLiveData<Boolean>()
    private val isLoadingFollowing = MutableLiveData<Boolean>()
    private val query = MutableLiveData<String>()
    private val githubAPI = GithubAPI()
    private val users = MutableLiveData<MutableList<GithubUser>>()

    fun init() {
        isLoadingUsers.value = true
        isLoadingFollowers.value = true
        isLoadingFollowing.value = true
        users.value = ArrayList()
        query.value = ""
    }

    fun getAllUsers() {
        setLoadingUsers(true)

        val q = query.value
        if (q == null || q.isEmpty()) {
            githubAPI.getService().getUsers().enqueue(object: Callback<List<GithubUser>> {
                override fun onResponse(
                    call: Call<List<GithubUser>>,
                    response: Response<List<GithubUser>>
                ) {
                    setLoadingUsers(false)
                    if (response.isSuccessful) {
                        val data = response.body()
                        users.value = data?.toMutableList()
                    }
                }

                override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                    setLoadingUsers(false)
                }

            })
        }
        else {
            githubAPI.getService().getUsers(q).enqueue(object: Callback<UserSearchResponse> {
                override fun onResponse(
                    call: Call<UserSearchResponse>,
                    response: Response<UserSearchResponse>
                ) {
                    setLoadingUsers(false)
                    if (response.isSuccessful) {
                        val data = response.body() as UserSearchResponse
                        users.value = data.items.toMutableList()
                    }
                }

                override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                    setLoadingUsers(false)
                }

            })
        }
    }

    fun getFullNameUser(adapter: GithubUserListAdapter, user: GithubUser, position: Int) {
        user.username?.let {
            githubAPI.getService().getUser(it).enqueue(object: Callback<GithubUser> {
                override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                    if (response.isSuccessful) {
                        val u = response.body()
                        if (u?.name != null) user.name = u.name else user.name = u?.username
                        adapter.notifyItemChanged(position)
                    }
                }

                override fun onFailure(call: Call<GithubUser>, t: Throwable) {

                }
            })
        }
    }

    fun getFollowers() : List<GithubUser> {
        var githubUsers: List<GithubUser> = ArrayList<GithubUser>()

        return githubUsers
    }

    fun getFollowing() : List<GithubUser> {
        var githubUsers: List<GithubUser> = ArrayList<GithubUser>()

        return githubUsers
    }

    fun setLoadingUsers(loading: Boolean) {
        isLoadingUsers.value = loading
    }

    fun setLoadingFollowers(loading: Boolean) {
        isLoadingFollowers.value = loading
    }

    fun setLoadingFollowing(loading: Boolean) {
        isLoadingFollowing.value = loading
    }

    fun isLoadingUsersLiveData() : LiveData<Boolean> {
        return isLoadingUsers
    }

    fun isLoadingFollowersLiveData() : LiveData<Boolean> {
        return isLoadingFollowers
    }

    fun isLoadingFollowingLiveData() : LiveData<Boolean> {
        return isLoadingFollowing
    }

    fun getUserListLiveData(): LiveData<MutableList<GithubUser>> {
        return users
    }

    fun setQuery(q: String) {
        query.value = q
    }

    fun getQueryLiveData(): LiveData<String> {
        return query
    }
}