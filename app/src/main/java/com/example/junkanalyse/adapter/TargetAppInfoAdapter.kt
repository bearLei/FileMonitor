package com.example.junkanalyse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.junkanalyse.R
import com.example.junkanalyse.db.entity.TargetAppInfoEntity

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/10 12:07
 * @desc
 */
class TargetAppInfoAdapter constructor(
    private val context: Context,
    private val data: List<TargetAppInfoEntity>
) :
    RecyclerView.Adapter<TargetAppInfoAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.name)
        private val pathTV: TextView = itemView.findViewById(R.id.path)
        fun setData(bean: TargetAppInfoEntity) {
            nameTV.text = bean.appName
            pathTV.text = bean.rootPath
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.adapter_target_app_info, null, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}