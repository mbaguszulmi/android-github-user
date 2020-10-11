package com.mbaguszulmi.githubuser.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mbaguszulmi.githubuser.model.database.dao.GithubUserDao
import com.mbaguszulmi.githubuser.model.database.entities.GithubUser

@Database(
    version = 1,
    entities = [GithubUser::class]
)
abstract class AppDb: RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao

    companion object {
        @Volatile
        var instance: AppDb? = null
        private const val DB_NAME = "github_user_db"
        fun getInstance(context: Context): AppDb {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, AppDb::class.java, DB_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
//                            .addMigrations() // <-- add migrations here
                            .build()
            }

            return instance as AppDb
        }
    }
}