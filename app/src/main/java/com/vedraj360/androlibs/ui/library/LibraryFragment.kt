package com.vedraj360.androlibs.ui.library

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vedraj360.androlibs.R
import com.vedraj360.androlibs.databinding.LibraryFragmentBinding
import com.vedraj360.androlibs.models.Library
import com.vedraj360.androlibs.utils.Helper.Companion.checkIsTablet
import com.vedraj360.androlibs.utils.Helper.Companion.hasNetwork
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LibraryFragment"

@AndroidEntryPoint
class LibraryFragment :
    Fragment(R.layout.library_fragment),
    LibraryPageAdapter.LibraryItemOnClickListener {

    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null
    private val viewModel by activityViewModels<LibraryViewModel>()

    private var _binding: LibraryFragmentBinding? = null
    private val binding get() = _binding

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LibraryFragmentBinding.bind(view)
        setHasOptionsMenu(true)
        if (savedInstanceState != null) {
            viewModel.isSearched = savedInstanceState.getBoolean("isSearched")
            if (viewModel.isSearched) {
                savedInstanceState.getString("searchValue")?.let { searchValue ->
                    modifySearch(searchValue)
                }
            }
        }

        observeFromShowLibrary()

        val libraryPageAdapter = LibraryPageAdapter(this)

        linearLayoutManager = LinearLayoutManager(requireContext())
        gridLayoutManager = GridLayoutManager(requireContext(), 2)

        libraryPageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {

                if (viewModel.listState != null) {
                    if (viewModel.isSearched) {
                        if (viewModel.searchValue != "main") {
                            viewModel.isSearched = true
                        } else if (viewModel.searchValue == "main") {
                            viewModel.isSearched = false
                        }
                        modifySearch(viewModel.searchValue)
                    }
                    binding?.libraryRecyclerView?.layoutManager?.onRestoreInstanceState(viewModel.listState)
                    viewModel.listState = null
                }
            }
        })

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            libraryPageAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        binding?.apply {
            libraryRecyclerView.setHasFixedSize(true)
            libraryRecyclerView.itemAnimator = null
            if (checkIsTablet(requireActivity())) {
                libraryRecyclerView.layoutManager = gridLayoutManager
            } else {
                libraryRecyclerView.layoutManager = linearLayoutManager
            }
            libraryRecyclerView.adapter = libraryPageAdapter.withLoadStateHeaderAndFooter(
                header = LibraryLoadStateAdapter { libraryPageAdapter.retry() },
                footer = LibraryLoadStateAdapter { libraryPageAdapter.retry() }
            )
            libraryButtonRetry.setOnClickListener {
                libraryPageAdapter.retry()
            }


        }



        libraryPageAdapter.addLoadStateListener { loadState ->
            binding?.apply {
                libraryProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                libraryRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                libraryButtonRetry.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && libraryPageAdapter.itemCount < 1) {
                    libraryRecyclerView.isVisible = false
                    libraryErrorInfo.isVisible = true
                } else {
                    libraryErrorInfo.isVisible = false
                }
            }
        }


    }


    private fun observeFromShowLibrary() {
        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("reload")
            ?.observe(viewLifecycleOwner) { key ->
                viewModel.searchValue = key
            }
    }

    private fun modifySearch(key: String) {

        Handler().postDelayed({
            searchItem?.expandActionView()
            searchView?.queryHint = key
            val view = requireActivity().currentFocus
            view?.let { v ->
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }, 500)
        searchView?.setQuery(key, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotBlank()) {
                    binding?.libraryRecyclerView?.scrollToPosition(0)
                    viewModel.search(query)
                    viewModel.searchValue = query
                    viewModel.isSearched = true
                    searchView?.clearFocus()
                    return true
                }
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (viewModel.isSearched) {
                    viewModel.search("")
                    searchView?.queryHint = "Search"
                    viewModel.isSearched = false
                    viewModel.searchValue = "main"
                }
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onItemClick(library: Library) {
        val action =
            LibraryFragmentDirections.actionLibraryFragmentToShowLibrary(
                viewModel.searchValue,
                library
            )
        if (hasNetwork(requireContext())) {
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "No Network found", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isSearched", viewModel.isSearched)
        outState.putString("searchValue", viewModel.searchValue)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.listState = binding?.libraryRecyclerView?.layoutManager?.onSaveInstanceState()
//        viewModel.isSearched = this.isSearched
    }

}