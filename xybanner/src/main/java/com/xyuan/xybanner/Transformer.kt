package com.xyuan.xybanner


import androidx.viewpager.widget.ViewPager.PageTransformer
import com.xyuan.xybanner.transformer.*

/**
 * 轮播Transformer样式
 */
object Transformer {
    //默认
    var Default: Class<out PageTransformer> = DefaultTransformer::class.java
    var Accordion: Class<out PageTransformer> = AccordionTransformer::class.java
    var BackgroundToForeground: Class<out PageTransformer> = BackgroundToForegroundTransformer::class.java
    var ForegroundToBackground: Class<out PageTransformer> = ForegroundToBackgroundTransformer::class.java
    var CubeIn: Class<out PageTransformer> = CubeInTransformer::class.java
    var CubeOut: Class<out PageTransformer> = CubeOutTransformer::class.java
    var FlipHorizontal: Class<out PageTransformer> = FlipHorizontalTransformer::class.java
    var FlipVertical: Class<out PageTransformer> = FlipVerticalTransformer::class.java
    var RotateDown: Class<out PageTransformer> = RotateDownTransformer::class.java
    var RotateUp: Class<out PageTransformer> = RotateUpTransformer::class.java
    var ScaleInOut: Class<out PageTransformer> = ScaleInOutTransformer::class.java
    var Scale: Class<out PageTransformer> = ScaleTransformer::class.java
    var ScaleRight: Class<out PageTransformer> = ScaleRightTransformer::class.java
    var Stack: Class<out PageTransformer> = StackTransformer::class.java
    var Tablet: Class<out PageTransformer> = TabletTransformer::class.java
    var ZoomIn: Class<out PageTransformer> = ZoomInTransformer::class.java
    var ZoomOut: Class<out PageTransformer> = ZoomOutTransformer::class.java
    var ZoomOutSlide: Class<out PageTransformer> = ZoomOutSlideTransformer::class.java
}
