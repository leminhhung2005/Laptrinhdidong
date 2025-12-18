package com.example.baitap10

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TodoListDB.db"

        // Bảng Users
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_EMAIL = "email"

        // Bảng Tasks
        private const val TABLE_TASKS = "tasks"
        private const val COLUMN_TASK_ID = "task_id"
        private const val COLUMN_TASK_USER_ID = "user_id"
        private const val COLUMN_TASK_TITLE = "title"
        private const val COLUMN_TASK_DESCRIPTION = "description"
        private const val COLUMN_TASK_DATE = "date"
        private const val COLUMN_TASK_TIME = "time"
        private const val COLUMN_TASK_COMPLETED = "completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tạo bảng Users
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL
            )
        """.trimIndent()

        // Tạo bảng Tasks
        val createTasksTable = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TASK_USER_ID INTEGER NOT NULL,
                $COLUMN_TASK_TITLE TEXT NOT NULL,
                $COLUMN_TASK_DESCRIPTION TEXT,
                $COLUMN_TASK_DATE TEXT NOT NULL,
                $COLUMN_TASK_TIME TEXT,
                $COLUMN_TASK_COMPLETED INTEGER DEFAULT 0,
                FOREIGN KEY($COLUMN_TASK_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createTasksTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // ==================== User Operations ====================

    fun registerUser(username: String, password: String, email: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_EMAIL, email)
        }

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    fun loginUser(username: String, password: String): Int {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_USER_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))

        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
        }
        cursor.close()
        db.close()
        return userId
    }

    fun checkUsernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // ==================== Task Operations ====================

    fun addTask(userId: Int, title: String, description: String, date: String, time: String?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK_USER_ID, userId)
            put(COLUMN_TASK_TITLE, title)
            put(COLUMN_TASK_DESCRIPTION, description)
            put(COLUMN_TASK_DATE, date)
            put(COLUMN_TASK_TIME, time)
            put(COLUMN_TASK_COMPLETED, 0)
        }

        val result = db.insert(TABLE_TASKS, null, values)
        db.close()
        return result != -1L
    }

    fun getAllTasks(userId: Int): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_TASK_USER_ID = ? ORDER BY $COLUMN_TASK_DATE DESC, $COLUMN_TASK_TIME DESC"
        val cursor: Cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val task = Task(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID)),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_USER_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DATE)),
                    time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TIME)),
                    completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_COMPLETED)) == 1
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return taskList
    }

    fun updateTask(taskId: Int, title: String, description: String, date: String, time: String?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK_TITLE, title)
            put(COLUMN_TASK_DESCRIPTION, description)
            put(COLUMN_TASK_DATE, date)
            put(COLUMN_TASK_TIME, time)
        }

        val result = db.update(TABLE_TASKS, values, "$COLUMN_TASK_ID = ?", arrayOf(taskId.toString()))
        db.close()
        return result > 0
    }

    fun deleteTask(taskId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_TASKS, "$COLUMN_TASK_ID = ?", arrayOf(taskId.toString()))
        db.close()
        return result > 0
    }

    fun toggleTaskCompletion(taskId: Int, completed: Boolean): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK_COMPLETED, if (completed) 1 else 0)
        }
        val result = db.update(TABLE_TASKS, values, "$COLUMN_TASK_ID = ?", arrayOf(taskId.toString()))
        db.close()
        return result > 0
    }
}