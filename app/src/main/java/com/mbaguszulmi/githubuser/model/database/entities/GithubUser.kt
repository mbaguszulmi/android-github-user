package com.mbaguszulmi.githubuser.model.database.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "github_user")
data class GithubUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String?,
    @SerializedName("login")
    val username: String?,
    @SerializedName("public_repos")
    @ColumnInfo(name = "repo_count")
    var repoCount: Int = 0,
    var followers: Int = 0,
    var following: Int = 0,
    val bio: String? = "",
    @SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,
    val favorite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
        if (name == null || name!!.isEmpty()) {
            name = username
        }
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
