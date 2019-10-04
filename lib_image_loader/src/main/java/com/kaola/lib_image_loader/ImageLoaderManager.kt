package com.kaola.lib_image_loader

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kaola.lib_image_loader.util.Utils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Imageloader 图片加载
 */
class ImageLoaderManager private constructor() {
    companion object {
        val instance: ImageLoaderManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageLoaderManager()
        }
    }

    /**
     * 显示imageView
     */
    fun displayImageView(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .asBitmap()
            .load(url)
            .apply(initCommonRequestOption())
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(imageView)
    }

    /**
     * 显示圆形
     */
    fun displayImageViewCircle(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .asBitmap()
            .load(url)
            .apply(initCommonRequestOption())
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    var drawable =
                        RoundedBitmapDrawableFactory.create(imageView.resources, resource)
                    drawable.isCircular = true
                    imageView.setImageDrawable(drawable)
                }
            })
    }

    /**
     * viewgroup显示图片
     */
    fun displayImageViewGroup(group: ViewGroup, url: String) {
        Glide.with(group.context)
            .asBitmap()
            .load(url)
            .apply(initCommonRequestOption())
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Single.just(resource)
                        .map { t ->
                            val drawable = BitmapDrawable(Utils.doBlur(t, 100, true))
                            drawable
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer<Drawable> {
                            group.setBackgroundDrawable(it)
                        })
                }
            })
    }


    fun initCommonRequestOption(): RequestOptions {
        var requestOptions = RequestOptions()
        requestOptions.placeholder(R.mipmap.ic_error_round)
            .error(R.mipmap.ic_error_round)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(false)
            .priority(Priority.NORMAL)

        return requestOptions
    }


}