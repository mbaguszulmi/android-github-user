package com.mbaguszulmi.githubuser.model.database.entities

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_user")
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
    var avatarUrl: String?
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
        parcel.writeInt(id)
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

    fun toContentValues(): ContentValues {
        val values = ContentValues()
        with(values) {
            put(KEY_ID, id)
            put(KEY_NAME, name)
            put(KEY_USERNAME, username)
            put(KEY_REPO_COUNT, repoCount)
            put(KEY_FOLLOWERS, followers)
            put(KEY_FOLLOWING, following)
            put(KEY_BIO, bio)
            put(KEY_AVATAR_URL, avatarUrl)
        }
        return values
    }

    fun getUriWithId(): Uri {
        return Uri.parse("$CONTENT_URI/$id")
    }

    companion object CREATOR : Parcelable.Creator<GithubUser> {
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_USERNAME = "username"
        private const val KEY_REPO_COUNT = "repo_count"
        private const val KEY_FOLLOWERS = "followers"
        private const val KEY_FOLLOWING = "following"
        private const val KEY_BIO = "bio"
        private const val KEY_AVATAR_URL = "avatar_url"
        const val AUTHORITY = "com.mbaguszulmi.githubuser"
        const val TABLE_NAME = "github_user"
        private const val SCHEME = "content"
        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()

        override fun createFromParcel(parcel: Parcel): GithubUser {
            return GithubUser(parcel)
        }

        override fun newArray(size: Int): Array<GithubUser?> {
            return arrayOfNulls(size)
        }

        fun fromContentValues(contentValues: ContentValues?): GithubUser {
            val githubUser = GithubUser(0, null, null, 0, 0, 0, null, null)

            if (contentValues == null) return githubUser

            if (contentValues.containsKey(KEY_ID))
                githubUser.id = contentValues.getAsInteger(KEY_ID)

            if (contentValues.containsKey(KEY_NAME))
                githubUser.name = contentValues.getAsString(KEY_NAME)

            if (contentValues.containsKey(KEY_USERNAME))
                githubUser.username = contentValues.getAsString(KEY_USERNAME)

            if (contentValues.containsKey(KEY_REPO_COUNT))
                githubUser.repoCount = contentValues.getAsInteger(KEY_REPO_COUNT)

            if (contentValues.containsKey(KEY_FOLLOWERS))
                githubUser.followers = contentValues.getAsInteger(KEY_FOLLOWERS)

            if (contentValues.containsKey(KEY_FOLLOWING))
                githubUser.following = contentValues.getAsInteger(KEY_FOLLOWING)

            if (contentValues.containsKey(KEY_BIO))
                githubUser.bio = contentValues.getAsString(KEY_BIO)

            if (contentValues.containsKey(KEY_AVATAR_URL))
                githubUser.avatarUrl = contentValues.getAsString(KEY_AVATAR_URL)

            return githubUser
        }

        fun fromCursor(cursor: Cursor): GithubUser? {
            var githubUser: GithubUser? = GithubUser(0, null, null, 0, 0, 0, null, null)

            cursor.apply {
                if (moveToFirst()) {
                    githubUser?.id = getInt(getColumnIndexOrThrow(KEY_ID))
                    githubUser?.name = getString(getColumnIndexOrThrow(KEY_NAME))
                    githubUser?.username = getString(getColumnIndexOrThrow(KEY_USERNAME))
                    githubUser?.repoCount = getInt(getColumnIndexOrThrow(KEY_REPO_COUNT))
                    githubUser?.followers = getInt(getColumnIndexOrThrow(KEY_FOLLOWERS))
                    githubUser?.following = getInt(getColumnIndexOrThrow(KEY_FOLLOWING))
                    githubUser?.bio = getString(getColumnIndexOrThrow(KEY_BIO))
                    githubUser?.avatarUrl = getString(getColumnIndexOrThrow(KEY_AVATAR_URL))
                }
                else githubUser = null
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

                    githubUsers.add(GithubUser(id, name, username, repoCount, followers, following, bio, avatarUrl))
                }
            }

            return githubUsers
        }
    }
}

fun GithubUser.getUrl(): String {
    return "https://github.com/" + this.username
}
