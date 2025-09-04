package com.example.famchat.extensions

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import com.example.famchat.R

fun View.rotateAnimation(duration: Long = 300L, repeatCount: Int = Animation.INFINITE) {
    val animation = AnimationUtils.loadAnimation(context, R.anim.n_rotation_repeat)
    animation.interpolator = LinearInterpolator()
    animation.repeatCount = repeatCount
    animation.fillAfter = true
    animation.duration = 300L
    this.startAnimation(animation)
}