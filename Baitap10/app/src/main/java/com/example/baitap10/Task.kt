package com.example.baitap10

data class Task(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val date: String,
    val time: String?,
    var completed: Boolean = false
)