package com.example.junkanalyse.ui

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
import com.example.junkanalyse.service.Constant.ACTION_COMMAND_START
import com.example.junkanalyse.service.Constant.EXTRA_FILE_PATH
import com.example.junkanalyse.service.MonitorService
import com.example.junkanalyse.view.DividerDrawable
import com.example.junkanalyse.viewmodel.TargetAppInfoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mTargetAppInfoViewModel: TargetAppInfoViewModel

    private lateinit var mAdapter: TargetAppInfoAdapter
    private val mData = ArrayList<TargetAppInfoEntity>()

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
}