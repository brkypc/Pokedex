package com.example.challenge3.utils

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

class ProgressAnimation(
    private val progressBar: ProgressBar,
    private val to: Int
) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)

        val value = to * interpolatedTime * 100
        progressBar.progress = value.toInt()
    }
}