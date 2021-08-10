package com.example.junkanalyse

import com.example.junkanalyse.db.JDataBase
import com.example.junkanalyse.repository.MonitorRepository
import com.example.junkanalyse.repository.TargetAppInfoRepository

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/23 15:45
 * @desc
 */
object InjectUtils {

    fun getTargetAppInfoRepository(): TargetAppInfoRepository {
        return TargetAppInfoRepository(JDataBase.getInstance().targetAppInfoDao())
    }

    fun getMonitorRepository(): MonitorRepository {
        return MonitorRepository(JDataBase.getInstance().monitorDao())
    }
}