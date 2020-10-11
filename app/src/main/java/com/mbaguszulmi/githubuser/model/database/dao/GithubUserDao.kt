package com.mbaguszulmi.githubuser.model.database.dao

import android.database.Cursor
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mbaguszulmi.githubuser.model.database.entities.GithubUser

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM github_user")
    fun findAll(): Cursor

    @Query("SELECT * FROM github_user WHERE id = :id")
    fun find(id: Int): Cursor

    @Query("SELECT * FROM github_user WHERE id = :id")
    fun findGithubUser(id: Int): GithubUser

    @Insert(onConflict = REPLACE)
    fun insert(vararg users: GithubUser): Long

    @Update
    fun update(user: GithubUser): Int

    @Delete
    fun delete(user: GithubUser): Int
}