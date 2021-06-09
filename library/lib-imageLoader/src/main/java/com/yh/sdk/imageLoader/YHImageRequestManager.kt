package com.yh.sdk.imageLoader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.load.resource.gif.GifDrawable
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File
import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import kotlin.math.ceil

/**
 * @description 图片加载发起类
 * @date: 2021/4/9 9:45 AM
 * @author: zengbobo
 */
class YHImageRequestManager(private val requestManager: RequestManager) {

    fun asGif(): YHImageRequestBuilder<GifDrawable> {
        return YHImageRequestBuilder<GifDrawable>(requestManager.asGif())
    }

    fun asDrawable(): YHImageRequestBuilder<Drawable> {
        return YHImageRequestBuilder<Drawable>(requestManager.asDrawable())
    }

    fun asBitmap(): YHImageRequestBuilder<Bitmap> {
        return YHImageRequestBuilder<Bitmap>(requestManager.asBitmap())
    }

    fun asFile(): YHImageRequestBuilder<File> {
        return YHImageRequestBuilder<File>(requestManager.asFile())
    }

    fun load(bitmap: Bitmap): YHImageRequestBuilder<Drawable> {
        return YHImageRequestBuilder<Drawable>(requestManager.load(bitmap))
    }

    fun load(drawable: Drawable): YHImageRequestBuilder<Drawable> {
        return YHImageRequestBuilder<Drawable>(requestManager.load(drawable))
    }

    fun load(string: String): YHImageRequestBuilder<Drawable> {
        return YHImageRequestBuilder<Drawable>(requestManager.load(string))
    }

    fun load(uri: Uri): YHImageRequestBuilder<Drawable> {
        return YHImageRequestBuilder<Drawable>(requestManager.load(uri))
    }

    fun load(file: File): YHImageRequestBuilder<Drawable> {
        return YHImageRequestBuilder<Drawable>(requestManager.load(file))
    }

    fun load(@RawRes @DrawableRes @Nullable resourceId: Int): YHImageRequestBuilder<Drawable> {
        return YHImageRequestBuilder<Drawable>(requestManager.load(resourceId))
    }


    companion object {
        val mReferenceQueue = ReferenceQueue<Drawable>()
        var defaultHoldMap = HashMap<String, SoftReference<Drawable>>()

        fun getDrawable(context: Context?, request: YHImageRequestBuilder<*>, resId: Int, resIdKey: String): Drawable? {
            if (resId == 0 && context == null) {
                return null
            }
            var drawble = defaultHoldMap[resIdKey]?.get()
            if (drawble == null) {
                println("getDrawable ------null")
                val decodeResource = decodeResource(context, request, resId)
                println("getDrawable ----- allocationByteCount=${decodeResource.allocationByteCount.toFloat() / (1024 * 1024)}")
                if (request.isCircle) {
                    val bitmap = TransformationUtils.circleCrop(Glide.get(context!!).bitmapPool, decodeResource, decodeResource.width, decodeResource.height)
                    drawble = BitmapDrawable(context.resources, bitmap)
                    defaultHoldMap[resIdKey] = SoftReference(drawble as BitmapDrawable, mReferenceQueue)
                } else if (request.isRoundedCorners) {
                    val scale :Float = if (request.targetW > 0 && decodeResource.width > 0) decodeResource.width.toFloat() / request.targetW else 1f
                    println("getDrawable ------scale=$scale request.targetW =${request.targetW} decodeResource.width=${decodeResource.width}   ${request.roundRadius}  ${scale}")
                    if (request.roundType == RoundedCornersTransformation.CornerType.ALL) {
                        val bitmap = TransformationUtils.roundedCorners(Glide.get(context!!).bitmapPool, decodeResource, ceil(request.roundRadius.toDouble() * scale).toInt())
                        drawble = BitmapDrawable(context.resources, bitmap)
                        defaultHoldMap[resIdKey] = SoftReference(drawble as BitmapDrawable, mReferenceQueue)
                    } else {
                        val topLeft = if (request.roundType == RoundedCornersTransformation.CornerType.LEFT
                                || request.roundType == RoundedCornersTransformation.CornerType.TOP
                                || request.roundType == RoundedCornersTransformation.CornerType.TOP_LEFT) request.roundRadius else 0

                        val bottomLeft = if (request.roundType == RoundedCornersTransformation.CornerType.LEFT
                                || request.roundType == RoundedCornersTransformation.CornerType.BOTTOM
                                || request.roundType == RoundedCornersTransformation.CornerType.BOTTOM_LEFT) request.roundRadius else 0

                        val topRight = if (request.roundType == RoundedCornersTransformation.CornerType.RIGHT
                                || request.roundType == RoundedCornersTransformation.CornerType.TOP
                                || request.roundType == RoundedCornersTransformation.CornerType.TOP_RIGHT) request.roundRadius else 0

                        val bottomRight = if (request.roundType == RoundedCornersTransformation.CornerType.RIGHT
                                || request.roundType == RoundedCornersTransformation.CornerType.BOTTOM
                                || request.roundType == RoundedCornersTransformation.CornerType.BOTTOM_RIGHT) request.roundRadius else 0

                        val bitmap = TransformationUtils.roundedCorners(Glide.get(context!!).bitmapPool, decodeResource,
                                topLeft.toFloat() * scale,
                                topRight.toFloat() * scale,
                                bottomRight.toFloat() * scale,
                                bottomLeft.toFloat() * scale)

                        drawble = BitmapDrawable(context!!.resources, bitmap)
                        defaultHoldMap[resIdKey] = SoftReference(drawble as BitmapDrawable, mReferenceQueue)
                    }
                }
            }
            println("getDrawable defaultHoldMap.size=${defaultHoldMap.size}")
            if (defaultHoldMap.size > 5) {
                var value = this.mReferenceQueue.poll()
                println("getDrawable 1 value=${value}")
                while (value != null) {
                    println("getDrawable 2 value=${value}")
                    val iterator = defaultHoldMap.keys.iterator()
                    while (iterator.hasNext()) {
                        val key = iterator.next()
                        if (defaultHoldMap[key] == value) {
                            defaultHoldMap.remove(key)
                            break
                        }
                    }
                    value = this.mReferenceQueue.poll()
                }
            }
            println("getDrawable ------end")
            return drawble
        }

        fun decodeResource(context: Context?, request: YHImageRequestBuilder<*>, resId: Int): Bitmap {
            if (request.targetW <= 0 || request.targetH <= 0) {
                return BitmapFactory.decodeResource(context!!.resources, resId)
            }

            var options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(context!!.resources, resId, options)
            options.inSampleSize = calculateInSampleSize(options, request.targetW, request.targetH)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeResource(context!!.resources, resId, options)
        }


        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight;
            val width = options.outWidth;
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize;
        }

        fun println(str: String) {
//            kotlin.io.println("YHImageRequestManager $str")
        }
    }
}