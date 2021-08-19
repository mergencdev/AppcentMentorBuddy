package com.mergenc.appcentmentorbudy.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mergenc.appcentmentorbudy.databinding.ImagesRowBinding
import com.mergenc.appcentmentorbudy.model.GalleryImage
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(
    private val imageList: ArrayList<GalleryImage>
) : RecyclerView.Adapter<RecyclerViewAdapter.ImageHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class ImageHolder(val binding: ImagesRowBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding = ImagesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImageHolder(binding, mListener)
    }

    // bind ImageURL, title and description;
    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.binding.textViewTitleRow.text = imageList.get(position).title
        holder.binding.textViewDescriptionRow.text = imageList.get(position).description

        // Image load to imageView thanks to the Picasso (it is great!);
        // Source: https://github.com/square/picasso;
        Picasso.get().load(imageList.get(position).imageURL).resize(500, 500)
            .into(holder.binding.imageViewRow)
    }

    // returns our data list size;
    override fun getItemCount(): Int {
        return imageList.size
    }
}