package com.yh.sdk.imageLoader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.RawRes
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File

/**
 * @description  图片加载库的配置，封装原始加载配置属性，进行转换
 * @time 2021/4/8 3:59 PM
 * @author zengbobo
 */
class YHImageRequestBuilder<ResourceType> constructor(private val requestBuilder: RequestBuilder<ResourceType>) {
    private val UNSET = -1
    private var isCacheable = true
    private var overrideHeight = UNSET
    private var overrideWidth = UNSET
    private var scaleType: YHScaleType = YHScaleType.CENTER_CROP
    private var bitmap: Bitmap? = null
    private var drawable: Drawable? = null
    private var string: String? = null
    private var uri: Uri? = null
    private var file: File? = null
    private var resourceId: Int? = null

    /**
     * 加载占位图资源ID，如果placeholder是0表示没有占位图
     */
    private var placeholderId = 0

    /**
     * 加载占位图资源Drawable对象
     */
    private var placeholderDrawable: Drawable? = null

    /**
     * 错误占位图的资源ID
     */
    private var errorId = 0

    /**
     * 加载失败占位图资源Drawable对象
     */
    private var errorPlaceholder: Drawable? = null


    /**
     * 模糊特效
     * Glide要将isCrossFade设置为false，不然会影响展示效果
     */
    private var isBlur: Boolean = false

    /**
     * 高斯模糊半經
     */
    private var blurRadius: Int = 25

    /**
     * 设置高斯模糊度数，如果外面设置为0则使用默认值
     */
    private var blurSampling: Int = 3


    /**
     * 是否圆角
     * Glide要将isCrossFade设置为false，不然会影响展示效果
     */
    var isRoundedCorners: Boolean = false
    private var isEngineRoundedCorners: Boolean = false//是否使用Glide的RoundedCorners实现圆角，不是自定义圆角转换器

    /**
     * 圆角的弧度
     */
    var roundRadius: Int = 25

    /**
     * 圆角边框大小
     */
    private var roundBorderWidth: Int = 0

    /**
     * 圆角边框颜色
     */
    private var roundBorderColor: Int = 0

    /**
     * 是否是黑白图像
     */
    private var isBlackWhite = false

    /**
     * 圆角的边向
     */
    var roundType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL

    private var borderOutSize = 0

    private @ColorInt
    var borderOutColor = Color.WHITE
    private var borderInSize = 0

    private @ColorInt
    var borderInColor = Color.WHITE
    var isCircle: Boolean = false

    fun isCircle(isCircle: Boolean): YHImageRequestBuilder<ResourceType> {
        this.isCircle = isCircle
        return this
    }


    fun circleBorderOut(borderOutSize: Int, @ColorInt borderOutColor: Int): YHImageRequestBuilder<ResourceType> {
        this.isCircle = true
        this.borderOutSize = borderOutSize
        this.borderOutColor = borderOutColor
        return this
    }

    fun circleBorderIn(borderInSize: Int, @ColorInt borderInColor: Int): YHImageRequestBuilder<ResourceType> {
        this.borderInSize = borderInSize
        this.borderInColor = borderInColor
        return this
    }


    private val listeners: ArrayList<YHImageLoaderListener<ResourceType>> = arrayListOf()

    fun addListeners(l: YHImageLoaderListener<ResourceType>): YHImageRequestBuilder<ResourceType> {
        this.listeners.add(l)
        return this
    }

    fun isBlur(blur: Boolean): YHImageRequestBuilder<ResourceType> {
        this.isBlur = blur
        return this
    }

    fun blurRadius(blurRadius: Int): YHImageRequestBuilder<ResourceType> {
        this.blurRadius = blurRadius
        return this
    }

    fun blurSampling(blurSampling: Int): YHImageRequestBuilder<ResourceType> {
        this.blurSampling = blurSampling
        return this
    }

    fun isRoundedCorners(isRoundedCorners: Boolean): YHImageRequestBuilder<ResourceType> {
        this.isRoundedCorners = isRoundedCorners
        return this
    }

