package com.haoht.ex12.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haoht.ex12.data.model.Todo
import com.haoht.ex12.data.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repo: TodoRepository) : ViewModel() {

    fun getTodos(uid: Int) = repo.getTodos(uid)

    fun addTodo(title: String, uid: Int) {
        viewModelScope.launch {
            repo.insert(Todo(title = title, userId = uid))
        }
    }
}
