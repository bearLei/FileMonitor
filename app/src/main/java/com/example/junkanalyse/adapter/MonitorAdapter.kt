package com.example.junkanalyse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.junkanalyse.R
import com.example.junkanalyse.db.entity.MonitorEntity

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
        fun setData(bean: MonitorEntity) {
            nameTV.text = bean.fileName
            pathTV.text = bean.path
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