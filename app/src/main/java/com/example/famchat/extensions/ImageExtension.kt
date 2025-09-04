package com.example.famchat.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.loadImage(
    url: String? = null,
    uri: Uri? = null,
    resource: Int? = null,
    bitmap: Bitmap? = null,
    byteArray: ByteArray? = null,
    base64: String? = null,
    errorHolder: Int = 0,
    placeholder: Int = 0,
    hasAnimation: Boolean = true,
    loadResult: ((Boolean) -> Unit)? = null
) {
    Glide.with(this)
        .load(url ?: uri ?: resource ?: bitmap ?: byteArray ?: base64)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                loadResult?.invoke(false)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                loadResult?.invoke(true)
                return false
            }
        })
        .transition(
            DrawableTransitionOptions.withCrossFade(
                if (hasAnimation) {
                    300
                } else {
                    0
                }
            )
        )
        .error(errorHolder)
        .placeholder(placeholder)
        .into(this)
}