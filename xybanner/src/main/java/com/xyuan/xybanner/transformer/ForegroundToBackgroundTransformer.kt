package com.xyuan.xybanner.transformer

import android.view.View

class ForegroundToBackgroundTransformer : ABaseTransformer() {

    override fun onTransform(view: View, position: Float) {
        val height = view.height.toFloat()
        val width = view.width.toFloat()
        val scale = ABaseTransformer.min(if (position > 0) 1f else Math.abs(1f + position), 0.5f)

        view.scaleX = scale
        view.scaleY = scale
        view.pivotX = width * 0.5f
        view.pivotY = height * 0.5f
        view.translationX = if (position > 0) width * position else -width * position * 0.25f
    }

}
