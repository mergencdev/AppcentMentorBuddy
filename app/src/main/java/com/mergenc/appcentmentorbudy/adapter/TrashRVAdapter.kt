package com.mergenc.appcentmentorbudy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mergenc.appcentmentorbudy.databinding.TrashRowBinding
import com.mergenc.appcentmentorbudy.model.TrashImage
import com.squareup.picasso.Picasso

class TrashRVAdapter(private val trashList: ArrayList<TrashImage>) :
    RecyclerView.Adapter<TrashRVAdapter.TrashHolder>() {

    class TrashHolder(val binding: TrashRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashHolder {
        val binding = TrashRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TrashHolder(binding)
    }

    override fun onBindViewHolder(holder: TrashHolder, position: Int) {
        val deletionDate = "Deletion time: ${trashList.get(position).date.toString()}"

        holder.binding.textViewTitleTrashRow.text = trashList.get(position).title
        holder.binding.textViewDescriptionTrashRow.text = trashList.get(position).description
        holder.binding.textViewDateTrashRow.text = deletionDate

        // Image load to imageView thanks to the Picasso (it is great!);
        // Source: https://github.com/square/picasso;
        Picasso.get().load(trashList.get(position).imageURL).resize(500, 500)
            .into(holder.binding.imageViewTrashRow)
    }

    override fun getItemCount(): Int {
        return trashList.size
    }

    fun updateTrashList(newTrashList: ArrayList<TrashImage>) {
        trashList.clear()
        trashList.addAll(newTrashList)
        notifyDataSetChanged()
    }
}