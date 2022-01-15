package com.example.dailytasks

import androidx.lifecycle.*
import com.example.dailytasks.room.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DailyTasksViewModel(private val repository: DailyTasksRepository): ViewModel() {

    val getBoyTasksList: LiveData<List<BoyTasksList>> = repository.getBoyTasksList.asLiveData()
    private var getBoyTaskListByName: LiveData<List<BoyTasksList>>? = null
    val getBoyTasks: LiveData<List<BoyTasksTable>> = repository.getBoyTasks.asLiveData()
    private var getBoyTasksByDate: LiveData<List<BoyTasksTable>>? = null
    private var getBoyTasksByViewTypeAndDate: LiveData<List<BoyTasksTable>>? = null
    val getBoyResults: LiveData<List<BoyResultsTable>> = repository.getBoyResults.asLiveData()
    private var getBoyResultByDate: LiveData<List<BoyResultsTable>>? = null

    val getGirlTasksList: LiveData<List<GirlTasksList>> = repository.getGirlTasksList.asLiveData()
    private var getGirlTaskListByName: LiveData<List<GirlTasksList>>? = null
    val getGirlTasks: LiveData<List<GirlTasksTable>> = repository.getGirlTasks.asLiveData()
    private var getGirlTasksByDate: LiveData<List<GirlTasksTable>>? = null
    private var getGirlTasksByViewTypeAndDate: LiveData<List<GirlTasksTable>>? = null
    val getGirlResults: LiveData<List<GirlResultsTable>> = repository.getGirlResults.asLiveData()
    private var getGirlResultByDate: LiveData<List<GirlResultsTable>>? = null

    ///////////////////////////////////BOY/////////////////////////////////////////////////////////
    fun insertBoyTaskList(task: BoyTasksList) = viewModelScope.launch {
        repository.insertBoyTaskList(task) }

    fun updateBoyTaskList(task: BoyTasksList) = viewModelScope.launch {
        repository.updateBoyTaskList(task) }

    fun sortBoyTaskListByName(categoryName: String): LiveData<List<BoyTasksList>>? {
        viewModelScope.launch {
            getBoyTaskListByName = repository.sortBoyTaskListByName(categoryName)
        }
        return getBoyTaskListByName
    }

    fun deleteBoyTask(categoryName: String) = viewModelScope.launch {
        repository.deleteBoyTask(categoryName)
    }

    fun deleteBoyTasksList() = viewModelScope.launch {
        repository.deleteBoyTaskList()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @DelicateCoroutinesApi
    fun insertBoyTasksTable(table: BoyTasksTable) = GlobalScope.launch {
        repository.insertBoyTasksTable(table) }

    @DelicateCoroutinesApi
    fun updateBoyTasksTable(table: BoyTasksTable) = GlobalScope.launch {
        repository.updateBoyTasksTable(table) }

    fun clearBoyTasksTable() = viewModelScope.launch {
        repository.clearBoyTasksTable()
        }

    fun sortBoyTasksTableByDate(date: String): LiveData<List<BoyTasksTable>>? {
        viewModelScope.launch {
            getBoyTasksByDate = repository.sortBoyTasksTableByDate(date)
        }
        return getBoyTasksByDate
    }

    fun sortBoyTasksTableByViewTypeAndDate(date: String, viewType: String): LiveData<List<BoyTasksTable>>? {
        viewModelScope.launch {
            getBoyTasksByViewTypeAndDate = repository.sortBoyTasksTableByViewTypeAndDate(date, viewType)
        }
        return getBoyTasksByViewTypeAndDate
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    fun insertBoyResult(boyResultsTable: BoyResultsTable) = viewModelScope.launch {
        repository.insertBoyResult(boyResultsTable) }

    @DelicateCoroutinesApi
    fun updateBoyResult(boyResultsTable: BoyResultsTable) = GlobalScope.launch {
        repository.updateBoyResult(boyResultsTable) }

    fun deleteBoyResults() = viewModelScope.launch {
        repository.deleteBoyResults()
    }

    fun sortBoyResultByDate(date: String): LiveData<List<BoyResultsTable>>? {
        viewModelScope.launch {
            getBoyResultByDate = repository.sortBoyResultByDate(date)
        }
        return getBoyResultByDate
    }

    //////////////////////////////////////GIRL/////////////////////////////////////////////////////
    fun insertGirlTaskList(task: GirlTasksList) = viewModelScope.launch {
        repository.insertGirlTaskList(task) }

    fun updateGirlTaskList(task: GirlTasksList) = viewModelScope.launch {
        repository.updateGirlTaskList(task) }

    fun sortGirlTaskListByName(categoryName: String): LiveData<List<GirlTasksList>>? {
        viewModelScope.launch {
            getGirlTaskListByName = repository.sortGirlTaskListByName(categoryName)
        }
        return getGirlTaskListByName
    }

    fun deleteGirlTask(categoryName: String) = viewModelScope.launch {
        repository.deleteGirlTask(categoryName)
    }

    fun deleteGirlTasksList() = viewModelScope.launch {
        repository.deleteGirlTaskList()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @DelicateCoroutinesApi
    fun insertGirlTasksTable(table: GirlTasksTable) = GlobalScope.launch {
        repository.insertGirlTasksTable(table) }

    @DelicateCoroutinesApi
    fun updateGirlTasksTable(table: GirlTasksTable) = GlobalScope.launch {
        repository.updateGirlTasksTable(table) }

    fun clearGirlTasksTable() = viewModelScope.launch {
        repository.clearGirlTasksTable()
    }

    fun sortGirlTasksTableByDate(date: String): LiveData<List<GirlTasksTable>>? {
        viewModelScope.launch {
            getGirlTasksByDate = repository.sortGirlTasksTableByDate(date)
        }
        return getGirlTasksByDate
    }

    fun sortGirlTasksTableByViewTypeAndDate(date: String, viewType: String): LiveData<List<GirlTasksTable>>? {
        viewModelScope.launch {
            getGirlTasksByViewTypeAndDate = repository.sortGirlTasksTableByViewTypeAndDate(date, viewType)
        }
        return getGirlTasksByViewTypeAndDate
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    fun insertGirlResult(GirlResultsTable: GirlResultsTable) = viewModelScope.launch {
        repository.insertGirlResult(GirlResultsTable) }

    @DelicateCoroutinesApi
    fun updateGirlResult(GirlResultsTable: GirlResultsTable) = GlobalScope.launch {
        repository.updateGirlResult(GirlResultsTable) }

    fun deleteGirlResults() = viewModelScope.launch {
        repository.deleteGirlResults()
    }

    fun sortGirlResultByDate(date: String): LiveData<List<GirlResultsTable>>? {
        viewModelScope.launch {
            getGirlResultByDate = repository.sortGirlResultByDate(date)
        }
        return getGirlResultByDate
    }
}


class DailyTasksModelFactory(private val repository: DailyTasksRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DailyTasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DailyTasksViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}