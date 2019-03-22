package com.xyuan.xybanner.transformer

import android.view.View

/**
 * 手风琴折叠样式
 */
class AccordionTransformer : ABaseTransformer() {

    override fun onTransform(view: View, position: Float) {
        view.pivotX = (if (position < 0) 0 else view.width).toFloat()
        view.scaleX = if (position < 0) 1f + position else 1f - position
    }

}
