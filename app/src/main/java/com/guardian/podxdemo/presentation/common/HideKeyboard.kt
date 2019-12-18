package com.guardian.podxdemo.presentation.common

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import timber.log.Timber

fun Fragment.hideKeyboard() {
    try {
        (requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .apply {
                this.hideSoftInputFromWindow(requireView().rootView.windowToken, 0)
            }
    } catch (e: IllegalStateException) {
        Timber.e(e)
    }
}