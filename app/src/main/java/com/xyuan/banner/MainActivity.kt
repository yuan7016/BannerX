package com.xyuan.banner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xyuan.xybanner.BannerConfig
import com.xyuan.xybanner.Transformer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val imageUrl = resources.getStringArray(R.array.image_url)
        val title = resources.getStringArray(R.array.title)


        banner1.setImages(imageUrl.toMutableList())
                .setImageLoader(GlideImageLoader2())
                .start()

        banner2.setImages(imageUrl.toMutableList())
                .setImageLoader(GlideImageLoader2())
                .setBannerStyle(BannerConfig.NUM_INDICATOR)
                .start()

        banner3.setImages(imageUrl.toMutableList())
                .setImageLoader(GlideImageLoader2())
                .setBannerAnimation(Transformer.CubeOut)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .setBannerTitles(title.toMutableList())
                .start()
    }
}
