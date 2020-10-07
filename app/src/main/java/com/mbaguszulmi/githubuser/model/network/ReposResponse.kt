package com.mbaguszulmi.githubuser.model.network

import com.google.gson.annotations.SerializedName
import com.mbaguszulmi.githubuser.model.GithubUser

data class ReposResponse(
    val id: Long,
    @SerializedName("node_id")
    val nodeId: String,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("private")
    val isPrivate: Boolean,
    val owner: GithubUser
)