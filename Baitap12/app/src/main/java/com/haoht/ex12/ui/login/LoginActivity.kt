package com.haoht.ex12.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.haoht.ex12.data.db.AppDatabase
import com.haoht.ex12.data.repository.UserRepository
import com.haoht.ex12.databinding.ActivityLoginBinding
import com.haoht.ex12.ui.register.RegisterActivity
import com.haoht.ex12.ui.todo.TodoActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var vm: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.get(this)
        vm = LoginViewModel(UserRepository(db.userDao()))

        binding.btnLogin.setOnClickListener {
            vm.login(
                binding.edtUser.text.toString(),
                binding.edtPass.text.toString()
            )
        }
        vm.user.observe(this) {
            if (it != null) {
                val i = Intent(this, TodoActivity::class.java)
                i.putExtra("USER_ID", it.id)
                startActivity(i)
                finish()
            } else {
                Toast.makeText(this, "Sai tài khoản", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
