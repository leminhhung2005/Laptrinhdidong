package com.haoht.ex12.data.db;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.haoht.ex12.data.model.Todo
import com.haoht.ex12.data.model.User

@Database(entities = [User::class, Todo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun todoDao(): TodoDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "todo_db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}