    fun roundRadius(roundRadius: Int): YHImageRequestBuilder<ResourceType> {
        this.isRoundedCorners = true
        this.roundRadius = roundRadius
        return this
    }

    fun roundBorderWidth(roundBorderWidth: Int): YHImageRequestBuilder<ResourceType> {
        this.roundBorderWidth = roundBorderWidth
        return this
    }

    fun roundType(roundType: RoundedCornersTransformation.CornerType): YHImageRequestBuilder<ResourceType> {
        this.isRoundedCorners = true
        this.roundType = roundType
        return this
    }

    fun error(drawable: Drawable?): YHImageRequestBuilder<ResourceType> {
        errorPlaceholder = drawable
        errorId = 0
        return this
    }

    fun error(@DrawableRes resourceId: Int): YHImageRequestBuilder<ResourceType> {
        errorPlaceholder = null
        errorId = resourceId
        return this
    }

    fun placeholder(drawable: Drawable?): YHImageRequestBuilder<ResourceType> {
        placeholderDrawable = drawable
        placeholderId = 0
        return this
    }

    fun placeholder(@DrawableRes resourceId: Int): YHImageRequestBuilder<ResourceType> {
        placeholderDrawable = null
        placeholderId = resourceId
        return this
    }

    fun skipMemoryCache(skip: Boolean): YHImageRequestBuilder<ResourceType> {
        isCacheable = !skip
        return this
    }

    fun override(width: Int, height: Int): YHImageRequestBuilder<ResourceType> {
        overrideWidth = width
        overrideHeight = height
        return this
    }

    fun scaleType(type: YHScaleType): YHImageRequestBuilder<ResourceType> {
        scaleType = type
        return this
    }

    fun centerCrop(): YHImageRequestBuilder<ResourceType> {
        scaleType = YHScaleType.CENTER_CROP
        return this
    }

    fun centerInside(): YHImageRequestBuilder<ResourceType> {
        scaleType = YHScaleType.CENTER_INSIDE
        return this
    }

    fun fitCenter(): YHImageRequestBuilder<ResourceType> {
        scaleType = YHScaleType.FIT_CENTER
        return this
    }

    private fun clearLoad() {
        this.bitmap = null
        this.drawable = null
        this.file = null
        this.uri = null
        this.string = null
        this.resourceId = null
    }

    fun load(@RawRes @DrawableRes @Nullable resourceId: Int?): YHImageRequestBuilder<ResourceType> {
        clearLoad()
        this.resourceId = resourceId
        return this
    }

    fun load(bitmap: Bitmap?): YHImageRequestBuilder<ResourceType> {
        clearLoad()
        this.bitmap = bitmap
        return this
    }

    fun load(drawable: Drawable?): YHImageRequestBuilder<ResourceType> {
        clearLoad()
        this.drawable = drawable
        return this
    }

    fun load(string: String?): YHImageRequestBuilder<ResourceType> {
        clearLoad()
        this.string = string
        return this
    }


    fun load(uri: Uri?): YHImageRequestBuilder<ResourceType> {
        clearLoad()
        this.uri = uri
        return this
    }

    fun load(file: File?): YHImageRequestBuilder<ResourceType> {
        clearLoad()
        this.file = file
        return this
    }

