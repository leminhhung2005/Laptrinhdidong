package com.haoht.ex12.data.db;

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.haoht.ex12.data.model.User

@Dao
interface UserDao {

    @Insert
    suspend fun register(user: User)

    @Query("SELECT * FROM users WHERE username=:u AND password=:p")
    suspend fun login(u: String, p: String): User?
}
