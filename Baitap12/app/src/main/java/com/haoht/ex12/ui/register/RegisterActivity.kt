package com.haoht.ex12.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.haoht.ex12.data.db.AppDatabase
import com.haoht.ex12.data.repository.UserRepository
import com.haoht.ex12.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.get(this)
        viewModel = RegisterViewModel(
            UserRepository(db.userDao())
        )

        binding.btnRegister.setOnClickListener {
            val username = binding.edtUser.text.toString()
            val password = binding.edtPass.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(username, password)
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
            finish() // quay lại LoginActivity
        }
    }
}
