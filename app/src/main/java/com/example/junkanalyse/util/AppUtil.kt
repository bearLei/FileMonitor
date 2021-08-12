package com.example.junkanalyse.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.JApplication
import com.example.junkanalyse.db.entity.TargetAppInfoEntity

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/11 11:58
 * @desc
 */
object AppUtil {

    fun scan() {
        val list = mutableListOf<TargetAppInfoEntity>()
        val pm: PackageManager = JApplication.mApplication.packageManager
        val installedPackages = pm.getInstalledPackages(0)
        for (pi in installedPackages) {
            val applicationInfo = pi?.applicationInfo ?: continue
            val name = applicationInfo.loadLabel(pm).toString()
            if (ApplicationInfo.FLAG_SYSTEM and applicationInfo.flags != 0) {
                continue
            }
            list.add(TargetAppInfoEntity(appName = name, rootPath = "/storage/emulated/0/Android/data/"+pi.packageName))
        }

        InjectUtils.getTargetAppInfoRepository().insertList(list)
    }


}