    private fun requestBuilder(context: Context? = null): RequestBuilder<ResourceType> {
        var builder: RequestBuilder<ResourceType> = requestBuilder
        if (bitmap != null) builder = builder.load(bitmap)
        if (string != null) builder = builder.load(string)
        if (uri != null) builder = builder.load(uri)
        if (drawable != null) builder = builder.load(drawable)
        if (file != null) builder = builder.load(file)
        if (resourceId != null) builder = builder.load(resourceId)



        if (scaleType == YHScaleType.FIT_CENTER) builder = builder.fitCenter()
        if (scaleType == YHScaleType.CENTER_CROP) builder = builder.centerCrop()
        if (scaleType == YHScaleType.CENTER_INSIDE) builder = builder.centerInside()

        builder = builder.skipMemoryCache(isCacheable)
        builder = builder.override(overrideWidth, overrideHeight)

        if (isBlur && isRoundedCorners) {
            val multi = MultiTransformation<Bitmap>(
                    BlurTransformation(blurRadius, blurSampling), RoundedCornersTransformation(roundRadius, roundBorderWidth, roundType))
            builder = builder.apply(RequestOptions.bitmapTransform(multi))
            transformationHolder(context, multi, "RoundedCorners_" + roundRadius + "_" + roundBorderWidth + "_" + roundType.name+"_"+targetW)
        } else if (isBlur) {
            val transformation = BlurTransformation(blurRadius, blurSampling)
            builder = builder.apply(RequestOptions.bitmapTransform(transformation))
        } else if (isRoundedCorners) {
            val transformation = RoundedCornersTransformation(roundRadius, roundBorderWidth, roundType)
            builder = builder.apply(RequestOptions.bitmapTransform(transformation))
            transformationHolder(context, transformation, "RoundedCorners_" + roundRadius + "_" + roundBorderWidth + "_" + roundType.name+"_"+targetW)
        } else if (isCircle) {
            val transformation = CropCircleWithDoubleBorderTransformation(borderOutSize, borderOutColor, borderInSize, borderInColor)
            builder = builder.apply(RequestOptions.bitmapTransform(transformation))
            transformationHolder(context, transformation, "Circle_" + borderOutSize + "_" + borderOutColor
                    + "_" + borderInSize + "_" + borderInColor)
        }

        if (placeholderDrawable != null) builder = builder.placeholder(placeholderDrawable)
        if (placeholderId != 0) builder = builder.placeholder(placeholderId)

        if (errorPlaceholder != null) builder = builder.error(errorPlaceholder)
        if (errorId != 0) builder = builder.error(errorId)

        if (listeners.size > 0) {
            listeners.forEach {
                builder = builder.addListener(object : RequestListener<ResourceType> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<ResourceType>?, isFirstResource: Boolean): Boolean {
                        return it.onLoadFailed(e, isFirstResource)
                    }

                    override fun onResourceReady(resource: ResourceType?, model: Any?, target: Target<ResourceType>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return it.onResourceReady(resource, isFirstResource)
                    }
                })
            }

        }
        return builder
    }

    private fun transformationHolder(context: Context?, transformation: Transformation<Bitmap>, resIdKey: String) {
        YHImageRequestManager.println("YHImageRequestBuilder transformationHolder placeholderId=$placeholderId  errorId=$errorId  resIdKey=$resIdKey")
        if (placeholderId != 0) {
            val drawable = YHImageRequestManager.getDrawable(context, this@YHImageRequestBuilder, placeholderId, resIdKey)
            if (drawable != null) {
                placeholderDrawable = drawable
                placeholderId = 0
            }
        }

        if (errorId != 0) {
            val drawable = YHImageRequestManager.getDrawable(context, this@YHImageRequestBuilder, placeholderId, resIdKey)
            if (drawable != null) {
                errorPlaceholder = drawable
                errorId = 0
            }
        }
    }

    var targetW = 0
    var targetH = 0
    fun into(view: ImageView) {
        if (listeners.size > 0) {
            listeners.forEach {
                it.onLoadStarted()
            }
        }
        targetW = if (view.measuredWidth > 0) {
            view.measuredWidth
        } else if (view.width > 0) {
            view.width
        } else if (view.layoutParams?.width ?: 0 > 0) {
            view.layoutParams?.width ?: 0
        } else {
            0
        }
        targetH = if (view.measuredHeight > 0) {
            view.measuredHeight
        } else if (view.height > 0) {
            view.height
        } else if (view.layoutParams?.height ?: 0 > 0) {
            view.layoutParams?.height ?: 0
        } else {
            0
        }
        YHImageRequestManager.println("into targetW=$targetW targetH=$targetH")
        requestBuilder(view.context).into(view)

    }

    fun preload() {
        if (listeners.size > 0) {
            listeners.forEach {
                it.onLoadStarted()
            }
        }
        requestBuilder().preload()
    }
}