# BannerX:Android 图片轮播控件 专门支持androidx
从项目[banner](https://github.com/youth5201314/banner) 移植过来的，保持基本API不变.

# demo图
![](https://github.com/yuan7016/BannerX/blob/master/screen/screen_1.jpg)
## 常量
|常量名称|描述|所属方法
|---|---|---|
|BannerConfig.NOT_INDICATOR| 不显示指示器和标题|setBannerStyle
|BannerConfig.CIRCLE_INDICATOR| 显示圆形指示器|setBannerStyle
|BannerConfig.NUM_INDICATOR| 显示数字指示器|setBannerStyle
|BannerConfig.NUM_INDICATOR_TITLE| 显示数字指示器和标题|setBannerStyle
|BannerConfig.CIRCLE_INDICATOR_TITLE| 显示圆形指示器和标题（垂直显示）|setBannerStyle
|BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE| 显示圆形指示器和标题（水平显示）|setBannerStyle
|BannerConfig.LEFT| 指示器居左|setIndicatorGravity
|BannerConfig.CENTER| 指示器居中|setIndicatorGravity
|BannerConfig.RIGHT| 指示器居右|setIndicatorGravity

## 动画常量类（setBannerAnimation方法调用）
[ViewPagerTransforms](https://github.com/ToxicBakery/ViewPagerTransforms) `动画时集成的第三方库，可能有兼容问题导致position位置不准确，你可以选择参考动画然后自定义动画`

|常量类名|
|---|
|Transformer.Default| 
|Transformer.Accordion| 
|Transformer.BackgroundToForeground| 
|Transformer.ForegroundToBackground| 
|Transformer.CubeIn| 
|Transformer.CubeOut| 
|Transformer.DepthPage| 
|Transformer.FlipHorizontal| 
|Transformer.FlipVertical| 
|Transformer.RotateDown| 
|Transformer.RotateUp| 
|Transformer.ScaleInOut| 
|Transformer.Stack| 
|Transformer.Tablet| 
|Transformer.ZoomIn| 
|Transformer.ZoomOut| 
|Transformer.ZoomOutSlide| 


## 方法
|方法名|描述|版本限制
|---|---|---|
|setBannerStyle(int bannerStyle)| 设置轮播样式（默认为CIRCLE_INDICATOR）|无
|setIndicatorGravity(int type)| 设置指示器位置（没有标题默认为右边,有标题时默认左边）|无
|isAutoPlay(boolean isAutoPlay)| 设置是否自动轮播（默认自动）|无
|setViewPagerIsScroll(boolean isScroll)| 设置是否允许手动滑动轮播图（默认true）|1.4.5开始
|update(List<?> imageUrls,List<String> titles)| 更新图片和标题 |1.4.5开始
|update(List<?> imageUrls)| 更新图片 |1.4.5开始
|startAutoPlay()|开始轮播|1.4开始，此方法只作用于banner加载完毕-->需要在start()后执行
|stopAutoPlay()|结束轮播|1.4开始，此方法只作用于banner加载完毕-->需要在start()后执行
|start()|开始进行banner渲染（必须放到最后执行）|1.4开始
|setOffscreenPageLimit(int limit)|同viewpager的方法作用一样|1.4.2开始
|setBannerTitle(String[] titles)| 设置轮播要显示的标题和图片对应（如果不传默认不显示标题）|1.3.3结束
|setBannerTitleList(List<String> titles)| 设置轮播要显示的标题和图片对应（如果不传默认不显示标题）|1.3.3结束
|setBannerTitles(List<String> titles)| 设置轮播要显示的标题和图片对应（如果不传默认不显示标题）|1.4开始
|setDelayTime(int time)| 设置轮播图片间隔时间（单位毫秒，默认为2000）|无
|setImages(Object[]/List<?> imagesUrl)| 设置轮播图片(所有设置参数方法都放在此方法之前执行)|1.4后去掉数组传参
|setImages(Object[]/List<?> imagesUrl,OnLoadImageListener listener)| 设置轮播图片，并且自定义图片加载方式|1.3.3结束
|setOnBannerClickListener(this)|设置点击事件，下标是从1开始|无（1.4.9以后废弃了）
|setOnBannerListener(this)|设置点击事件，下标是从0开始|1.4.9以后
|setOnLoadImageListener(this)|设置图片加载事件，可以自定义图片加载方式|1.3.3结束
|setImageLoader(Object implements ImageLoader)|设置图片加载器|1.4开始
|setOnPageChangeListener(this)|设置viewpager的滑动监听|无
|setBannerAnimation(Class<? extends PageTransformer> transformer)|设置viewpager的默认动画,传值见动画表|无
|setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer)|设置viewpager的自定义动画|无

## Attributes属性（banner布局文件中调用）
|Attributes|forma|describe
|---|---|---|
|delay_time| integer|轮播间隔时间，默认2000
|scroll_time| integer|轮播滑动执行时间，默认800
|is_auto_play| boolean|是否自动轮播，默认true
|title_background| color|reference|标题栏的背景色
|title_textcolor| color|标题字体颜色
|title_textsize| dimension|标题字体大小
|title_height| dimension|标题栏高度
|indicator_width| dimension|指示器圆形按钮的宽度
|indicator_height| dimension|指示器圆形按钮的高度
|indicator_margin| dimension|指示器之间的间距
|indicator_drawable_selected| reference|指示器选中效果
|indicator_drawable_unselected| reference|指示器未选中效果
|image_scale_type| enum |和imageview的ScaleType作用一样
|banner_default_image| reference | 当banner数据为空是显示的默认图片
|banner_layout| reference |自定义banner布局文件，但是必须保证id的名称一样（你可以将banner的布局文件复制出来进行修改）

## ====使用步骤=====

#### Step 1.依赖banner
Gradle
引用本地lib
```groovy
implementation project(':xybanner')
```


#### Step 2.添加权限到你的 AndroidManifest.xml
```xml
<!-- if you want to load images from the internet -->
<uses-permission android:name="android.permission.INTERNET" /> 

<!-- if you want to load images from a file OR from the internet -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

#### Step 3.在布局文件中添加Banner，可以设置自定义属性
！！！此步骤可以省略，直接在Activity或者Fragment中new Banner();
```xml
<com.xyuan.xybanner.Banner
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/banner"
    android:layout_width="match_parent"
    android:layout_height="高度自己设置" />
```

#### Step 4.重写图片加载器
```java
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
          注意：
          1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
          2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
          传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
          切记不要胡乱强转！
         */
        eg：
        
        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);

        //Picasso 加载图片简单用法
        Picasso.with(context).load(path).into(imageView);
        
        //用fresco加载图片简单用法，记得要写下面的createImageView方法
        Uri uri = Uri.parse((String) path);
        imageView.setImageURI(uri);
    }
    
    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public ImageView createImageView(Context context) {
        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
        SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
        return simpleDraweeView;
    }
}
```

#### Step 5.在Activity或者Fragment中配置Banner 

- 注意！start()方法必须放到最后执行，点击事件请放到start()前，每次都提交问题问为什么点击没有反应？需要轮播一圈才能点击？点击第一个怎么返回1？麻烦仔细阅读文档。

```java
--------------------------简单使用-------------------------------
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Banner banner = (Banner) findViewById(R.id.banner);
    //设置图片加载器
    banner.setImageLoader(new GlideImageLoader());
    //设置图片集合
    banner.setImages(images);
    //banner设置方法全部调用完毕时最后调用
    banner.start();
}
--------------------------详细使用-------------------------------
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Banner banner = (Banner) findViewById(R.id.banner);
    //设置banner样式
    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
    //设置图片加载器
    banner.setImageLoader(new GlideImageLoader());
    //设置图片集合
    banner.setImages(images);
    //设置banner动画效果
    banner.setBannerAnimation(Transformer.DepthPage);
    //设置标题集合（当banner样式有显示title时）
    banner.setBannerTitles(titles);
    //设置自动轮播，默认为true
    banner.isAutoPlay(true);
    //设置轮播时间
    banner.setDelayTime(1500);
    //设置指示器位置（当banner模式中有指示器时）
    banner.setIndicatorGravity(BannerConfig.CENTER);
    //banner设置方法全部调用完毕时最后调用
    banner.start();
}
-----------------当然如果你想偷下懒也可以这么用--------------------
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Banner banner = (Banner) findViewById(R.id.banner);
    banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
}
```

#### Step 6.（可选）增加体验
```java
//如果你需要考虑更好的体验，可以这么操作
@Override
protected void onStart() {
    super.onStart();
    //开始轮播
    banner.startAutoPlay();
}

@Override
protected void onStop() {
    super.onStop();
    //结束轮播
    banner.stopAutoPlay();
}
```
