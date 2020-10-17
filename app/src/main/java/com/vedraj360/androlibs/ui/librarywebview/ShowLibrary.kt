package com.vedraj360.androlibs.ui.librarywebview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.vedraj360.androlibs.R
import com.vedraj360.androlibs.databinding.ShowLibraryLayoutBinding
import com.vedraj360.androlibs.ui.library.LibraryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowLibrary : Fragment(R.layout.show_library_layout) {

    private val viewModel by viewModels<LibraryViewModel>()


    private val args by navArgs<ShowLibraryArgs>()
    private var _binding: ShowLibraryLayoutBinding? = null
    private val binding get() = _binding!!
    private val webViewBaseUrl = "https://androlibs.ml/library/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ShowLibraryLayoutBinding.bind(view)

        val libName = args.library.name
        val library = args.library
        val url = webViewBaseUrl + libName

        Log.e("TAG", "onViewCreated: $url")
        handelBackPress()

        binding.apply {
            webView.apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        url: String
                    ): Boolean {
                        view.loadUrl(url)
                        return true
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                        if (webViewProgressBar.isVisible) {
                            webViewProgressBar.visibility = View.GONE
                        }
                    }

                    override fun onReceivedError(
                        view: WebView,
                        errorCode: Int,
                        description: String,
                        failingUrl: String
                    ) {
                        Toast.makeText(
                            requireContext(),
                            "Error",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(url)
            }

            var isSaved = false
            viewModel.isLibSaved(library.libraryId).observe(viewLifecycleOwner) {
                Log.e("TAG", "onViewCreated: $it")
                isSaved = it
            }

            fab.setOnClickListener {
                if (isSaved) {
                    Snackbar.make(view, "Library Already Added", Snackbar.LENGTH_SHORT).show()
                } else {
                    viewModel.saveLibrary(library)
                    Snackbar.make(view, "Library Added to Favorites", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun handelBackPress() {
        val searchValue = args.searchValue
        Log.e("TAG", "handelBackPress: $searchValue")
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (searchValue != "main") {
                    val navController = findNavController()
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "reload",
                        searchValue
                    )
                    navController.popBackStack()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}