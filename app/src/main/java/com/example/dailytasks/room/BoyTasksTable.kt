package com.example.dailytasks.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boy_tasks_table")
data class BoyTasksTable(@PrimaryKey val id: Long,
                         val viewText: String,
                         val date: String,
                         val checkBoxStatus: Boolean,
                         val viewType: String
)
