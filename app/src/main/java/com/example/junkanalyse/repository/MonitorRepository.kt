package com.example.junkanalyse.repository

import androidx.lifecycle.LiveData
import com.example.junkanalyse.db.dao.MonitorDao
import com.example.junkanalyse.db.entity.MonitorEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 16:18
 * @desc
 */
class MonitorRepository(private val mDao: MonitorDao) {

    val allData = mDao.queryAllData()

    fun insert(bean: MonitorEntity) {
        GlobalScope.launch {
            mDao.insert(bean)
        }
    }

    fun insertList(list: List<MonitorEntity>) {
        GlobalScope.launch {
            mDao.insertList(list)
        }
    }

    fun update(bean: MonitorEntity) {

    }

    fun queryDataByParentPath(parentPath: String): LiveData<List<MonitorEntity>> {
        return mDao.queryDataByParentPath(parentPath)
    }

    fun delete(bean: MonitorEntity) {
        GlobalScope.launch {
            mDao.delete(bean.path)
        }
    }

    fun deleteAll() {
        GlobalScope.launch {
            mDao.deleteAll()
        }
    }

}