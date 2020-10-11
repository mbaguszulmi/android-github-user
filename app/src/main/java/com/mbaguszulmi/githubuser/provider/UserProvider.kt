package com.mbaguszulmi.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.mbaguszulmi.githubuser.App
import com.mbaguszulmi.githubuser.model.database.AppDb
import com.mbaguszulmi.githubuser.model.database.dao.GithubUserDao
import com.mbaguszulmi.githubuser.model.database.entities.GithubUser

class UserProvider : ContentProvider() {
    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var githubUserDao: GithubUserDao
    }

    init {
        sUriMatcher.addURI(GithubUser.AUTHORITY, GithubUser.TABLE_NAME, USER)

        sUriMatcher.addURI(GithubUser.AUTHORITY, "${GithubUser.TABLE_NAME}/#", USER_ID)
    }

    override fun onCreate(): Boolean {
        githubUserDao = AppDb.getInstance(context as Context).githubUserDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

        return when (sUriMatcher.match(uri)) {
            USER -> githubUserDao.findAll()
            USER_ID -> githubUserDao.find(uri.lastPathSegment!!.toInt())
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added = when (USER) {
            sUriMatcher.match(uri) -> githubUserDao.insert(GithubUser.fromContentValues(values))
            else -> 0
        }

        context?.contentResolver?.notifyChange(GithubUser.CONTENT_URI, null)

        return Uri.parse("${GithubUser.CONTENT_URI}/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated = when (USER_ID) {
            sUriMatcher.match(uri) -> githubUserDao.update(GithubUser.fromContentValues(values))
            else -> 0
        }

        context?.contentResolver?.notifyChange(GithubUser.CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {

        val deleted =  when (USER_ID) {
            sUriMatcher.match(uri) -> {
                val githubUser = githubUserDao.findGithubUser(uri.lastPathSegment!!.toInt())
                githubUserDao.delete(githubUser)
            }
            else -> 0
        }

        context?.contentResolver?.notifyChange(GithubUser.CONTENT_URI, null)

        return deleted
    }

    override fun getType(uri: Uri): String? {
        return "GithubUser"
    }
}