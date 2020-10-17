package com.vedraj360.androlibs.ui.otherfragmets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.vedraj360.androlibs.R
import com.vedraj360.androlibs.databinding.VedxLayoutBinding

class VedxFragment : Fragment(R.layout.vedx_layout) {

    private var _binding: VedxLayoutBinding? = null
    private val webViewBaseUrl = "https://vedraj360.ml"

    private val binding get() = _binding


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = VedxLayoutBinding.bind(view)
        binding?.apply {
            vedxWebView.apply {
                settings.javaScriptEnabled = true
                loadUrl(webViewBaseUrl)
                settings.setAppCacheEnabled(true);
//                settings.cacheMode = WebSettings.LOAD_DEFAULT
//                settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK;
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        url: String
                    ): Boolean {
                        view.loadUrl(url)
                        return true
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                        if (vedxWebViewProgressBar.isVisible) {
                            vedxWebViewProgressBar.visibility = View.GONE
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

            }


        }
    }
}