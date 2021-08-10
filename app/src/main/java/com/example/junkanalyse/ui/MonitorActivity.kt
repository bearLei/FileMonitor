package com.example.junkanalyse.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.junkanalyse.IMonitorAidl
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.adapter.MonitorAdapter
import com.example.junkanalyse.databinding.ActivityMonitorBinding
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.service.Constant
import com.example.junkanalyse.service.MonitorService
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

    var monitorAidl: IMonitorAidl? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            monitorAidl = IMonitorAidl.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        initView()
        registerListener()
        iniViewModel()
        subscribeUi()
        bindService()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
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
                monitorAidl?.sendData(Constant.CMD_ADD_MONITOR, it)
            }
        }

        binding.removeAll.setOnClickListener {
            mBean?.rootPath?.let {
                mMonitorViewModel.deleteAll()
            }
        }
    }

    private fun bindService() {
        val intent = Intent(this, MonitorService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }
}