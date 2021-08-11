package com.example.junkanalyse.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.adapter.MonitorAdapter
import com.example.junkanalyse.databinding.ActivityMonitorBinding
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.job.CalculateFileSizeCreate
import com.example.junkanalyse.job.JobChain
import com.example.junkanalyse.util.FileUtils
import com.example.junkanalyse.view.DividerDrawable
import com.example.junkanalyse.viewmodel.MonitorViewModel

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
        initData()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initData()
    }


    private fun initData() {
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
        binding.count.setOnClickListener {
            for (bean in mData) {
                if (FileUtils.isDir(bean.path)) {
                    JobChain.newInstance().addJob(CalculateFileSizeCreate(bean).create())
                }
            }
        }
    }

}