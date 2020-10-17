package com.vedraj360.androlibs.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.util.*

class ActivityViewModel @ViewModelInject constructor(@Assisted state: SavedStateHandle) :
    ViewModel() {

    val backStack = state.getLiveData<Stack<Int>>(backStackKey, Stack())

    companion object {
        const val backStackKey = "BACK_STACK"
    }

}