package com.example.pickmylunchmenu.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.fadeIn() {
    this.alpha = 0f
    this.visibility = View.VISIBLE
    this.animate().alpha(1f).setDuration(150).setListener(null)
}

fun View.fadeOut() {
    this.animate().alpha(0f).setDuration(150).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            visibility = View.GONE
        }
    })
}

fun View.fadeOutInvisible() {
    this.animate().alpha(0f).setDuration(150).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            visibility = View.INVISIBLE
        }
    })
}
