package com.mbaguszulmi.githubuser.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.model.GithubUser
import com.mbaguszulmi.githubuser.viewmodel.MainViewModel

import kotlinx.android.synthetic.main.itemview_github_users.view.*

class GithubUserListAdapter(
    val activity: ViewModelStoreOwner
) : RecyclerView.Adapter<GithubUserListAdapter.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var mainViewModel: MainViewModel
    private lateinit var githubUsers: MutableList<GithubUser>

    constructor(activity: ViewModelStoreOwner,
                 githubUsers: MutableList<GithubUser>): this(activity) {
        this.githubUsers = githubUsers
        this.mainViewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun updateData(githubUsers: MutableList<GithubUser>) {
        with(this.githubUsers) {
            clear()
            addAll(githubUsers)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.itemview_github_users, parent,
            false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return githubUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = githubUsers[position]
        holder.bindGithubUser(user)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(user)
        }

        if (user.name == null) mainViewModel.getFullNameUser(this, user, position)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindGithubUser(user: GithubUser) {
            val username = "@" + user.username;
            itemView.tv_full_name.text = user.name
            itemView.tv_username.text = username;
            Glide.with(itemView)
                .load(user.avatarUrl)
                .into(itemView.iv_avatar)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: GithubUser);
    }
}