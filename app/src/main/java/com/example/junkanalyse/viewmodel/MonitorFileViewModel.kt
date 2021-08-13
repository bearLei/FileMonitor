package com.example.junkanalyse.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.junkanalyse.db.entity.FileEntity
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.repository.MonitorFileRepository
import com.example.junkanalyse.repository.MonitorRepository
import com.example.junkanalyse.repository.TargetAppInfoRepository
import com.example.junkanalyse.util.FileUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:58
 * @desc
 */
class MonitorFileViewModel(private val repository: MonitorFileRepository) : BaseViewModel() {

    val allData: LiveData<List<FileEntity>> = repository.allData


    fun insert(bean: FileEntity) {
        repository.insert(bean)
    }

    fun deleteAll() {
        GlobalScope.launch {
            repository.deleteAll()
        }
    }

    fun getQueryDataByParentPath(parentPath: String): LiveData<List<FileEntity>> {
        return repository.queryDataByParentPath(parentPath)
    }

    fun scanByRootPath(rootPath: String) {
        recursiveFile(rootPath)
    }

    private fun insert(parentPath: String, path: String, fileName: String) {
        GlobalScope.launch {
            val fileEntity = FileEntity()
            fileEntity.fileName = fileName
            fileEntity.parentPath = parentPath
            fileEntity.path = path
            if (FileUtils.isDir(path)) {
                fileEntity.type = 1
            } else {
                fileEntity.type = 0
            }
            fileEntity.size = File(path).length()
            fileEntity.updateTime = System.currentTimeMillis()
            insert(fileEntity)
        }
    }

    private fun recursiveFile(path: String) {
        val rootFile = File(path)
        if (!rootFile.exists() || !rootFile.isDirectory) {
            return
        }
        val listFiles = rootFile.listFiles()
        if (listFiles != null && listFiles.isNotEmpty()) {
            for (file in listFiles) {
                try {
                    if (file.isDirectory) {
                        recursiveFile(file.path)
                    }
                    insert(path, file.path, file.absolutePath)
                } catch (e: Exception) {

                }
            }
        }
    }

    fun checkStartRootPath(rootPath: String) {
        GlobalScope.launch {
            val list = repository.queryListByParentPath(rootPath)
            var isContainerRecycledFile = false
            for (bean in list) {
                isContainerRecycledFile = if (bean.type == 1) {
                    checkDir(bean)
                } else {
                    checkFile(bean)
                }
//                Log.d(
//                    "leix",
//                    "checkStartRootPath：--> path：-->${bean.path}-->isContainerRecycledFile:$isContainerRecycledFile"
//                )
                if (isContainerRecycledFile) {
                    bean.hasRecycled = true
                    repository.update(bean)
                }
            }
        }
    }

    private fun checkDir(fileEntity: FileEntity): Boolean {
        if (!FileUtils.isDir(fileEntity.path)) {
            fileEntity.hasRecycled = true
            repository.update(fileEntity)
        }
        val list = repository.queryListByParentPath(fileEntity.path)
        var isContainerRecycledFile = false
        for (bean in list) {
            if (bean.type == 1) {
                if (checkDir(bean)) {
                    isContainerRecycledFile = true
                }
            } else {
                if (checkFile(bean)) {
                    isContainerRecycledFile = true
                }
            }
        }
        if (isContainerRecycledFile) {
            fileEntity.hasRecycled = true
            repository.update(fileEntity)
        }
//        Log.d(
//            "leix",
//            "checkDir：--> path：-->${fileEntity.path}-->isContainerRecycledFile:$isContainerRecycledFile"
//        )
        return isContainerRecycledFile
    }


    private fun checkFile(fileEntity: FileEntity): Boolean {
        if (!FileUtils.isFile(fileEntity.path)) {
            fileEntity.hasRecycled = true
            repository.update(fileEntity)
        }
        return fileEntity.hasRecycled
    }


    class MonitorViewModelFactory(private val repository: MonitorFileRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MonitorFileViewModel(repository) as T
        }

    }

}