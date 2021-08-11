package com.example.junkanalyse.service

import android.app.Service
import android.content.Intent
import android.os.FileObserver
import android.os.IBinder
import android.util.Log
import com.example.junkanalyse.IMonitorAidl
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.util.FileUtils


/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 17:40
 * @desc
 */
class MonitorService : Service() {
    companion object {
        const val TAG = "MonitorService"
    }


    private val mFileObserverMap: HashMap<String, FileObserver?> = HashMap()

    private val stub = object : IMonitorAidl.Stub() {

        override fun sendData(cmd: Int, path: String?) {
            Log.d(TAG, "接收到指令：$cmd---path:-->$path")
            when (cmd) {
                Constant.CMD_ADD_MONITOR -> {
                    mFileObserverMap.clear()
                    path?.let {
                        val generateFileObserver = generateFileObserver(it)
                        mFileObserverMap[it] = generateFileObserver
                        startObservers()
                    }
                }
            }

        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return stub
    }

    private fun checkBeforeStart(path: String?) {
        path?.let {
            if (mFileObserverMap[path] == null) {
                val generateFileObserver = generateFileObserver(path)
                mFileObserverMap[path] = generateFileObserver
            }
            startObservers()
        }
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
        val eventMask = FileObserver.ALL_EVENTS
        return object : FileObserver(rootPath, eventMask) {
            override fun onEvent(event: Int, path: String?) {
                if (event and 0x8000 != 0) {
                    return
                }
                when (event and ALL_EVENTS) {
                    DELETE -> {
                        path?.let {
                            Log.d(TAG, "删除文件：$path")
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
                            val abPath = "$rootPath/$it"
                            insert(
                                MonitorEntity(
                                    abPath,
                                    parentPath = rootPath,
                                    fileName = it, updateTime = System.currentTimeMillis()
                                )
                            )
                            if (FileUtils.isDir(abPath)) {
                                Log.d(TAG, "$abPath----->是文件夹，添加监测")
                                checkBeforeStart(abPath)
                            }
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