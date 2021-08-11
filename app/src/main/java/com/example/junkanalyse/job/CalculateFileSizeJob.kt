package com.example.junkanalyse.job

import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.util.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/11 10:03
 * @desc
 */
class CalculateFileSizeJob(params: Job.Params) : Job {
    private var mParams: Job.Params = params
    override fun doJob(): Boolean {
        val data = mParams.mData
        if (data is MonitorEntity) {
            GlobalScope.launch(Dispatchers.IO) {
                val path = data.path
                val size = FileUtils.getDirLength(File(path))
                data.size = size
                InjectUtils.getMonitorRepository().insert(data)
            }
        }
        return true
    }
}