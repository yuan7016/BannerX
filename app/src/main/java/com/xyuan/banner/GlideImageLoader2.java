package com.xyuan.banner;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.xyuan.xybanner.loader.ImageLoader;
import com.xyuan.xybanner.loader.ImageLoaderInterface;
import org.jetbrains.annotations.NotNull;


public class GlideImageLoader2 extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }

//    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}
