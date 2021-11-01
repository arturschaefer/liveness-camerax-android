package com.schaefer.livenessmlkit.adapter

import androidx.recyclerview.widget.DiffUtil

class ImageListDiffCallback(
    private val oldList: List<ByteArray>,
    private val newList: List<ByteArray>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].contentEquals(newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].contentEquals(newList[newItemPosition])
    }
}
