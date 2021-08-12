package com.example.junkanalyse.service

import android.app.Service
import android.content.Intent
import android.os.FileObserver
import android.os.IBinder
import android.util.Log
import com.example.junkanalyse.IMonitorAidl
import java.io.File
import java.lang.Exception


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

    private val mRecycledData = mutableListOf<String>()
    private val stub = object : IMonitorAidl.Stub() {

        override fun sendData(cmd: Int, path: String?) {
            Log.d(TAG, "接收到指令：$cmd---path:-->$path")
            when (cmd) {
                Constant.CMD_START_MONITOR -> {
                    mFileObserverMap.clear()
                    path?.let {
                        start(it)
                        startObservers()
                    }
                }
                Constant.CMD_STOP_MONITOR->{
                    stopObserving()
                }
            }

        }

        override fun getResult(): List<String> {
            return mRecycledData
        }
    }

    private fun start(path: String) {
        recursiveFile(path)
        startObservers()
    }

    private fun recursiveFile(path: String, hierarchy: Int = 1) {
        val rootFile = File(path)
        if (!rootFile.exists() || !rootFile.isDirectory) {
            return
        }
        generateObserver(path)
        var hierarchyFinal = hierarchy
        val listFiles = rootFile.listFiles()
        if (listFiles != null && listFiles.isNotEmpty()) {
            for (file in listFiles) {
                try {
                    if (file.isDirectory) {
                        recursiveFile(file.path, hierarchyFinal++)
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return stub
    }

    private fun startObservers() {
        val keys = mFileObserverMap.keys
        for (key in keys) {
            Log.d(TAG, "$key ----开始监测")
            mFileObserverMap[key]?.stopWatching()
            mFileObserverMap[key]?.startWatching()
        }
    }

    private fun generateObserver(rootPath: String) {
        val eventMask = FileObserver.ALL_EVENTS
        val observer = object : FileObserver(rootPath, eventMask) {
            override fun onEvent(event: Int, path: String?) {
                if (event and 0x8000 != 0) {
                    return
                }
                when (event and ALL_EVENTS) {
                    DELETE -> {
                        Log.d(TAG, "删除文件：$rootPath/$path")
                        mRecycledData.add("$rootPath/$path")
                    }
                }
            }
        }
        mFileObserverMap[rootPath] = observer
    }


    private fun stopObserving() {
        val keys = mFileObserverMap.keys
        for (key in keys) {
            Log.d(TAG, "$key ----停止监测")
            mFileObserverMap[key]?.stopWatching()
        }
    }

}