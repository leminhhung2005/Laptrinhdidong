package com.haoht.ex12.data.repository

import com.haoht.ex12.data.db.TodoDao
import com.haoht.ex12.data.model.Todo

class TodoRepository(private val dao: TodoDao) {

    fun getTodos(uid: Int) = dao.getTodos(uid)

    suspend fun insert(todo: Todo) = dao.insert(todo)
}
