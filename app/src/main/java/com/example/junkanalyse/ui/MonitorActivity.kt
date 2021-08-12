package com.example.junkanalyse.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
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
import kotlin.properties.Delegates

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 15:09
 * @desc
 */
class MonitorActivity : AppCompatActivity() {


    companion object {
        const val SPIL = "/"
    }

    private var mBean: TargetAppInfoEntity? = null
    private lateinit var binding: ActivityMonitorBinding
    private lateinit var mAdapter: MonitorAdapter
    private val mData = ArrayList<MonitorEntity>()
    private lateinit var mMonitorViewModel: MonitorViewModel
    private var mShowTop: Boolean = true
    private var mRootPath: String = ""
    private var mIsMonitoring: Boolean by Delegates.observable(false, { _, _, isMonitoring ->
        run {
            if (isMonitoring) {
                binding.start.text = "监控中"
            } else {
                binding.start.text = "开始监控"
            }
        }
    })

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
        bindService()
    }

    private fun parseIntent() {
        mBean = intent.getSerializableExtra("bean") as TargetAppInfoEntity?
        mShowTop = intent.getBooleanExtra("showTop", true)
        mBean?.let {
            mRootPath = it.rootPath
            binding.monitorName.text = it.appName
            binding.monitorPath.text = it.rootPath
            binding.parentPath.text = it.rootPath
        }
        if (mShowTop) {
            binding.topGroup.visibility = View.VISIBLE
        } else {
            binding.topGroup.visibility = View.GONE
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
            mMonitorViewModel.getQueryDataByParentPath(mRootPath)
                .observe(this@MonitorActivity, { data ->
                    mData.clear()
                    mData.addAll(data)
                    mAdapter.notifyDataSetChanged()
                    binding.showResult.text = "本页数量：${mData.size}"
                })
        }
    }

    private fun registerListener() {

        binding.stop.setOnClickListener {
            mIsMonitoring = false
            unMonitor()
        }
        binding.start.setOnClickListener {
            mBean?.rootPath?.let {
                if (mIsMonitoring) {
                    return@let
                }
                mIsMonitoring = true
                monitorAidl?.sendData(Constant.CMD_START_MONITOR, it)
            }
        }
        binding.getResult.setOnClickListener {
            val result = monitorAidl?.result
            binding.getResult.text = "获取数量：${result?.size}"
            result?.let {
                filterData(result)
            }
            for (bean in mResultData) {
                InjectUtils.getMonitorRepository().insert(bean)
            }
        }
    }


    //服务相关
    var monitorAidl: IMonitorAidl? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            monitorAidl = IMonitorAidl.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    private fun bindService() {
        val intent = Intent(this, MonitorService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    private fun unMonitor() {
        monitorAidl?.sendData(Constant.CMD_STOP_MONITOR, "")
    }

    private var mResultData = mutableListOf<MonitorEntity>()

    private fun filterData(list: MutableList<String>) {
        for (str in list) {
            checkParentPathExist(str)
        }
    }

    private fun checkParentPathExist(path: String) {
        val lastIndexOf = path.lastIndexOf(SPIL)//先找最后1个截断点
        if (lastIndexOf != -1) {
            val parentPath = path.subSequence(0, lastIndexOf).toString()
            val fileName = path.substring(lastIndexOf, path.length)
            var exist = false
            for (bean in mResultData) {
                if (bean.path == path) {
                    exist = true
                }
            }
            if (!exist) {
                checkParentPathExist(parentPath)
            }
            mResultData.add(
                MonitorEntity(
                    parentPath = parentPath,
                    path = path,
                    fileName = fileName,
                    updateTime = System.currentTimeMillis()
                )
            )
        }
    }

}