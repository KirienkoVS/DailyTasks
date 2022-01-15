package com.example.dailytasks.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "boy_tasks_list_table")
data class BoyTasksList(@PrimaryKey val number: Long, val categoryName: String, val viewType: String) /*{
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}*/