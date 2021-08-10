package com.example.junkanalyse

import android.os.FileObserver
import android.util.Log

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 9:42
 * @desc
 */
class MonitorFiles(path: String?, mask: Int) : FileObserver(path, mask) {

    companion object {
        const val TAG = "MonitorFiles"
    }

    private val mFileIgnore = 0x8000
    override fun onEvent(event: Int, path: String?) {
        if (event and mFileIgnore != 0) {
            return
        }
        when (ALL_EVENTS and event) {
            CREATE -> Log.d(TAG, "创建文件夹：-->$path")
            DELETE -> Log.d(TAG, "删除文件夹：--->$path")
        }
    }
}