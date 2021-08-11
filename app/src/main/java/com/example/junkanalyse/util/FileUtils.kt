package com.example.junkanalyse.util

import android.util.Log
import java.io.File
import java.io.FileFilter
import java.util.*

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 18:31
 * @desc
 */
object FileUtils {

    fun isDir(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.isDirectory
    }

    fun isDir(file: File?): Boolean {
        return file != null && file.exists() && file.isDirectory
    }

    fun isFile(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.isFile
    }

    fun isFile(file: File?): Boolean {
        return file != null && file.exists() && file.isFile
    }

    fun getDirLength(dir: File): Long {
        if (!isDir(dir)) return 0
        var len: Long = 0
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                len += if (file.isDirectory) {
                    getDirLength(file)
                } else {
                    file.length()
                }
            }
        }
        return len
    }

    fun getFileLength(file: File?): Long {
        return if (file != null && isFile(file)) {
            file.length()
        } else {
            0
        }
    }

    fun getFileSizeText(size: Long): String {
        return when {
            size < 1000 -> {
                //999B
                String.format(Locale.getDefault(), "%dB", size)
            }
            size < 10 * 1024L -> {
                //9.99KB
                String.format(Locale.getDefault(), "%1.2fKB", size / 1024f)
            }
            size < 100 * 1024L -> {
                //99.9KB
                String.format(Locale.getDefault(), "%1.1fKB", size / 1024f)
            }
            size < 1000 * 1024L -> {
                //999KB
                String.format(Locale.getDefault(), "%dKB", (size / 1024f).toInt())
            }
            size < 10 * 1024 * 1024L -> {
                //9.99MB
                String.format(Locale.getDefault(), "%1.2fMB", size / 1024f / 1024)
            }
            size < 100 * 1024 * 1024L -> {
                //99.9MB
                String.format(Locale.getDefault(), "%1.1fMB", size / 1024f / 1024)
            }
            size < 1000 * 1024 * 1024L -> {
                //999MB
                String.format(Locale.getDefault(), "%dMB", (size / 1024f / 1024).toInt())
            }
            size < 10 * 1024 * 1024 * 1024L -> {
                //9.99GB
                String.format(Locale.getDefault(), "%1.2fGB", size / 1024f / 1024 / 1024)
            }
            size < 100 * 1024 * 1024 * 1024L -> {
                //99.9GB
                String.format(Locale.getDefault(), "%1.1fGB", size / 1024f / 1024 / 1024)
            }
            size < 1000 * 1024 * 1024 * 1024L -> {
                //999GB
                String.format(Locale.getDefault(), "%dGB", (size / 1024f / 1024 / 1024).toInt())
            }
            else -> {
                String.format(
                    Locale.getDefault(),
                    "%1.2fTB",
                    size / 1024f / 1024 / 1024 / 1024
                )
            }
        }
    }

    fun deleteFilesInDir(dir: File?): Boolean {
        return deleteFilesInDirWithFilter(dir, object : FileFilter {
            override fun accept(pathname: File): Boolean {
                return pathname.isFile
            }
        })
    }

    fun deleteFilesInDirWithFilter(dir: File?, filter: FileFilter?): Boolean {
        if (dir == null || filter == null) return false
        // dir doesn't exist then return true
        if (!dir.exists()) return true
        // dir isn't a directory then return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                try {
                    if (file.isFile) {
                        if (!file.delete()) return false
                    } else if (file.isDirectory) {
                        if (!deleteDir(file)) return false
                    }
                } catch (e: Exception) {
                    Log.d("leix", "delete error:" + e.message)
                }
            }
        }
        return true
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir == null) return false
        // dir doesn't exist then return true
        if (!dir.exists()) return true
        // dir isn't a directory then return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.isFile) {
                    if (!file.delete()) return false
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return dir.delete()
    }
}