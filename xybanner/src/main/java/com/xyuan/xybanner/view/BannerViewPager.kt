package com.xyuan.xybanner.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class BannerViewPager : ViewPager {
    //是否可滑动
    private var scrollable = true

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return this.scrollable && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return this.scrollable && super.onInterceptTouchEvent(ev)
    }

    fun setScrollable(scrollable: Boolean) {
        this.scrollable = scrollable
    }

}
