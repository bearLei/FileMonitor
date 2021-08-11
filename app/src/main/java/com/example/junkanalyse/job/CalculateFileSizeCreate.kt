package com.example.junkanalyse.job

import com.example.junkanalyse.db.entity.MonitorEntity

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/11 10:12
 * @desc
 */
class CalculateFileSizeCreate(entity: MonitorEntity) : JobCreator {
    private var mData: Any = entity
    override fun create(): Job? {
        val params = Job.Params(mData)
        return CalculateFileSizeJob(params)
    }
}