package com.example.dailytasks.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BoyTasksDao {

    @Query("SELECT * FROM boy_tasks_table")
    fun getDailyTaskTable(): Flow<List<BoyTasksTable>>

    @Query("SELECT * FROM boy_tasks_table WHERE Date = :date")
    fun getByDate(date: String): LiveData<List<BoyTasksTable>>

    @Query("SELECT * FROM boy_tasks_table WHERE viewType = :viewType and date = :date")
    fun getByViewTypeAndDate(date: String, viewType: String): LiveData<List<BoyTasksTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(table: BoyTasksTable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(table: BoyTasksTable)

    @Query("DELETE FROM boy_tasks_table")
    suspend fun deleteAll()
}

@Dao
interface BoyResultsDao {

    @Query("SELECT * FROM boy_results_table")
    fun getResults(): Flow<List<BoyResultsTable>>

    @Query("SELECT * FROM boy_results_table WHERE Date = :date")
    fun getResultByDate(date: String): LiveData<List<BoyResultsTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertResult(result: BoyResultsTable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateResult(result: BoyResultsTable)

    @Query("DELETE FROM boy_results_table")
    suspend fun deleteResults()
}

@Dao
interface BoyTasksListDao {

    @Query("SELECT * FROM boy_tasks_list_table")
    fun getTasksList(): Flow<List<BoyTasksList>>

    @Query("SELECT * FROM boy_tasks_list_table WHERE categoryName = :categoryName")
    fun getTaskByName(categoryName: String): LiveData<List<BoyTasksList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: BoyTasksList)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: BoyTasksList)

    @Query("DELETE FROM boy_tasks_list_table WHERE categoryName = :categoryName")
    suspend fun deleteTask(categoryName: String)

    @Query("DELETE FROM boy_tasks_list_table")
    suspend fun deleteTaskList()
}

//////////////////////////////////////GIRL//////////////////////////////////////////////////////////
@Dao
interface GirlTasksDao {

    @Query("SELECT * FROM girl_tasks_table")
    fun getDailyTaskTable(): Flow<List<GirlTasksTable>>

    @Query("SELECT * FROM girl_tasks_table WHERE Date = :date")
    fun getByDate(date: String): LiveData<List<GirlTasksTable>>

    @Query("SELECT * FROM girl_tasks_table WHERE viewType = :viewType and date = :date")
    fun getByViewTypeAndDate(date: String, viewType: String): LiveData<List<GirlTasksTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(table: GirlTasksTable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(table: GirlTasksTable)

    @Query("DELETE FROM girl_tasks_table")
    suspend fun deleteAll()
}

@Dao
interface GirlResultsDao {

    @Query("SELECT * FROM girl_results_table")
    fun getResults(): Flow<List<GirlResultsTable>>

    @Query("SELECT * FROM girl_results_table WHERE Date = :date")
    fun getResultByDate(date: String): LiveData<List<GirlResultsTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertResult(result: GirlResultsTable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateResult(result: GirlResultsTable)

    @Query("DELETE FROM girl_results_table")
    suspend fun deleteResults()
}

@Dao
interface GirlTasksListDao {

    @Query("SELECT * FROM girl_tasks_list_table")
    fun getTasksList(): Flow<List<GirlTasksList>>

    @Query("SELECT * FROM girl_tasks_list_table WHERE categoryName = :categoryName")
    fun getTaskByName(categoryName: String): LiveData<List<GirlTasksList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: GirlTasksList)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: GirlTasksList)

    @Query("DELETE FROM girl_tasks_list_table WHERE categoryName = :categoryName")
    suspend fun deleteTask(categoryName: String)

    @Query("DELETE FROM girl_tasks_list_table")
    suspend fun deleteTaskList()
}