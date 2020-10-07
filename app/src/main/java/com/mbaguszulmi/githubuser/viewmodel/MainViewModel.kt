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
    private var isLoadingFollowers = false
    private var isLoadingFollowing = false
    private var isLoadingUser = false
    private val isLoadingDetail = MutableLiveData<Boolean>()
    private val query = MutableLiveData<String>()
    private val githubAPI = GithubAPI()
    private val users = MutableLiveData<MutableList<GithubUser>>()
    private val user = MutableLiveData<GithubUser>()
    private val followers = MutableLiveData<MutableList<GithubUser>>()
    private val following = MutableLiveData<MutableList<GithubUser>>()

    fun init() {
        isLoadingUsers.value = false
        updateLoadingDetail()
        users.value = ArrayList()
        followers.value = ArrayList()
        following.value = ArrayList()
        query.value = ""
    }

    fun fetchAllUsers() {
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

    fun fetchFullNameUser(adapter: GithubUserListAdapter, user: GithubUser, position: Int) {
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

    fun fetchFollowers(username: String) {
        isLoadingFollowers = true
        updateLoadingDetail()
        githubAPI.getService().getFollowers(username).enqueue(object : Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                isLoadingFollowers = false
                updateLoadingDetail()
                if (response.isSuccessful) {
                    val list = response.body()
                    followers.value = list?.toMutableList()
                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                isLoadingFollowers = false
                updateLoadingDetail()
            }
        })
    }

    fun fetchFollowing(username: String) {
        isLoadingFollowing = true
        updateLoadingDetail()
        githubAPI.getService().getFollowing(username).enqueue(object : Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                isLoadingFollowing = false
                updateLoadingDetail()
                if (response.isSuccessful) {
                    val list = response.body()
                    following.value = list?.toMutableList()
                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                isLoadingFollowing = false
                updateLoadingDetail()
            }
        })
    }

    fun fetchUser(username: String) {
        isLoadingUser = true
        updateLoadingDetail()
        githubAPI.getService().getUser(username).enqueue(object : Callback<GithubUser> {
            override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                isLoadingUser = false
                updateLoadingDetail()
                if (response.isSuccessful) {
                    user.value = response.body()
                    fetchFollowers(username)
                    fetchFollowing(username)
                }
            }

            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                isLoadingUser = false
                updateLoadingDetail()
            }
        })
    }

    fun setLoadingUsers(loading: Boolean) {
        isLoadingUsers.value = loading
    }

    fun isLoadingUsersLiveData() : LiveData<Boolean> = isLoadingUsers

    fun isLoadingDetailLiveData() : LiveData<Boolean> = isLoadingDetail

    fun getUserListLiveData(): LiveData<MutableList<GithubUser>> = users

    fun setQuery(q: String) {
        query.value = q
    }

    fun getQueryLiveData(): LiveData<String> = query

    fun getFollowersLiveData(): LiveData<MutableList<GithubUser>> = followers

    fun getFollowingLiveData(): LiveData<MutableList<GithubUser>> = following

    fun updateLoadingDetail() {
        isLoadingDetail.value = isLoadingUser || isLoadingFollowers || isLoadingFollowing
    }

    fun getUserLiveData(): LiveData<GithubUser> = user

    fun setUser(user: GithubUser) {
        this.user.value = user
    }
}