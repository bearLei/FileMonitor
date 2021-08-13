package com.example.junkanalyse.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.adapter.MonitorAdapter
import com.example.junkanalyse.adapter.MonitorFileAdapter
import com.example.junkanalyse.databinding.ActivityMonitorBinding
import com.example.junkanalyse.databinding.ActivityMonitorFileBinding
import com.example.junkanalyse.db.entity.FileEntity
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.view.DividerDrawable
import com.example.junkanalyse.viewmodel.MonitorFileViewModel

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/12 18:15
 * @desc
 */
class MonitorFileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonitorFileBinding
    private lateinit var mAdapter: MonitorFileAdapter
    private val mData = ArrayList<FileEntity>()
    private lateinit var mMonitorFileViewModel: MonitorFileViewModel

    private var mRootPath: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mRootPath = intent?.getStringExtra("path")
        iniViewModel()
        initView()
        subscribeUi()
        registerListener()
    }


    private fun iniViewModel() {
        mMonitorFileViewModel = ViewModelProvider(
            this,
            MonitorFileViewModel.MonitorViewModelFactory(InjectUtils.getMonitorFileRepository())
        ).get(MonitorFileViewModel::class.java)
    }

    private fun initView() {
        mAdapter = MonitorFileAdapter(this, mData)
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

    private fun subscribeUi() {
        mMonitorFileViewModel.getQueryDataByParentPath(mRootPath!!).observe(this,
            {
                mData.clear()
                mData.addAll(it)
                mAdapter.notifyDataSetChanged()
            })
    }

    private fun registerListener() {
        binding.scan.setOnClickListener {
            mMonitorFileViewModel.scanByRootPath(mRootPath!!)
        }
        binding.refresh.setOnClickListener {
            mMonitorFileViewModel.checkStartRootPath(mRootPath!!)
        }
        binding.deleteAll.setOnClickListener {
            mMonitorFileViewModel.deleteAll()
        }
    }
}