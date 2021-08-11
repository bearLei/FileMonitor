package com.example.junkanalyse.util

import java.text.SimpleDateFormat
import java.util.*


/**
 * @author leix
 * @version 1
 * @createTime 2021/8/11 10:23
 * @desc
 */
object TimeUtils {

    fun getNowTimeString(time: Long): String {
        val date = Date(time)
        val formatter = SimpleDateFormat("MM-dd HH:mm:ss")
        return formatter.format(date)
    }
}