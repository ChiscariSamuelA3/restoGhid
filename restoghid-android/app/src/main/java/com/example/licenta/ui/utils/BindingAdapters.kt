package com.example.licenta.ui.utils

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import java.net.URL


object BindingAdapters {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(imageView: ImageView, imageUrl: String) {

        val url = URL(imageUrl)
        val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        imageView.setImageBitmap(bitmap)
    }
}