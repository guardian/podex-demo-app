package com.guardian.podxdemo.presentation.common

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?)?.apply {
        this.hideSoftInputFromWindow(view?.rootView?.windowToken, 0)
    }
}