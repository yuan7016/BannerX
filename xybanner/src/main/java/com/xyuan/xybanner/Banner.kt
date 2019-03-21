package com.xyuan.xybanner

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import android.widget.ImageView.ScaleType
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager.widget.ViewPager.PageTransformer
import com.xyuan.xybanner.listener.OnBannerClickListener
import com.xyuan.xybanner.loader.ImageLoader
import com.xyuan.xybanner.loader.ImageLoaderInterface
import com.xyuan.xybanner.view.BannerViewPager
import java.lang.reflect.Field
import java.util.ArrayList

/**
 * Banner for androidx
 */
class Banner : FrameLayout, OnPageChangeListener {

    var tag = "banner"
    private var mIndicatorMargin = BannerConfig.PADDING_SIZE
    private var mIndicatorWidth: Int = 0
    private var mIndicatorHeight: Int = 0
    private val indicatorSize: Int
    private var bannerBackgroundImage: Int = 0
    private var bannerStyle = BannerConfig.CIRCLE_INDICATOR
    private var delayTime = BannerConfig.TIME
    private var scrollTime = BannerConfig.DURATION
    private var isAutoPlay = BannerConfig.IS_AUTO_PLAY
    private var isScroll = BannerConfig.IS_SCROLL
    private var mIndicatorSelectedResId = R.drawable.gray_radius
    private var mIndicatorUnselectedResId = R.drawable.white_radius
    private var mLayoutResId = R.layout.banner_layout
    private var titleHeight: Int = 0
    private var titleBackground: Int = 0
    private var titleTextColor: Int = 0
    private var titleTextSize: Int = 0
    private var count = 0
    private var currentItem: Int = 0
    private var gravity = -1
    private var lastPosition = 1
    private var scaleType = 1
    private var titles: MutableList<String>? = null
    private var imageUrls: MutableList<Any>? = null
    private val imageViews: MutableList<View>
    private val indicatorImages: MutableList<ImageView>
    private var viewPager: BannerViewPager? = null
    private var bannerTitle: TextView? = null
    private var numIndicatorInside: TextView? = null
    private var numIndicator: TextView? = null
    private var indicator: LinearLayout? = null
    private var indicatorInside: LinearLayout? = null
    private var titleView: LinearLayout? = null
    private var bannerDefaultImage: ImageView? = null
    private var imageLoaderInterFace: ImageLoaderInterface<ImageView>? = null
    private var adapter: BannerPagerAdapter? = null
    private var mOnPageChangeListener: OnPageChangeListener? = null
    private var mScroller: BannerScroller? = null
    private var listener: OnBannerClickListener? = null
    private val dm: DisplayMetrics
    private val handler = WeakHandler()

