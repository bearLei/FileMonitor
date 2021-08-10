package com.example.junkanalyse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.repository.MonitorRepository
import com.example.junkanalyse.repository.TargetAppInfoRepository

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:58
 * @desc
 */
class MonitorViewModel(private val repository: MonitorRepository) : BaseViewModel() {

    val allData: LiveData<List<MonitorEntity>> = repository.allData


    fun insert(bean: MonitorEntity) {
        repository.insert(bean)
    }

    fun deleteAll() {
        repository.deleteAll()
    }

    fun getQueryDataByParentPath(parentPath: String): LiveData<List<MonitorEntity>> {
        return repository.queryDataByParentPath(parentPath)
    }

    class MonitorViewModelFactory(private val repository: MonitorRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MonitorViewModel(repository) as T
        }

    }

}