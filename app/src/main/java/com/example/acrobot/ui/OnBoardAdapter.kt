package com.example.acrobot.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.acrobot.data.WelcomeModel
import com.example.acrobot.databinding.ItemOnboardingBinding
import javax.inject.Inject

class OnBoardAdapter @Inject constructor() : RecyclerView.Adapter<OnBoardAdapter.OnBoardViewHolder>() {

    inner class OnBoardViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: WelcomeModel){
                binding.imgOnboard.setImageResource(item.image)
                binding.splashtitle.text = item.title
                binding.descriptionsplash.text = item.description
            }
    }

    var onBoardList: List<WelcomeModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val differCallBack = object : DiffUtil.ItemCallback<WelcomeModel>() {
        override fun areItemsTheSame(oldItem: WelcomeModel, newItem: WelcomeModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: WelcomeModel,
            newItem: WelcomeModel,
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardViewHolder {
        return OnBoardViewHolder(
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var onItemClickListener: ((WelcomeModel) -> Unit)? = null

    override fun onBindViewHolder(holder: OnBoardViewHolder, position: Int) {
        val onBoard = onBoardList[position]
        holder.apply {
            bind(onBoard)
        }
    }

    override fun getItemCount() = onBoardList.size

    fun setOnItemClickListener(listener: (WelcomeModel) -> Unit) {
        onItemClickListener = listener
    }
}