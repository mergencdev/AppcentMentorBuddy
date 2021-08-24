package com.mergenc.appcentmentorbudy.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class ImageHolder(val binding: ImagesRowBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
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

        // Image load to imageView thanks to the Picasso;
        // Source: https://github.com/square/picasso;
        Picasso.get().load(imageList[position].imageURL).resize(500, 500).centerInside()
            .into(holder.binding.imageViewRow)

        // CenterInside() is a cropping technique that scales the image so that both dimensions are
        // equal to or less than the requested bounds of the ImageView.
        // The image will be displayed completely, but might not fill the entire ImageView.
    }

    // returns our data list size;
    override fun getItemCount(): Int {
        return imageList.size
    }
}