    private val task = object : Runnable {
        override fun run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1
                if (currentItem == 1) {
                    viewPager!!.setCurrentItem(currentItem, false)
                    handler.post(this)
                } else {
                    viewPager!!.currentItem = currentItem
                    handler.postDelayed(this, delayTime.toLong())
                }
            }
        }
    }


    constructor(context: Context,attrs : AttributeSet) : this(context,attrs,0)


    constructor(context: Context,attrs : AttributeSet,defStyle : Int) : super(context,attrs,defStyle){
        titles = ArrayList()
        imageUrls = ArrayList()
        imageViews = ArrayList()
        indicatorImages = ArrayList()
        dm = context.resources.displayMetrics
        indicatorSize = dm.widthPixels / 80
        initView(context, attrs)
    }


    private fun initView(context: Context, attrs: AttributeSet?) {
        imageViews.clear()
        handleTypedArray(context, attrs)
        val view = LayoutInflater.from(context).inflate(mLayoutResId, this, true)
        bannerDefaultImage = view.findViewById(R.id.bannerDefaultImage)
        viewPager = view.findViewById(R.id.bannerViewPager)
        titleView = view.findViewById(R.id.titleView)
        indicator = view.findViewById(R.id.circleIndicator)
        indicatorInside = view.findViewById(R.id.indicatorInside)
        bannerTitle = view.findViewById(R.id.bannerTitle)
        numIndicator = view.findViewById(R.id.numIndicator)
        numIndicatorInside = view.findViewById(R.id.numIndicatorInside)
        bannerDefaultImage!!.setImageResource(bannerBackgroundImage)
        initViewPagerScroll()
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner)
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_width, indicatorSize)
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_height, indicatorSize)
        mIndicatorMargin =
                typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_margin, BannerConfig.PADDING_SIZE)
        mIndicatorSelectedResId =
                typedArray.getResourceId(R.styleable.Banner_indicator_drawable_selected, R.drawable.gray_radius)
        mIndicatorUnselectedResId =
                typedArray.getResourceId(R.styleable.Banner_indicator_drawable_unselected, R.drawable.white_radius)
        scaleType = typedArray.getInt(R.styleable.Banner_image_scale_type, scaleType)
        delayTime = typedArray.getInt(R.styleable.Banner_delay_time, BannerConfig.TIME)
        scrollTime = typedArray.getInt(R.styleable.Banner_scroll_time, BannerConfig.DURATION)
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_is_auto_play, BannerConfig.IS_AUTO_PLAY)
        titleBackground = typedArray.getColor(R.styleable.Banner_title_background, BannerConfig.TITLE_BACKGROUND)
        titleHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_title_height, BannerConfig.TITLE_HEIGHT)
        titleTextColor = typedArray.getColor(R.styleable.Banner_title_textcolor, BannerConfig.TITLE_TEXT_COLOR)
        titleTextSize =
                typedArray.getDimensionPixelSize(R.styleable.Banner_title_textsize, BannerConfig.TITLE_TEXT_SIZE)
        mLayoutResId = typedArray.getResourceId(R.styleable.Banner_banner_layout, mLayoutResId)
        bannerBackgroundImage = typedArray.getResourceId(R.styleable.Banner_banner_default_image, R.drawable.no_banner)
        typedArray.recycle()
    }

    private fun initViewPagerScroll() {
        try {
            val mField = ViewPager::class.java.getDeclaredField("mScroller")
            mField.isAccessible = true
            mScroller = BannerScroller(viewPager!!.context)
            mScroller!!.duration = scrollTime
            mField.set(viewPager, mScroller)
        } catch (e: Exception) {
            Log.e(tag, e.message)
        }

    }


    fun isAutoPlay(isAutoPlay: Boolean): Banner {
        this.isAutoPlay = isAutoPlay
        return this
    }

    fun setImageLoader(imageLoader: ImageLoaderInterface<ImageView>) : Banner {
        this.imageLoaderInterFace = imageLoader
        return this
    }

    fun setDelayTime(delayTime: Int): Banner {
        this.delayTime = delayTime
        return this
    }

    fun setIndicatorGravity(type: Int): Banner {
        when (type) {
            BannerConfig.LEFT -> this.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
            BannerConfig.CENTER -> this.gravity = Gravity.CENTER
            BannerConfig.RIGHT -> this.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        }
        return this
    }

    fun setBannerAnimation(transformer: Class<out PageTransformer>): Banner {
        try {
            setPageTransformer(true, transformer.newInstance())
        } catch (e: Exception) {
            Log.e(tag, "Please set the PageTransformer class")
        }

        return this
    }

    /**
     * Set the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     * @return Banner
     */
    fun setOffscreenPageLimit(limit: Int): Banner {
        if (viewPager != null) {
            viewPager!!.offscreenPageLimit = limit
        }
        return this
    }

    /**
     * Set a [PageTransformer] that will be called for each attached page whenever
     * the scroll position is changed. This allows the application to apply custom property
     * transformations to each page, overriding the default sliding look and feel.
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     * to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @return Banner
     */
    fun setPageTransformer(reverseDrawingOrder: Boolean, transformer: PageTransformer): Banner {
        viewPager!!.setPageTransformer(reverseDrawingOrder, transformer)
        return this
    }

    fun setBannerTitles(titles: MutableList<String>): Banner {
        this.titles = titles
        return this
    }

    fun setBannerStyle(bannerStyle: Int): Banner {
        this.bannerStyle = bannerStyle
        return this
    }

    fun setViewPagerIsScroll(isScroll: Boolean): Banner {
        this.isScroll = isScroll
        return this
    }

    fun setImages(imageUrls: MutableList<Any>): Banner {
        this.imageUrls = imageUrls
        this.count = imageUrls.size
        return this
    }

    fun update(imageUrls: List<Any>, titles: List<String>) {
        this.titles!!.clear()
        this.titles!!.addAll(titles)
        update(imageUrls)
    }

    fun update(imageUrls: List<Any>) {
        this.imageUrls!!.clear()
        this.imageViews.clear()
        this.indicatorImages.clear()
        this.imageUrls!!.addAll(imageUrls)
        this.count = this.imageUrls!!.size
        start()
    }

    fun updateBannerStyle(bannerStyle: Int) {
        indicator!!.visibility = View.GONE
        numIndicator!!.visibility = View.GONE
        numIndicatorInside!!.visibility = View.GONE
        indicatorInside!!.visibility = View.GONE
        bannerTitle!!.visibility = View.GONE
        titleView!!.visibility = View.GONE
        this.bannerStyle = bannerStyle
        start()
    }

    fun start(): Banner {
        setBannerStyleUI()
        setImageList(imageUrls)
        setData()
        return this
    }

    private fun setTitleStyleUI() {
        if (titles!!.size != imageUrls!!.size) {
            throw RuntimeException("[Banner] --> The number of titles and images is different")
        }
        if (titleBackground != -1) {
            titleView!!.setBackgroundColor(titleBackground)
        }
        if (titleHeight != -1) {
            titleView!!.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight)
        }
        if (titleTextColor != -1) {
            bannerTitle!!.setTextColor(titleTextColor)
        }
        if (titleTextSize != -1) {
            bannerTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize.toFloat())
        }
        if (titles != null && titles!!.size > 0) {
            bannerTitle!!.text = titles!![0]
            bannerTitle!!.visibility = View.VISIBLE
            titleView!!.visibility = View.VISIBLE
        }
    }

    private fun setBannerStyleUI() {
        val visibility = if (count > 1) View.VISIBLE else View.GONE
        when (bannerStyle) {
            BannerConfig.CIRCLE_INDICATOR -> indicator!!.visibility = visibility
            BannerConfig.NUM_INDICATOR -> numIndicator!!.visibility = visibility
            BannerConfig.NUM_INDICATOR_TITLE -> {
                numIndicatorInside!!.visibility = visibility
                setTitleStyleUI()
            }
            BannerConfig.CIRCLE_INDICATOR_TITLE -> {
                indicator!!.visibility = visibility
                setTitleStyleUI()
            }
            BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE -> {
                indicatorInside!!.visibility = visibility
                setTitleStyleUI()
            }
        }
    }

    private fun initImages() {
        imageViews.clear()
        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
            bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||
            bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE
        ) {
            createIndicator()
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR_TITLE) {
            numIndicatorInside!!.text = "1/$count"
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR) {
            numIndicator!!.text = "1/$count"
        }
    }

    private fun setImageList(imagesUrl: List<Any>?) {
        if (imagesUrl == null || imagesUrl.isEmpty()) {
            bannerDefaultImage!!.visibility = View.VISIBLE
            Log.e(tag, "The image data set is empty.")
            return
        }

        bannerDefaultImage!!.visibility = View.GONE
        initImages()

        for (i in 0..count + 1) {
            var imageView : ImageView? = null

            if (imageLoaderInterFace != null) {
                imageView = imageLoaderInterFace!!.createImageView(context)
            }

            if (imageView == null) {
                imageView = ImageView(context)
            }
            setScaleType(imageView)
            var url: Any? = null
            if (i == 0) {
                url = imagesUrl[count - 1]
            } else if (i == count + 1) {
                url = imagesUrl[0]
            } else {
                url = imagesUrl[i - 1]
            }

            imageViews.add(imageView)
            if (imageLoaderInterFace != null){
                imageLoaderInterFace!!.displayImage(context, url!!, imageView)
            } else{
                Log.e(tag, "Please set images loader.")
            }
        }
    }

    private fun setScaleType(imageView: View) {
        if (imageView is ImageView) {
            val view = imageView
            when (scaleType) {
                0 -> view.scaleType = ScaleType.CENTER
                1 -> view.scaleType = ScaleType.CENTER_CROP
                2 -> view.scaleType = ScaleType.CENTER_INSIDE
                3 -> view.scaleType = ScaleType.FIT_CENTER
                4 -> view.scaleType = ScaleType.FIT_END
                5 -> view.scaleType = ScaleType.FIT_START
                6 -> view.scaleType = ScaleType.FIT_XY
                7 -> view.scaleType = ScaleType.MATRIX
            }

        }
    }

    private fun createIndicator() {
        indicatorImages.clear()
        indicator!!.removeAllViews()
        indicatorInside!!.removeAllViews()
        for (i in 0 until count) {
            val imageView = ImageView(context)
            imageView.scaleType = ScaleType.CENTER_CROP
            val params = LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight)
            params.leftMargin = mIndicatorMargin
            params.rightMargin = mIndicatorMargin
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId)
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId)
            }
            indicatorImages.add(imageView)
            if (bannerStyle == BannerConfig.CIRCLE_INDICATOR || bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE)
                indicator!!.addView(imageView, params)
            else if (bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                indicatorInside!!.addView(imageView, params)
        }
    }


    private fun setData() {
        currentItem = 1
        if (adapter == null) {
            adapter = BannerPagerAdapter()
            viewPager!!.addOnPageChangeListener(this)
        }
        viewPager!!.adapter = adapter
        viewPager!!.isFocusable = true
        viewPager!!.currentItem = 1
        if (gravity != -1)
            indicator!!.gravity = gravity
        if (isScroll && count > 1) {
            viewPager!!.setScrollable(true)
        } else {
            viewPager!!.setScrollable(false)
        }
        if (isAutoPlay)
            startAutoPlay()
    }


    fun startAutoPlay() {
        handler.removeCallbacks(task)
        handler.postDelayed(task, delayTime.toLong())
    }

    fun stopAutoPlay() {
        handler.removeCallbacks(task)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            val action = ev.action
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_OUTSIDE
            ) {
                startAutoPlay()
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    fun toRealPosition(position: Int): Int {
        var realPosition = (position - 1) % count
        if (realPosition < 0)
            realPosition += count
        return realPosition
    }

    internal inner class BannerPagerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return imageViews.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(imageViews[position])
            val view = imageViews[position]
            if (listener != null) {
                view.setOnClickListener { listener!!.onBannerClick(toRealPosition(position)) }
            }
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

    }

    override fun onPageScrollStateChanged(state: Int) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener!!.onPageScrollStateChanged(state)
        }
        //        Log.i(tag,"currentItem: "+currentItem);
        when (state) {
            0//No operation
            -> if (currentItem == 0) {
                viewPager!!.setCurrentItem(count, false)
            } else if (currentItem == count + 1) {
                viewPager!!.setCurrentItem(1, false)
            }
            1//start Sliding
            -> if (currentItem == count + 1) {
                viewPager!!.setCurrentItem(1, false)
            } else if (currentItem == 0) {
                viewPager!!.setCurrentItem(count, false)
            }
            2//end Sliding
            -> {
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener!!.onPageScrolled(toRealPosition(position), positionOffset, positionOffsetPixels)
        }
    }

    override fun onPageSelected(position: Int) {
        var position = position
        currentItem = position
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener!!.onPageSelected(toRealPosition(position))
        }
        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
            bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||
            bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE
        ) {
            indicatorImages[(lastPosition - 1 + count) % count].setImageResource(mIndicatorUnselectedResId)
            indicatorImages[(position - 1 + count) % count].setImageResource(mIndicatorSelectedResId)
            lastPosition = position
        }
        if (position == 0) position = count
        if (position > count) position = 1
        when (bannerStyle) {
            BannerConfig.CIRCLE_INDICATOR -> {
            }
            BannerConfig.NUM_INDICATOR -> numIndicator!!.text = position.toString() + "/" + count
            BannerConfig.NUM_INDICATOR_TITLE -> {
                numIndicatorInside!!.text = position.toString() + "/" + count
                bannerTitle!!.text = titles!![position - 1]
            }
            BannerConfig.CIRCLE_INDICATOR_TITLE -> bannerTitle!!.text = titles!![position - 1]
            BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE -> bannerTitle!!.text = titles!![position - 1]
        }

    }

    /**
     * 新版的接口下标是从1开始，同时解决下标越界问题
     *
     * @param listener
     * @return
     */
    fun setOnBannerListener(listener: OnBannerClickListener): Banner {
        this.listener = listener
        return this
    }

    fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener
    }

    fun releaseBanner() {
        handler.removeCallbacksAndMessages(null)
    }
}
