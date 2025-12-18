package com.example.baitap10

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etTaskTitle: EditText
    private lateinit var etTaskDescription: EditText
    private lateinit var etTaskDate: EditText
    private lateinit var etTaskTime: EditText
    private lateinit var btnSaveTask: Button
    private lateinit var btnCancel: Button

    private var taskId: Int = -1
    private var userId: Int = -1
    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        dbHelper = DatabaseHelper(this)

        // Lấy userId
        val sharedPref = getSharedPreferences("TodoListPrefs", MODE_PRIVATE)
        userId = sharedPref.getInt("USER_ID", -1)

        etTaskTitle = findViewById(R.id.etTaskTitle)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        etTaskDate = findViewById(R.id.etTaskDate)
        etTaskTime = findViewById(R.id.etTaskTime)
        btnSaveTask = findViewById(R.id.btnSaveTask)
        btnCancel = findViewById(R.id.btnCancel)

        // Kiểm tra nếu đang edit task
        taskId = intent.getIntExtra("TASK_ID", -1)
        if (taskId != -1) {
            supportActionBar?.title = "Sửa công việc"
            loadTaskData()
        } else {
            supportActionBar?.title = "Thêm công việc"
        }

        // Date picker
        etTaskDate.setOnClickListener {
            showDatePicker()
        }

        // Time picker
        etTaskTime.setOnClickListener {
            showTimePicker()
        }

        btnSaveTask.setOnClickListener {
            saveTask()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadTaskData() {
        etTaskTitle.setText(intent.getStringExtra("TASK_TITLE"))
        etTaskDescription.setText(intent.getStringExtra("TASK_DESCRIPTION"))
        selectedDate = intent.getStringExtra("TASK_DATE") ?: ""
        selectedTime = intent.getStringExtra("TASK_TIME") ?: ""
        etTaskDate.setText(selectedDate)
        etTaskTime.setText(selectedTime)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                etTaskDate.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                etTaskTime.setText(selectedTime)
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun saveTask() {
        val title = etTaskTitle.text.toString().trim()
        val description = etTaskDescription.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tiêu đề công việc", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show()
            return
        }

        val success = if (taskId == -1) {
            // Thêm mới
            dbHelper.addTask(userId, title, description, selectedDate, selectedTime)
        } else {
            // Cập nhật
            dbHelper.updateTask(taskId, title, description, selectedDate, selectedTime)
        }

        if (success) {
            Toast.makeText(
                this,
                if (taskId == -1) "Thêm công việc thành công" else "Cập nhật thành công",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } else {
            Toast.makeText(this, "Thao tác thất bại", Toast.LENGTH_SHORT).show()
        }
    }
}