package com.example.dailytasks.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "girl_tasks_list_table")
data class GirlTasksList(@PrimaryKey val number: Long, val categoryName: String, val viewType: String)