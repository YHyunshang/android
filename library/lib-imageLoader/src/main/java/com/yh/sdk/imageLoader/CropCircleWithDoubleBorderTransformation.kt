package com.yh.sdk.imageLoader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import jp.wasabeef.glide.transformations.BitmapTransformation
import java.security.MessageDigest

/**
 * @author zengbobo
 * @description 圆图带边环，分内环和外环
 * @time 2021/4/14 10:40 AM
 */
class CropCircleWithDoubleBorderTransformation(private val borderOutSize: Int, @param:ColorInt private val borderOutColor: Int, private val borderInSize: Int, @param:ColorInt private val borderInColor: Int) : BitmapTransformation() {
    override fun transform(context: Context, pool: BitmapPool,
                           toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight)

//        setCanvasBitmapDensity(toTransform, bitmap);
        bitmap.density = toTransform.density
        val paint = Paint()
        paint.color = borderOutColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderOutSize.toFloat()
        paint.isAntiAlias = true
        val canvas = Canvas(bitmap)
        if (borderOutSize > 0) {
            canvas.drawCircle(
                    outWidth / 2f,
                    outHeight / 2f,
                    Math.max(outWidth, outHeight) / 2f - borderOutSize / 2f,
                    paint
            )
            if (borderInSize > 0) {
                paint.color = borderInColor
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = borderInSize.toFloat()
                paint.isAntiAlias = true
                canvas.drawCircle(
                        outWidth / 2f,
                        outHeight / 2f,
                        Math.max(outWidth, outHeight) / 2f - borderOutSize - borderInSize / 2f,
                        paint
                )
            }
        } else {
            canvas.drawCircle(
                    outWidth / 2f,
                    outHeight / 2f,
                    Math.max(outWidth, outHeight) / 2f,
                    paint
            )
        }
        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(key().toByteArray(Key.CHARSET))
    }

    private fun key(): String {
        return (ID + "_" + borderOutSize + "_" + borderOutColor
                + "_" + borderInSize + "_" + borderInColor)
    }

    override fun equals(o: Any?): Boolean {
        return o is CropCircleWithDoubleBorderTransformation && o.borderOutSize == borderOutSize && o.borderOutColor == borderOutColor && o.borderInSize == borderInSize && o.borderInColor == borderInColor
    }

    override fun hashCode(): Int {
        return key().hashCode()
    }

    companion object {
        private const val VERSION = 1
        private const val ID = "com.yh.sdk.imageLoader.CropCircleWithDoubleBorderTransformation.$VERSION"
    }

}