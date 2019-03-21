package com.xyuan.xybanner.loader

import android.content.Context
import android.view.View
import android.widget.ImageView

import java.io.Serializable


interface ImageLoaderInterface<T : ImageView> : Serializable {

    fun displayImage(context: Context, path: Any, imageView: T)

    fun createImageView(context: Context): T
}
