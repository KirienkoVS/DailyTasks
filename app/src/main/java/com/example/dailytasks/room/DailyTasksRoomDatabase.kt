package com.example.dailytasks.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BoyTasksTable::class, BoyResultsTable::class, BoyTasksList::class,
    GirlTasksTable::class, GirlResultsTable::class, GirlTasksList::class], version = 1, exportSchema = false)
abstract class DailyTasksRoomDatabase: RoomDatabase() {

    abstract fun boyTasksDao(): BoyTasksDao
    abstract fun boyResultsDao(): BoyResultsDao
    abstract fun boyTasksListDao(): BoyTasksListDao

    abstract fun girlTasksDao(): GirlTasksDao
    abstract fun girlResultsDao(): GirlResultsDao
    abstract fun girlTasksListDao(): GirlTasksListDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: DailyTasksRoomDatabase? = null

        fun getDatabase(context: Context): DailyTasksRoomDatabase {
            // if the INSTANCE is not null, then return it, if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailyTasksRoomDatabase::class.java,
                    "dailyTasks_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}