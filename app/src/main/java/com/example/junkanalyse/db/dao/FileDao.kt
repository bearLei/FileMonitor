package com.example.junkanalyse.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.junkanalyse.db.entity.FileEntity
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:44
 * @desc
 */
@Dao
interface FileDao {

    @Query("SELECT * FROM scan_file_table")
    fun queryAllData(): LiveData<List<FileEntity>>

    @Query("SELECT * FROM scan_file_table where parentPath = :parent")
    fun queryDataByParentPath(parent: String): LiveData<List<FileEntity>>

    @Query("SELECT * FROM scan_file_table where parentPath = :parent")
    fun queryListByParentPath(parent: String): List<FileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bean: FileEntity): Long


    @Update
    suspend fun update(bean: FileEntity)

    @Query("DELETE FROM scan_file_table where path = :path")
    suspend fun delete(path: String)

    @Query("DELETE FROM scan_file_table")
    suspend fun deleteAll()

}