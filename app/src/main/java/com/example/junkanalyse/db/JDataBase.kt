package com.example.junkanalyse.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.junkanalyse.JApplication
import com.example.junkanalyse.db.dao.FileDao
import com.example.junkanalyse.db.dao.MonitorDao
import com.example.junkanalyse.db.dao.TargetAppInfoDao
import com.example.junkanalyse.db.entity.FileEntity
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:39
 * @desc
 */
@Database(
    entities = [TargetAppInfoEntity::class, MonitorEntity::class,FileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class JDataBase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "fileMonitor-db"


        private val galleryDatabase by lazy { createDatabase() }

        fun getInstance() = galleryDatabase


        private fun createDatabase(): JDataBase {
            return Room.databaseBuilder(
                JApplication.mApplication,
                JDataBase::class.java, DB_NAME
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun targetAppInfoDao(): TargetAppInfoDao
    abstract fun monitorDao():MonitorDao
    abstract fun fileDao():FileDao
}