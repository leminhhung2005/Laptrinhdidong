package com.haoht.ex12.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haoht.ex12.data.model.User
import com.haoht.ex12.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repo: UserRepository) : ViewModel() {

    fun register(u: String, p: String) {
        viewModelScope.launch {
            repo.register(User(username = u, password = p))
        }
    }
}
