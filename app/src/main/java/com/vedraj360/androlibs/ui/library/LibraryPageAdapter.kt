package com.vedraj360.androlibs.ui.library

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vedraj360.androlibs.R
import com.vedraj360.androlibs.databinding.LibraryItemLayoutBinding
import com.vedraj360.androlibs.models.Library
import com.vedraj360.androlibs.utils.Helper

private const val TAG = "LibraryPageAdapter"

class LibraryPageAdapter(private val itemClickListener: LibraryItemOnClickListener) :
    PagingDataAdapter<Library, LibraryPageAdapter.LibraryViewHolder>(Compare) {

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val currentItem = getItem(position)
//        Log.e(TAG, "onBindViewHolder: $currentItem")
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val binding =
            LibraryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LibraryViewHolder(binding)
    }

    inner class LibraryViewHolder(private val binding: LibraryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val library = getItem(position)
                    if (library != null) {
                        itemClickListener.onItemClick(library)
                    }
                }
            }
        }

        fun bind(library: Library) {
//            Log.e("TAG", "bind: $library")
            val photo = if (library.photo!![0] != "") {
                library.photo[0]
            } else {
                "empty"
            }

            binding.apply {
                if (photo != "empty") {
                    libraryImage.visibility = View.VISIBLE
                    try {
                        Glide.with(libraryImage).load(photo)
                            .centerCrop()
                            .error(R.drawable.ic_placeholder)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(libraryImage)
                    } catch (e: Exception) {
                        Log.e(TAG, "bind: Error $e")
                    }

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

    interface LibraryItemOnClickListener {
        fun onItemClick(library: Library)
    }

    companion object {
        private val Compare = object : DiffUtil.ItemCallback<Library>() {
            override fun areItemsTheSame(oldItem: Library, newItem: Library) =
                oldItem.libraryId == newItem.libraryId

            override fun areContentsTheSame(oldItem: Library, newItem: Library) = oldItem == newItem
        }
    }


}