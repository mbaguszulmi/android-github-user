package com.mbaguszulmi.githubuser.model.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.mbaguszulmi.githubuser.model.database.entities.GithubUser

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM github_user")
    fun findAll(): List<GithubUser>

    @Query("SELECT * FROM github_user WHERE id = :id")
    fun find(id: Int): GithubUser

    @Insert(onConflict = REPLACE)
    fun insert(vararg users: GithubUser)

    @Update
    fun update(user: GithubUser)

    @Delete
    fun delete(user: GithubUser)
}