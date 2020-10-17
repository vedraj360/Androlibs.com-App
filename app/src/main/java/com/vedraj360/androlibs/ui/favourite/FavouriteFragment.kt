package com.vedraj360.androlibs.ui.favourite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vedraj360.androlibs.R
import com.vedraj360.androlibs.databinding.FavouriteLayoutBinding
import com.vedraj360.androlibs.models.Library
import com.vedraj360.androlibs.ui.library.LibraryViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FavouriteFragment"

@AndroidEntryPoint
class FavouriteFragment : Fragment(R.layout.favourite_layout),

    FavouriteAdapter.FavouriteItemOnClickListener {

    private var _binding: FavouriteLayoutBinding? = null
    private val binding get() = _binding

    private val viewModel by activityViewModels<LibraryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FavouriteLayoutBinding.bind(view)
        initUI(view)

    }

    private fun initUI(view: View) {
        val favouriteAdapter = FavouriteAdapter(this)

        binding?.apply {
            favouriteRecyclerView.setHasFixedSize(true)
            favouriteRecyclerView.itemAnimator = null
            favouriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            favouriteRecyclerView.adapter = favouriteAdapter
        }

        viewModel.getFavouriteLibrary().observe(viewLifecycleOwner) {
            favouriteAdapter.differ.submitList(it)
        }


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val library = favouriteAdapter.differ.currentList[position]
                try {
                    viewModel.deleteLibrary(library)
                    Snackbar.make(view, "Successfully deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.saveLibrary(library)
                        }
                        show()
                    }
                } catch (e: Exception) {
                    Snackbar.make(view, "Error! Try again", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding?.favouriteRecyclerView)
        }

    }

    override fun onItemClick(library: Library) {
        val action =
            FavouriteFragmentDirections.actionFavouriteFragmentToShowLibrary("main", library)
        findNavController().navigate(action)
    }

}