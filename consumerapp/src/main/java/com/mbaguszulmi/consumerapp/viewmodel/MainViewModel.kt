package com.mbaguszulmi.consumerapp.viewmodel

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mbaguszulmi.consumerapp.api.GithubAPI
import com.mbaguszulmi.consumerapp.model.GithubUser
import com.mbaguszulmi.consumerapp.view.adapter.GithubUserListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val isLoadingUsers = MutableLiveData<Boolean>()
    private var isLoadingFollowers = false
    private var isLoadingFollowing = false
    private var isLoadingUser = false
    private val isLoadingDetail = MutableLiveData<Boolean>()
    private val githubAPI = GithubAPI()
    private val users = MutableLiveData<MutableList<GithubUser>>()
    private val user = MutableLiveData<GithubUser>()
    private val followers = MutableLiveData<MutableList<GithubUser>>()
    private val following = MutableLiveData<MutableList<GithubUser>>()
    private val favorite = MutableLiveData<Boolean>()

    fun init() {
        isLoadingUsers.value = false
        updateLoadingDetail()
        users.value = ArrayList()
        followers.value = ArrayList()
        following.value = ArrayList()
        favorite.value = false
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

    private fun isLoadingDetail(): Boolean = isLoadingDetail.value!!

    fun getUserListLiveData(): LiveData<MutableList<GithubUser>> = users

    fun getFollowersLiveData(): LiveData<MutableList<GithubUser>> = followers

    fun getFollowingLiveData(): LiveData<MutableList<GithubUser>> = following

    fun updateLoadingDetail() {
        isLoadingDetail.value = isLoadingUser || isLoadingFollowers || isLoadingFollowing
    }

    fun getUserLiveData(): LiveData<GithubUser> = user

    fun setUser(user: GithubUser) {
        this.user.value = user
    }

    fun isFavoriteLiveData(): LiveData<Boolean> = favorite

    fun setFavorite(favorite: Boolean) {
        this.favorite.value = favorite
    }

    fun registerFavoriteListWatcher(context: AppCompatActivity) {
        val handlerThread = HandlerThread("UserListObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        context.contentResolver.registerContentObserver(GithubUser.CONTENT_URI, true,
            object: ContentObserver(handler) {
                override fun onChange(selfChange: Boolean) {
                    loadUserAsync(context)
                    context.runOnUiThread {
                        isLoadingUsers.value = true
                    }
                }
            })
    }

    fun loadUserAsync(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            val differedUsers = async (Dispatchers.IO) {
                val cursor = context.contentResolver?.query(GithubUser.CONTENT_URI, null, null, null, null)
                val githubUserList = cursor?.let { GithubUser.fromCursorToList(it) }
                cursor?.close()
                githubUserList
            }

            var userList = differedUsers.await()
            if (userList == null) {
                userList = ArrayList()
            }
            isLoadingUsers.value = false
            users.value = userList
        }
    }

    fun registerFavoriteWatcher(context: Context, user: GithubUser) {
        val handlerThread = HandlerThread("UserObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        context.contentResolver.registerContentObserver(GithubUser.CONTENT_URI, true,
            object: ContentObserver(handler) {
                override fun onChange(selfChange: Boolean) {
                    updateFavorite(context, user)
                }
            })
    }

    fun updateFavorite(context: Context, user: GithubUser) {
        GlobalScope.launch(Dispatchers.Main) {
            val differedUser = async(Dispatchers.IO) {
                val cursor = context.contentResolver?.query(user.getUriWithId(), null, null, null, null)
                val githubUser = cursor?.let { GithubUser.fromCursor(it) }
                cursor?.close()
                githubUser
            }
            val userNew = differedUser.await()
            Log.d(TAG, "updateFavorite")
            favorite.value = userNew != null
        }
    }

    fun toggleFavorite(context: Context, user: GithubUser) {
        if (!isLoadingDetail()) {
            val isFavorite = favorite.value!!

            if (isFavorite) {
                context.contentResolver.delete(user.getUriWithId(), null, null)
            }
            else {
                context.contentResolver.insert(GithubUser.CONTENT_URI, user.toContentValues())
            }
        }
    }
}