package com.example.junkanalyse.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.junkanalyse.R
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.ui.MonitorActivity
import com.example.junkanalyse.util.FileUtils
import com.example.junkanalyse.util.TimeUtils

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 12:07
 * @desc
 */
class MonitorAdapter constructor(
    private val context: Context,
    private val data: List<MonitorEntity>
) :
    RecyclerView.Adapter<MonitorAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.file_name)
        private val pathTV: TextView = itemView.findViewById(R.id.path)
        private val timeTV: TextView = itemView.findViewById(R.id.update_time)

        fun setData(bean: MonitorEntity) {
            nameTV.text = bean.fileName
            pathTV.text = bean.path
            if (bean.isMayCouldRecycled()) {
                nameTV.setTextColor(context.resources.getColor(R.color.base_color_d81e06))
            } else {
                nameTV.setTextColor(context.resources.getColor(R.color.base_color_4577dc))
            }
            timeTV.text = TimeUtils.getNowTimeString(bean.updateTime)
            itemView.setOnClickListener {
                val targetAppInfoBean =
                    TargetAppInfoEntity(appName = bean.fileName, rootPath = bean.path)
                val intent = Intent(context, MonitorActivity::class.java)
                intent.putExtra("bean", targetAppInfoBean)
                intent.putExtra("showTop", false)
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.adapter_item_monitor, null, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}