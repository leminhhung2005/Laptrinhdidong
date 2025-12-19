package com.haoht.ex12.data.db;

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.haoht.ex12.data.model.Todo

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(todo: Todo)

    @Query("SELECT * FROM todos WHERE userId=:uid")
    fun getTodos(uid: Int): LiveData<List<Todo>>
}
