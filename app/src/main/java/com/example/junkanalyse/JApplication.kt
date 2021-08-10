package com.example.junkanalyse

import android.app.Application

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 11:52
 * @desc
 */
class JApplication: Application() {
    companion object{
        lateinit var mApplication:Application
    }

    override fun onCreate() {
        super.onCreate()
        mApplication =this
    }
}