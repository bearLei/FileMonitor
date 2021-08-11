package com.example.junkanalyse.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
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
import com.example.junkanalyse.util.FileUtils
import com.example.junkanalyse.view.DividerDrawable
import com.example.junkanalyse.viewmodel.TargetAppInfoViewModel
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
        bindService()
    }

    override fun onDestroy() {
        super.onDestroy()
        unBind()
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
    }

    private fun initRecycleView() {
        mAdapter = TargetAppInfoAdapter(this, mData)
        mAdapter.registerItemClickListener(object : TargetAppInfoAdapter.OnItemClickListener {
            override fun onItemClick(bean: TargetAppInfoEntity) {
                if (mCurrentMonitorBean == bean) {
                    showDialog(object : DialogExt.IAction {
                        override fun cancel() {

                        }

                        override fun ok() {
                            startMonitor(bean)
                        }

                    })
                    return
                }
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
            InjectUtils.getMonitorRepository().deleteAll()
            FileUtils.deleteFilesInDir(File(bean.rootPath))
            withContext(Dispatchers.Main) {
                mCurrentMonitorBean = bean
                monitorAidl?.sendData(Constant.CMD_ADD_MONITOR, bean.rootPath)
                val intent = Intent(this@MainActivity, MonitorActivity::class.java)
                intent.putExtra("bean", bean)
                startActivity(intent)
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

    private fun unBind() {
        unbindService(serviceConnection)
    }
}