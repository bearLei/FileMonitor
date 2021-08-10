package com.example.junkanalyse.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 15:48
 * @desc
 */
@Entity(tableName = "monitor_table")
class MonitorEntity(
    @PrimaryKey
    @ColumnInfo(name = "path") var path: String = "",//绝对路径
    @ColumnInfo(name = "parentPath") var parentPath: String = "",//父路径
    @ColumnInfo(name = "file_name") var fileName: String = "",
    @ColumnInfo(name = "type") var type: Int = 0
) : Serializable