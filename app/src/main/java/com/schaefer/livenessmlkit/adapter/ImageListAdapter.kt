package com.schaefer.livenessmlkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.schaefer.livenessmlkit.databinding.LayoutItemImageBinding

internal class ImageListAdapter : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    var imageList = emptyList<ByteArray>()
        set(value) {
            val result = DiffUtil.calculateDiff(
                ImageListDiffCallback(
                    field,
                    value
                )
            )
            result.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = LayoutItemImageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        with(holder) {
            with(imageList[position]) {
                this.let {
                    Glide.with(holder.itemView)
                        .load(it)
                        .into(binding.ivResult)
                }
            }
        }
    }

    override fun getItemCount(): Int = imageList.size

    inner class ImageViewHolder(
        val binding: LayoutItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
