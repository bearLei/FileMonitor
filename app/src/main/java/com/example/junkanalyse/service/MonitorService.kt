package com.example.junkanalyse.service

import android.app.Service
import android.content.Intent
import android.os.FileObserver
import android.os.FileObserver.ALL_EVENTS
import android.os.IBinder
import android.util.Log

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

    var path = "/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone"
    var path1 = "/storage/emulated/0/Android/data/com.tencent.mobileqq/cache"

    private val mFileObserverMap: HashMap<String, FileObserver?> = HashMap()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when {
                Constant.ACTION_COMMAND_START == intent.action -> {
                    stopObserving()
                    val observedPath =
                        intent.getStringExtra(Constant.EXTRA_FILE_PATH)
                    Log.d(TAG, "收到监测指令：-->$observedPath")
                    startObserving(path)
                    startObserving(path1)
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


    private fun startObserving(path: String) {
        Log.d(TAG, "开始监测:$path")
        val eventMask = ALL_EVENTS
        val fileObserver = object : FileObserver(path, eventMask) {
            override fun onEvent(event: Int, path: String?) {

                val FILE_IGNORED = 0x8000
                if (event and FILE_IGNORED != 0) {
                    return
                }
                when (event and ALL_EVENTS) {
                    DELETE -> Log.d(TAG, "删除文件：$path")
                    CREATE -> Log.d(TAG, "创建文件：$path")
                }
            }
        }

        mFileObserverMap[path] = fileObserver
        fileObserver.startWatching()
    }

    private fun stopObserving() {
        val keys = mFileObserverMap.keys
        for (key in keys) {
            mFileObserverMap[key]?.stopWatching()
            mFileObserverMap[key] = null
        }
    }
}