package com.example.junkanalyse.repository

import androidx.lifecycle.LiveData
import com.example.junkanalyse.db.dao.TargetAppInfoDao
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:47
 * @desc
 */
class TargetAppInfoRepository(private val mDao: TargetAppInfoDao) {

    val allData: LiveData<List<TargetAppInfoEntity>> = mDao.queryAllData()

    fun insertTargetAppInfo(bean:TargetAppInfoEntity){
        GlobalScope.launch {
            mDao.insert(bean)
        }
    }

}