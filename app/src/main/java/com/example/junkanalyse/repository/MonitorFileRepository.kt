package com.example.junkanalyse.repository

import androidx.lifecycle.LiveData
import com.example.junkanalyse.db.dao.FileDao
import com.example.junkanalyse.db.dao.MonitorDao
import com.example.junkanalyse.db.entity.FileEntity
import com.example.junkanalyse.db.entity.MonitorEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 16:18
 * @desc
 */
class MonitorFileRepository(private val mDao: FileDao) {

    val allData = mDao.queryAllData()

    fun insert(bean: FileEntity) {
        GlobalScope.launch {
            mDao.insert(bean)
        }
    }

    fun queryDataByParentPath(parentPath: String): LiveData<List<FileEntity>> {
        return mDao.queryDataByParentPath(parentPath)
    }

    fun queryListByParentPath(parentPath: String): List<FileEntity> {
        return mDao.queryListByParentPath(parentPath)
    }

    fun delete(bean: FileEntity) {
        GlobalScope.launch {
            mDao.delete(bean.path)
        }
    }

    fun deleteAll() {
        GlobalScope.launch {
            mDao.deleteAll()
        }
    }


    fun update(bean: FileEntity) {
        GlobalScope.launch {
            mDao.update(bean)
        }
    }
}