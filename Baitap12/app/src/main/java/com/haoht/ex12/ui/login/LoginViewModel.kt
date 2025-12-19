package com.haoht.ex12.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haoht.ex12.data.model.User
import com.haoht.ex12.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepository) : ViewModel() {

    val user = MutableLiveData<User?>()

    fun login(u: String, p: String) {
        viewModelScope.launch {
            user.value = repo.login(u, p)
        }
    }
}
