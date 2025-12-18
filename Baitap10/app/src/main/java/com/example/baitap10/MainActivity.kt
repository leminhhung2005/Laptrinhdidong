package com.example.baitap10

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var fabAddTask: FloatingActionButton
    private var userId: Int = -1
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lấy userId từ SharedPreferences
        val sharedPref = getSharedPreferences("TodoListPrefs", MODE_PRIVATE)
        userId = sharedPref.getInt("USER_ID", -1)
        username = sharedPref.getString("USERNAME", "") ?: ""

        if (userId == -1) {
            // Chưa đăng nhập, quay về LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        supportActionBar?.title = "Xin chào, $username"

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewTasks)
        fabAddTask = findViewById(R.id.fabAddTask)

        setupRecyclerView()
        loadTasks()

        fabAddTask.setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(
            onEditClick = { task ->
                val intent = Intent(this, AddEditTaskActivity::class.java)
                intent.putExtra("TASK_ID", task.id)
                intent.putExtra("TASK_TITLE", task.title)
                intent.putExtra("TASK_DESCRIPTION", task.description)
                intent.putExtra("TASK_DATE", task.date)
                intent.putExtra("TASK_TIME", task.time)
                startActivity(intent)
            },
            onDeleteClick = { task ->
                deleteTask(task)
            },
            onCheckClick = { task ->
                toggleTaskCompletion(task)
            }
        )
        recyclerView.adapter = taskAdapter
    }

    private fun loadTasks() {
        val tasks = dbHelper.getAllTasks(userId)
        taskAdapter.updateTasks(tasks)
    }

    private fun deleteTask(task: Task) {
        val success = dbHelper.deleteTask(task.id)
        if (success) {
            Toast.makeText(this, "Đã xóa công việc", Toast.LENGTH_SHORT).show()
            loadTasks()
        } else {
            Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleTaskCompletion(task: Task) {
        val newStatus = !task.completed
        val success = dbHelper.toggleTaskCompletion(task.id, newStatus)
        if (success) {
            loadTasks()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("TodoListPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}