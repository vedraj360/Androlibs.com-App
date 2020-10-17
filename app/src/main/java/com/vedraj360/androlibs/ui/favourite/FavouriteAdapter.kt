package com.vedraj360.androlibs.ui.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vedraj360.androlibs.databinding.LibraryItemLayoutBinding
import com.vedraj360.androlibs.models.Library
import com.vedraj360.androlibs.utils.Helper

class FavouriteAdapter(private val clickListener: FavouriteItemOnClickListener) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val binding =
            LibraryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class FavouriteViewHolder(private val binding: LibraryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val library = differ.currentList[position]
                    if (library != null) {
                        clickListener.onItemClick(library)
                    }
                }
            }
        }

        fun bind(library: Library) {
            val photo = if (library.photo!![0] != "") {
                library.photo[0]
            } else {
                "empty"
            }
            binding.apply {
                if (photo != "empty") {
                    libraryImage.visibility = View.VISIBLE
                    Glide.with(libraryImage).load(photo)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(libraryImage)
                } else
                    libraryImage.visibility = View.GONE
                libraryName.text = library.name
                libraryCategoryName.text = library.category!!.name
                libraryUserName.text = library.user!!.name
                libraryDescription.text = library.description
                libraryDate.text = library.createdAt?.let { Helper.formatDate(it) }
            }
        }

    }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<Library>() {
        override fun areItemsTheSame(oldItem: Library, newItem: Library): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Library, newItem: Library): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    interface FavouriteItemOnClickListener {
        fun onItemClick(library: Library)
    }
}