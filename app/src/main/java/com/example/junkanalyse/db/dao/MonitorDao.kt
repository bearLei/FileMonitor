package com.example.junkanalyse.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:44
 * @desc
 */
@Dao
interface MonitorDao {

    @Query("SELECT * FROM monitor_table")
    fun queryAllData(): LiveData<List<MonitorEntity>>

    @Query("SELECT * FROM monitor_table where parentPath = :parent")
    fun queryDataByParentPath(parent: String): LiveData<List<MonitorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bean: MonitorEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(bean: List<MonitorEntity>)

    @Query("DELETE FROM monitor_table where path = :path")
    suspend fun delete(path: String)

    @Query("DELETE FROM monitor_table")
    suspend fun deleteAll()

}