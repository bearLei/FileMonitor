package com.example.junkanalyse.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/12 18:20
 * @desc
 */
@Entity(tableName = "scan_file_table")
class FileEntity {
    @PrimaryKey
    @ColumnInfo(name = "file_name")
    var fileName: String = ""

    @ColumnInfo(name = "path")
    var path: String = ""

    @ColumnInfo(name = "size")
    var size: Long = 0

    @ColumnInfo(name = "parentPath")
    var parentPath: String = ""

    @ColumnInfo(name = "updateTime")
    var updateTime:Long = 0

    @ColumnInfo(name = "type") var type:Int = 0

    @ColumnInfo(name = "hasRecycled") var hasRecycled :Boolean = false
}