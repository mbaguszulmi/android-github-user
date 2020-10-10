package com.mbaguszulmi.githubuser.model.network

import com.google.gson.annotations.SerializedName
import com.mbaguszulmi.githubuser.model.database.entities.GithubUser

data class UserSearchResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_result")
    val incompleteResult: Boolean,
    val items: List<GithubUser>,
    val message: String?
)