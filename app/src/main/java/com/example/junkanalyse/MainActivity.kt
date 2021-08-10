package com.example.junkanalyse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.junkanalyse.databinding.ActivityMainBinding
import com.example.junkanalyse.service.Constant.ACTION_COMMAND_DUMP
import com.example.junkanalyse.service.Constant.ACTION_COMMAND_START
import com.example.junkanalyse.service.Constant.EXTRA_FILE_PATH
import com.example.junkanalyse.service.MonitorService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startMonitor.setOnClickListener {
            startMonitor()
        }
    }

    var path = "/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone"
    private fun startMonitor() {
        val intent = Intent(this, MonitorService::class.java)
        intent.action = ACTION_COMMAND_START
        intent.putExtra(EXTRA_FILE_PATH, path)
        startService(intent)
    }

}