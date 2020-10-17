package com.vedraj360.androlibs.ui.otherfragmets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vedraj360.androlibs.R
import mehdi.sakout.aboutpage.AboutPage


class AboutFragment : Fragment(R.layout.about_layout) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return AboutPage(requireContext())
            .isRTL(false)
            .setImage(R.drawable.ic_android)
//            .addGitHub("vedraj360")
            .setDescription("Best Destination to get Android Libraries.")
            .addGroup("Connect with us")
            .addWebsite("https://androlibs.com")
            .addPlayStore("com.vedraj360.androlibs")
            .create()
    }

}