package com.example.cfttesttask.ui.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * Hides keyboard **only** if invoked from focused view
 */
fun View.clearFocusAndHideKeyboard() {
    clearFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}