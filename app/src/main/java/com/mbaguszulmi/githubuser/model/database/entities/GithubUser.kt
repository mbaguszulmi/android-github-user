package com.mbaguszulmi.githubuser.model.database.entities

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import androidx.core.content.contentValuesOf
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "github_user")
data class GithubUser(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String?,
    @SerializedName("login")
    var username: String?,
    @SerializedName("public_repos")
    @ColumnInfo(name = "repo_count")
    var repoCount: Int = 0,
    var followers: Int = 0,
    var following: Int = 0,
    var bio: String? = "",
    @SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String?,
    var favorite: Boolean = false
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
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_USERNAME = "username"
        const val KEY_REPO_COUNT = "repo_count"
        const val KEY_FOLLOWERS = "followers"
        const val KEY_FOLLOWING = "following"
        const val KEY_BIO = "bio"
        const val KEY_AVATAR_URL = "avatar_url"
        const val KEY_FAVORITE = "favorite"

        override fun createFromParcel(parcel: Parcel): GithubUser {
            return GithubUser(parcel)
        }

        override fun newArray(size: Int): Array<GithubUser?> {
            return arrayOfNulls(size)
        }

        fun fromCursor(cursor: Cursor): GithubUser {
            val githubUser = GithubUser(0, null, null, 0, 0, 0, null, null, false)

            cursor.apply {
                moveToFirst()
                githubUser.id = getInt(getColumnIndexOrThrow(KEY_ID))
                githubUser.name = getString(getColumnIndexOrThrow(KEY_NAME))
                githubUser.username = getString(getColumnIndexOrThrow(KEY_USERNAME))
                githubUser.repoCount = getInt(getColumnIndexOrThrow(KEY_REPO_COUNT))
                githubUser.followers = getInt(getColumnIndexOrThrow(KEY_FOLLOWERS))
                githubUser.following = getInt(getColumnIndexOrThrow(KEY_FOLLOWING))
                githubUser.bio = getString(getColumnIndexOrThrow(KEY_BIO))
                githubUser.avatarUrl = getString(getColumnIndexOrThrow(KEY_AVATAR_URL))
                githubUser.favorite = getInt(getColumnIndexOrThrow(KEY_FAVORITE)) == 1
            }

            return githubUser
        }

        fun fromCursorToList(cursor: Cursor): ArrayList<GithubUser> {
            val githubUsers = ArrayList<GithubUser>()

            cursor.apply {
                while (moveToNext()) {
                    val id = getInt(getColumnIndexOrThrow(KEY_ID))
                    val name = getString(getColumnIndexOrThrow(KEY_NAME))
                    val username = getString(getColumnIndexOrThrow(KEY_USERNAME))
                    val repoCount = getInt(getColumnIndexOrThrow(KEY_REPO_COUNT))
                    val followers = getInt(getColumnIndexOrThrow(KEY_FOLLOWERS))
                    val following = getInt(getColumnIndexOrThrow(KEY_FOLLOWING))
                    val bio = getString(getColumnIndexOrThrow(KEY_BIO))
                    val avatarUrl = getString(getColumnIndexOrThrow(KEY_AVATAR_URL))
                    val favorite = getInt(getColumnIndexOrThrow(KEY_FAVORITE)) == 1

                    githubUsers.add(GithubUser(id, name, username, repoCount, followers, following, bio, avatarUrl, favorite))
                }
            }

            return githubUsers
        }
    }
}

fun GithubUser.getUrl(): String {
    return "https://github.com/" + this.username
}
