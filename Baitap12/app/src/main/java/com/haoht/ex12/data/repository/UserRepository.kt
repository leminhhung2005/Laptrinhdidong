package com.haoht.ex12.data.repository

import com.haoht.ex12.data.db.UserDao
import com.haoht.ex12.data.model.User

class UserRepository(private val dao: UserDao) {

    suspend fun register(user: User) = dao.register(user)

    suspend fun login(u: String, p: String) = dao.login(u, p)
}
