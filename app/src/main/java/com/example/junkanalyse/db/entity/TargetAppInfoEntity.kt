package com.example.junkanalyse.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import kotlin.random.Random

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:40
 * @desc
 */
@Entity(tableName = "app_table")
data class TargetAppInfoEntity(

    @ColumnInfo(name = "app_name")
    var appName: String,
    @PrimaryKey
    @ColumnInfo(name = "root_path")
    var rootPath: String
):Serializable
