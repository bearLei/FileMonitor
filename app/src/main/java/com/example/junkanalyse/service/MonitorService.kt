package com.example.junkanalyse.service

import android.app.Service
import android.content.Intent
import android.os.FileObserver
import android.os.FileObserver.*
import android.os.IBinder
import android.util.Log
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.service.Constant.ACTION_EVENT
import com.example.junkanalyse.service.Constant.EXTRA_EVENT_DUMP

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 10:18
 * @desc
 */
class MonitorService : Service() {

    companion object {
        const val TAG = "MonitorService"
    }

    private val mFileObserverMap: HashMap<String, FileObserver?> = HashMap()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when {
                Constant.ACTION_COMMAND_START == intent.action -> {
                    val observedPath =
                        intent.getStringExtra(Constant.EXTRA_FILE_PATH)
                    Log.d(TAG, "收到监测指令：-->$observedPath")
                    if (mFileObserverMap[observedPath] == null) {
                        val generateFileObserver = generateFileObserver(observedPath)
                        mFileObserverMap[observedPath] = generateFileObserver
                    }
                    startObservers()
                }
                Constant.ACTION_COMMAND_STOP == intent.action -> {

                }
                Constant.ACTION_COMMAND_CLEAR == intent.action -> {

                }
                Constant.ACTION_COMMAND_DUMP == intent.action -> {

                }
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }


    private fun startObservers() {
        val keys = mFileObserverMap.keys
        for (key in keys) {
            Log.d(TAG, "$key ----开始监测")
            mFileObserverMap[key]?.stopWatching()
            mFileObserverMap[key]?.startWatching()
        }
    }

    private fun generateFileObserver(rootPath: String): FileObserver {
        Log.d(TAG, "获取1个FileObserver:-->$rootPath")
        val eventMask = ALL_EVENTS
        return object : FileObserver(rootPath, eventMask) {
            override fun onEvent(event: Int, path: String?) {
                if (event and 0x8000 != 0) {
                    return
                }
                when (event and ALL_EVENTS) {
                    DELETE -> {
                        Log.d(TAG, "删除文件：$path")
                        path?.let {
                            Log.d(TAG, "创建文件：$path")
                            delete(
                                MonitorEntity(
                                    "$rootPath/$it",
                                    parentPath = rootPath,
                                    fileName = it
                                )
                            )
                        }
                    }
                    CREATE -> {
                        path?.let {
                            Log.d(TAG, "创建文件：$path")
                            insert(
                                MonitorEntity(
                                    "$rootPath/$it",
                                    parentPath = rootPath,
                                    fileName = it
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun stopObserving() {
        val keys = mFileObserverMap.keys
        for (key in keys) {
            Log.d(TAG, "$key ----停止监测")
            mFileObserverMap[key]?.stopWatching()
        }
    }

    private fun insert(data: MonitorEntity) {
        InjectUtils.getMonitorRepository().insert(data)
    }


    private fun delete(data: MonitorEntity) {
        InjectUtils.getMonitorRepository().delete(data)
    }
}