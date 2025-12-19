package com.haoht.ex12.ui.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haoht.ex12.data.db.AppDatabase
import com.haoht.ex12.data.repository.TodoRepository
import com.haoht.ex12.databinding.ActivityTodoBinding

class TodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding
    private lateinit var vm: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra("USER_ID", -1)

        val db = AppDatabase.get(this)
        vm = TodoViewModel(TodoRepository(db.todoDao()))

        binding.btnAdd.setOnClickListener {
            val title = binding.edtTodo.text.toString()
            if (title.isNotEmpty()) {
                vm.addTodo(title, userId)
                binding.edtTodo.text.clear()
            }
        }

        vm.getTodos(userId).observe(this) {
            binding.txtTodo.text =
                it.joinToString("\n") { t -> "• ${t.title}" }
        }
    }
}
