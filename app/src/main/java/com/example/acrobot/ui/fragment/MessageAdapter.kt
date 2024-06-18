package com.example.acrobot.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acrobot.R
import com.example.acrobot.data.models.Chat


class MessageAdapter(context: Context) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private lateinit var sharedPreferences: SharedPreferences
    var context: Context

    init {
        this.context = context
        sharedPreferences =
            context.getSharedPreferences("user", Context.MODE_PRIVATE)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return if (viewType == MES_TYPE_RIGHT) {
            ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false)
            )
        } else {
            ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false)
            )
        }
    }
    var list: List<Chat> = arrayListOf()

    override fun onBindViewHolder(
        holder: MessageAdapter.ViewHolder,
        position: Int
    ) {
        holder.massge.text = list[position].message
        holder.name.text=list[position].sender
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profile_image: ImageView

        var massge: TextView
        var name: TextView

        init {
            profile_image = itemView.findViewById<ImageView>(R.id.profile_image)
            massge = itemView.findViewById<TextView>(R.id.show_message)
            name = itemView.findViewById<TextView>(R.id.name)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].sender == sharedPreferences.getString("name",null)) {
            MES_TYPE_RIGHT
        } else {
            MES_TYPE_LEFT
        }

    }

    companion object {
        const val MES_TYPE_LEFT = 0
        const val MES_TYPE_RIGHT = 1
    }
}