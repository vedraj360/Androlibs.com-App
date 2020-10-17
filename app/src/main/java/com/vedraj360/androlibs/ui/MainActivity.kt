package com.vedraj360.androlibs.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vedraj360.androlibs.R
import com.vedraj360.androlibs.utils.setupWithNav
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private var needToAddToBackStack: Boolean = true
    private lateinit var fragmentBackStack: Stack<Int>
    private lateinit var navController: NavController


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val list = ArrayList<Int>()
        try {
            for (s in fragmentBackStack) {
                list.add(s)
            }
            outState.putIntegerArrayList("Stack", list)
        } catch (e: Exception) {

        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentBackStack = Stack()
        if (savedInstanceState != null) {
            val stack = savedInstanceState.getIntegerArrayList("Stack")
            if (stack != null) {
                for (s in stack) {
//                    Log.e(TAG, "stack: $s")
                    fragmentBackStack.add(s)
                }
                needToAddToBackStack = false
            }
        }
        defaultNavigation()

        if (main_toolbar != null) {
            setSupportActionBar(main_toolbar)
        }

    }

    @SuppressLint("RestrictedApi")
    private fun defaultNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        bottomNavigationView.setupWithNavController(navController)

        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.libraryFragment, R.id.vedxFragment, R.id.favouriteFragment, R.id.aboutFragment -> {
                    bottomNavigationView.visibility = VISIBLE
                }
                else -> {
                    bottomNavigationView.visibility = GONE
                }
            }
            val actionBar = supportActionBar
            if (actionBar != null) {
                when (destination.id) {
                    R.id.favouriteFragment -> {
                        actionBar.show()
                        actionBar.title = "FAVOURITES"
                    }
                    R.id.aboutFragment, R.id.vedxFragment -> {
                        actionBar.hide()
                    }
                    R.id.showLibrary -> {
                        actionBar.hide()
                    }
                    else -> {
                        actionBar.show()
                        actionBar.title = getString(R.string.ANDROLIBS)
                    }
                }
            }

            if (needToAddToBackStack) {
                if (!fragmentBackStack.contains(destination.id)) {
                    fragmentBackStack.add(destination.id)
                } else if (fragmentBackStack.contains(destination.id)) {
                    if (destination.id == R.id.libraryFragment) {
                        val homeCount =
                            Collections.frequency(fragmentBackStack, R.id.libraryFragment)
                        if (homeCount < 2) {
                            fragmentBackStack.push(destination.id)
                        } else {

                            val x = fragmentBackStack.reversed().indexOf(
                                R.id.libraryFragment
                            )
                            Log.e(TAG, "x $x")
                            fragmentBackStack.asReversed().remove(destination.id)
                            fragmentBackStack.push(destination.id)
                        }
                    } else {
                        fragmentBackStack.remove(destination.id)
                        fragmentBackStack.push(destination.id)
                    }
                }

            }
            needToAddToBackStack = true
        }
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navGraphIds =
            listOf(R.navigation.nav_home, R.navigation.nav_vedx, R.navigation.nav_about)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNav(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment_main,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
//            setupActionBarWithNavController(navController)
            addOnDestinationChangedListener(navController)
        })
        currentNavController = controller
    }

    override fun onBackPressed() {
        if (::fragmentBackStack.isInitialized && fragmentBackStack.size > 1) {
            fragmentBackStack.pop()
            val fragmentId = fragmentBackStack.lastElement()
            needToAddToBackStack = false
            navController.navigate(fragmentId)

        } else {
            if (::fragmentBackStack.isInitialized && fragmentBackStack.size == 1) {
                finish()
            } else {
                super.onBackPressed()
            }
        }

    }

    private fun addOnDestinationChangedListener(navController: NavController) {
        // ensure only one listener is active
//        navController.removeOnDestinationChangedListener(this)
//        navController.addOnDestinationChangedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState!!)
    }


}