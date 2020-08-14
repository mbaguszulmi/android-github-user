package com.mbaguszulmi.githubuser.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.model.GithubUser

import kotlinx.android.synthetic.main.itemview_github_users.view.*

class GithubUserListAdapter(private val githubUsers: List<GithubUser>)
    : RecyclerView.Adapter<GithubUserListAdapter.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
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
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindGithubUser(user: GithubUser) {
            val username = "@" + user.username;
            itemView.tv_full_name.text = user.name
            itemView.tv_username.text = username;
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: GithubUser);
    }
}