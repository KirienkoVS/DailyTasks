package com.example.dailytasks.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "girl_results_table")
data class GirlResultsTable(@PrimaryKey val id: Long,
                           val date: String,
                           val result: String,
                           val rating: Float
)