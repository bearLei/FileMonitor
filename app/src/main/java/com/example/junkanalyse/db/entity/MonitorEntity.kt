package com.example.junkanalyse.db.entity

import android.os.Parcel
import android.os.Parcelable
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
    @ColumnInfo(name = "type") var type: Int = 0,
    @ColumnInfo(name = "size") var size: Long = 0,
    @ColumnInfo(name = "updateTime") var updateTime: Long = 0,
    @ColumnInfo(name = "hierarchy") var hierarchy: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(parentPath)
        parcel.writeString(fileName)
        parcel.writeInt(type)
        parcel.writeLong(size)
        parcel.writeLong(updateTime)
        parcel.writeInt(hierarchy)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MonitorEntity> {
        override fun createFromParcel(parcel: Parcel): MonitorEntity {
            return MonitorEntity(parcel)
        }

        override fun newArray(size: Int): Array<MonitorEntity?> {
            return arrayOfNulls(size)
        }
    }

    fun isMayCouldRecycled(): Boolean {
        return path.contains("cache")
    }
}


