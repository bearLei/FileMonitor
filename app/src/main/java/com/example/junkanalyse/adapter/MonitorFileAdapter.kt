package com.example.junkanalyse.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.FileUtil
import com.example.junkanalyse.InjectUtils
import com.example.junkanalyse.R
import com.example.junkanalyse.db.entity.FileEntity
import com.example.junkanalyse.db.entity.MonitorEntity
import com.example.junkanalyse.db.entity.TargetAppInfoEntity
import com.example.junkanalyse.ui.MonitorActivity
import com.example.junkanalyse.ui.MonitorFileActivity
import com.example.junkanalyse.util.FileUtils
import com.example.junkanalyse.util.TimeUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 12:07
 * @desc
 */
class MonitorFileAdapter constructor(
    private val context: Context,
    private val data: List<FileEntity>
) :
    RecyclerView.Adapter<MonitorFileAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.file_name)
        private val pathTV: TextView = itemView.findViewById(R.id.path)
        private val timeTV: TextView = itemView.findViewById(R.id.update_time)
        private var fileType: ImageView = itemView.findViewById(R.id.file_type)
        private var sizeTv: TextView = itemView.findViewById(R.id.size)
        fun setData(bean: FileEntity) {
            nameTV.text = bean.fileName
            pathTV.text = bean.path
            timeTV.text = TimeUtils.getNowTimeString(bean.updateTime)
            if (bean.type == 1) {
                fileType.setBackgroundResource(R.drawable.file_folder)
            } else {
                fileType.setBackgroundResource(R.drawable.file_icon)
            }
            if (bean.hasRecycled) {
                nameTV.setTextColor(context.resources.getColor(R.color.base_color_d81e06))
            } else {
                nameTV.setTextColor(context.resources.getColor(R.color.base_color_4577dc))
            }
            sizeTv.text = FileUtils.getFileSizeText(bean.size)
            itemView.setOnClickListener {
                if (bean.type == 1) {
                    val intent = Intent(context, MonitorFileActivity::class.java)
                    intent.putExtra("path", bean.path)
                    context.startActivity(intent)
                }
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