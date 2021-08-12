package com.example.junkanalyse.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.junkanalyse.db.entity.TargetAppInfoEntity

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:44
 * @desc
 */
@Dao
interface TargetAppInfoDao {

    @Query("SELECT * FROM APP_TABLE")
    fun queryAllData(): LiveData<List<TargetAppInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bean: TargetAppInfoEntity):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(bean: List<TargetAppInfoEntity>)

}