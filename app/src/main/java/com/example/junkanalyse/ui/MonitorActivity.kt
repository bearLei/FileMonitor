package com.example.junkanalyse.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.adapter.MonitorAdapter
import com.example.junkanalyse.adapter.TargetAppInfoAdapter
import com.example.junkanalyse.databinding.ActivityMonitorBinding
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.service.Constant
import com.example.junkanalyse.service.MonitorService
import com.example.junkanalyse.view.DividerDrawable
import com.example.junkanalyse.viewmodel.MonitorViewModel
import com.example.junkanalyse.viewmodel.TargetAppInfoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 15:09
 * @desc
 */
class MonitorActivity : AppCompatActivity() {

    private var mBean: TargetAppInfoEntity? = null

    private lateinit var binding: ActivityMonitorBinding
    private lateinit var mAdapter: MonitorAdapter
    private val mData = ArrayList<MonitorEntity>()
    private lateinit var mMonitorViewModel: MonitorViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        initView()
        registerListener()
        iniViewModel()
        subscribeUi()
    }


    private fun parseIntent() {
        mBean = intent.getSerializableExtra("bean") as TargetAppInfoEntity?
        mBean?.let {
            binding.monitorName.text = it.appName
            binding.monitorPath.text = it.rootPath
            binding.appName.text = it.appName
        }
    }


    private fun initView() {
        mAdapter = MonitorAdapter(this, mData)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            ).also { decor ->
                decor.setDrawable(DividerDrawable(30).also { })
            })
        binding.recycleView.adapter = mAdapter
    }


    private fun iniViewModel() {
        mMonitorViewModel = ViewModelProvider(
            this,
            MonitorViewModel.MonitorViewModelFactory(InjectUtils.getMonitorRepository())
        ).get(MonitorViewModel::class.java)
    }

    private fun subscribeUi() {
        mBean?.rootPath?.let {
            mMonitorViewModel.getQueryDataByParentPath(it)
                .observe(this@MonitorActivity, { data ->
                    mData.clear()
                    mData.addAll(data)
                    mAdapter.notifyDataSetChanged()
                })
        }
    }

    private fun registerListener() {
        binding.startMonitor.setOnClickListener {
            mBean?.rootPath?.let {
                startMonitor(it)
            }
        }

        binding.removeAll.setOnClickListener {
            mBean?.rootPath?.let {
                mMonitorViewModel.deleteAll()
            }
        }
    }

    private fun startMonitor(path: String) {
        val intent = Intent(this, MonitorService::class.java)
        intent.action = Constant.ACTION_COMMAND_START
        intent.putExtra(Constant.EXTRA_FILE_PATH, path)
        startService(intent)
    }
}