package com.example.dailytasks.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boy_results_table")
data class BoyResultsTable(@PrimaryKey val id: Long,
                           val date: String,
                           val result: String,
                           val rating: Float
)
