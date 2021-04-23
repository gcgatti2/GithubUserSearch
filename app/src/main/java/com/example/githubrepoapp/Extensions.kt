package com.example.githubrepoapp

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

/*
    Taken from: https://zhuinden.medium.com/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c
 */

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun Activity.showSnackbar(view: View, message: String, isError: Boolean = false) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        .also { snackbar ->
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).also {
                it.gravity = Gravity.CENTER_HORIZONTAL
                it.textAlignment = View.TEXT_ALIGNMENT_CENTER
                if (isError) {
                    snackbar.setBackgroundTint(getThemeColor(R.attr.colorError))
                    it.setBackgroundColor(getThemeColor(R.attr.colorError))
                }
            }
        }
        .show()
}

fun View.makeGone() {
    this.visibility = GONE
}

fun View.makeVisible() {
    this.visibility = VISIBLE
}

@ColorInt
fun Context.getThemeColor(@AttrRes attribute: Int) = TypedValue().let {
    theme.resolveAttribute(attribute, it, true); it.data
}

