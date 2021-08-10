package com.example.junkanalyse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.repository.TargetAppInfoRepository

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:58
 * @desc
 */
class TargetAppInfoViewModel(private val repository: TargetAppInfoRepository) : BaseViewModel() {

    val allData: LiveData<List<TargetAppInfoEntity>> = repository.allData

    fun insertTargetAppInfo(bean: TargetAppInfoEntity) {
        repository.insertTargetAppInfo(bean)
    }


    class GalleryViewModelFactory(private val repository: TargetAppInfoRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TargetAppInfoViewModel(repository) as T
        }

    }

}