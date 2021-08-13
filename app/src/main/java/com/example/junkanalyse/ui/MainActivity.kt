package com.example.junkanalyse.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.adapter.TargetAppInfoAdapter
import com.example.junkanalyse.databinding.ActivityMainBinding
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.util.AppUtil
import com.example.junkanalyse.view.DividerDrawable
import com.example.junkanalyse.viewmodel.TargetAppInfoViewModel
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mTargetAppInfoViewModel: TargetAppInfoViewModel

    private lateinit var mAdapter: TargetAppInfoAdapter
    private val mData = ArrayList<TargetAppInfoEntity>()

    private var mCurrentMonitorBean: TargetAppInfoEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData() {
        initRecycleView()
        iniViewModel()
        subscribeUi()
        registerListener()
        requestPermissions()
    }


    private fun iniViewModel() {
        mTargetAppInfoViewModel = ViewModelProvider(
            this,
            TargetAppInfoViewModel.TargetAppInfoViewModelFactory(InjectUtils.getTargetAppInfoRepository())
        ).get(TargetAppInfoViewModel::class.java)
    }

    private fun subscribeUi() {
        mTargetAppInfoViewModel.allData.observe(this, Observer {
            mData.clear()
            mData.addAll(it)
            mAdapter.notifyDataSetChanged()
        })
        GlobalScope.launch {
            AppUtil.scan()
        }
    }

    private fun initRecycleView() {
        mAdapter = TargetAppInfoAdapter(this, mData)
        mAdapter.registerItemClickListener(object : TargetAppInfoAdapter.OnItemClickListener {
            override fun onItemClick(bean: TargetAppInfoEntity) {
                startMonitor(bean)
            }
        })
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.addItemDecoration(DividerItemDecoration(
            this,
            RecyclerView.VERTICAL
        ).also { decor ->
            decor.setDrawable(DividerDrawable(30).also { })
        })
        binding.recycleView.adapter = mAdapter
    }

    private fun registerListener() {
        binding.search.setOnClickListener {
            val editAppName = binding.editAppName.text.toString()
            if (!editAppName.isNullOrEmpty()) {
                for ((index, bean) in mData.withIndex()) {
                    if (bean.appName.contentEquals(editAppName)) {
                        binding.recycleView.smoothScrollToPosition(index)
                        break
                    }
                }
            }
        }
    }

    private fun startMonitor(bean: TargetAppInfoEntity) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                mCurrentMonitorBean = bean
                val intent = Intent(this@MainActivity, MonitorFileActivity::class.java)
                intent.putExtra("path", bean.rootPath)
                startActivity(intent)
            }
        }
    }


    private fun requestPermissions() {
        val rp = RxPermissions(this)
        rp.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe() {

        }

    }
}