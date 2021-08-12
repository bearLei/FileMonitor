package com.example.junkanalyse.ui

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.junkanalyse.DialogExt
import com.example.junkanalyse.DialogExt.showDialog
import com.example.junkanalyse.IMonitorAidl
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.adapter.TargetAppInfoAdapter
import com.example.junkanalyse.databinding.ActivityMainBinding
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.service.Constant
import com.example.junkanalyse.service.MonitorService
import com.example.junkanalyse.util.AppUtil
import com.example.junkanalyse.util.FileUtils
import com.example.junkanalyse.view.DividerDrawable
import com.example.junkanalyse.viewmodel.TargetAppInfoViewModel
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

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

        AppUtil.scan()
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
        binding.addItem.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }
    }

    private fun startMonitor(bean: TargetAppInfoEntity) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                mCurrentMonitorBean = bean
                val intent = Intent(this@MainActivity, MonitorActivity::class.java)
                intent.putExtra("bean", bean)
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