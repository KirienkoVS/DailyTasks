package com.example.dailytasks.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class DailyTasksRepository(
    private val boyTasksDao: BoyTasksDao,
    private val boyResultsDao: BoyResultsDao,
    private val boyTasksListDao: BoyTasksListDao,
    private val girlTasksDao: GirlTasksDao,
    private val girlResultsDao: GirlResultsDao,
    private val girlTasksListDao: GirlTasksListDao
) {

    val getBoyTasks: Flow<List<BoyTasksTable>> = boyTasksDao.getDailyTaskTable()
    val getBoyTasksList: Flow<List<BoyTasksList>> = boyTasksListDao.getTasksList()
    val getBoyResults: Flow<List<BoyResultsTable>> = boyResultsDao.getResults()

    val getGirlTasks: Flow<List<GirlTasksTable>> = girlTasksDao.getDailyTaskTable()
    val getGirlTasksList: Flow<List<GirlTasksList>> = girlTasksListDao.getTasksList()
    val getGirlResults: Flow<List<GirlResultsTable>> = girlResultsDao.getResults()


    /////////////////////////////////////////BOY///////////////////////////////////////////////////
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertBoyTasksTable(table: BoyTasksTable) {
        boyTasksDao.insert(table)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateBoyTasksTable(table: BoyTasksTable) {
        boyTasksDao.update(table)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun clearBoyTasksTable() {
        boyTasksDao.deleteAll()
    }

    fun sortBoyTasksTableByDate(date: String): LiveData<List<BoyTasksTable>> {
        return boyTasksDao.getByDate(date)
    }

    fun sortBoyTasksTableByViewTypeAndDate(date: String, viewType: String): LiveData<List<BoyTasksTable>> {
        return boyTasksDao.getByViewTypeAndDate(date, viewType)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertBoyResult(boyResultsTable: BoyResultsTable) {
        boyResultsDao.insertResult(boyResultsTable)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateBoyResult(boyResultsTable: BoyResultsTable) {
        boyResultsDao.updateResult(boyResultsTable)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteBoyResults() {
        boyResultsDao.deleteResults()
    }

    fun sortBoyResultByDate(date: String): LiveData<List<BoyResultsTable>> {
        return boyResultsDao.getResultByDate(date)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertBoyTaskList(task: BoyTasksList) {
        boyTasksListDao.insertTask(task)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateBoyTaskList(task: BoyTasksList) {
        boyTasksListDao.updateTask(task)
    }

    fun sortBoyTaskListByName(categoryName: String): LiveData<List<BoyTasksList>> {
        return boyTasksListDao.getTaskByName(categoryName)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteBoyTaskList() {
        boyTasksListDao.deleteTaskList()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteBoyTask(categoryName: String) {
        boyTasksListDao.deleteTask(categoryName)
    }

    /////////////////////////////////////////GIRL//////////////////////////////////////////////////
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertGirlTasksTable(table: GirlTasksTable) {
        girlTasksDao.insert(table)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateGirlTasksTable(table: GirlTasksTable) {
        girlTasksDao.update(table)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun clearGirlTasksTable() {
        girlTasksDao.deleteAll()
    }

    fun sortGirlTasksTableByDate(date: String): LiveData<List<GirlTasksTable>> {
        return girlTasksDao.getByDate(date)
    }

    fun sortGirlTasksTableByViewTypeAndDate(date: String, viewType: String): LiveData<List<GirlTasksTable>> {
        return girlTasksDao.getByViewTypeAndDate(date, viewType)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertGirlResult(girlResultsTable: GirlResultsTable) {
        girlResultsDao.insertResult(girlResultsTable)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateGirlResult(girlResultsTable: GirlResultsTable) {
        girlResultsDao.updateResult(girlResultsTable)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteGirlResults() {
        girlResultsDao.deleteResults()
    }

    fun sortGirlResultByDate(date: String): LiveData<List<GirlResultsTable>> {
        return girlResultsDao.getResultByDate(date)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertGirlTaskList(task: GirlTasksList) {
        girlTasksListDao.insertTask(task)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateGirlTaskList(task: GirlTasksList) {
        girlTasksListDao.updateTask(task)
    }

    fun sortGirlTaskListByName(categoryName: String): LiveData<List<GirlTasksList>> {
        return girlTasksListDao.getTaskByName(categoryName)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteGirlTaskList() {
        girlTasksListDao.deleteTaskList()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteGirlTask(categoryName: String) {
        girlTasksListDao.deleteTask(categoryName)
    }
}