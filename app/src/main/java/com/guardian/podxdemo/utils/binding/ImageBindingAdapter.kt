package com.guardian.podxdemo.utils.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bind:imageUrl")
fun bindImageUrlString(imageView: ImageView, imageUrlString: String) {
    Glide.with(imageView.context).load(imageUrlString).into(imageView)
}