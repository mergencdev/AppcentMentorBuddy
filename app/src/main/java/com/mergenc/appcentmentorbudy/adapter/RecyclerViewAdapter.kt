package com.mergenc.appcentmentorbudy.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mergenc.appcentmentorbudy.databinding.ImagesRowBinding
import com.mergenc.appcentmentorbudy.model.GalleryImage
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(private val imageList: ArrayList<GalleryImage>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ImageHolder>() {

    class ImageHolder(val binding: ImagesRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding = ImagesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImageHolder(binding)
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