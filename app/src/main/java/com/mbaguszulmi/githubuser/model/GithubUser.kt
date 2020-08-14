package com.mbaguszulmi.githubuser.model

import android.os.Parcel
import android.os.Parcelable

data class GithubUser(
    val name: String?,
    val username: String?,
    val repoCount: Int,
    val followers: Int,
    val following: Int,
    val bio: String?,
    val avatarUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(username)
        parcel.writeInt(repoCount)
        parcel.writeInt(followers)
        parcel.writeInt(following)
        parcel.writeString(bio)
        parcel.writeString(avatarUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GithubUser> {
        override fun createFromParcel(parcel: Parcel): GithubUser {
            return GithubUser(parcel)
        }

        override fun newArray(size: Int): Array<GithubUser?> {
            return arrayOfNulls(size)
        }
    }
}

fun GithubUser.getUrl(): String {
    return "https://github.com/" + this.username
}
