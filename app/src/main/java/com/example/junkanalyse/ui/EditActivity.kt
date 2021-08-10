package com.example.junkanalyse.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.databinding.ActivityEditBinding
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.viewmodel.TargetAppInfoViewModel

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 14:27
 * @desc
 */
class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var mTargetAppInfoViewModel: TargetAppInfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniViewModel()
        binding.ok.setOnClickListener {
            insert()
        }
        binding.setDefault.setOnClickListener {
            insertDefault()
        }
    }

    private fun iniViewModel() {
        mTargetAppInfoViewModel = ViewModelProvider(
            this,
            TargetAppInfoViewModel.GalleryViewModelFactory(InjectUtils.getTargetAppInfoRepository())
        ).get(TargetAppInfoViewModel::class.java)
    }


    fun insert() {
        val appName = binding.editName.text.toString()
        val path = binding.editPath.text.toString()
        if (appName.isEmpty() || path.isEmpty()) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show()
            return
        }
        mTargetAppInfoViewModel.insertTargetAppInfo(
            TargetAppInfoEntity(
                appName = appName,
                rootPath = path
            )
        )

        finish()
    }

    private fun insertDefault() {
        mTargetAppInfoViewModel.insertTargetAppInfo(
            TargetAppInfoEntity(
                rootPath = "/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone",
                appName = "手机QQ空间"
            )
        )

        mTargetAppInfoViewModel.insertTargetAppInfo(
            TargetAppInfoEntity(
                rootPath = "/storage/emulated/0/Android/data/com.tencent.mobileqq/cache",
                appName = "手机QQ缓存"
            )
        )
    }
}