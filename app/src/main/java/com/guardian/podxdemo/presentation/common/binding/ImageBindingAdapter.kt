package com.guardian.podxdemo.presentation.common.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun bindImageUrlString(imageView: ImageView, imageUrlString: String?) {
    Glide.with(imageView.context).load(imageUrlString).into(imageView)
}