package com.guardian.podxdemo.presentation.common.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.guardian.podxdemo.R

@BindingAdapter("imageUrl")
fun bindImageUrlString(imageView: ImageView, imageUrlString: String?) {
    if (imageUrlString != null) {
        Glide.with(imageView.context)
            .load(imageUrlString)
            .placeholder(imageView.drawable)
            .error(R.drawable.image_placeholder)
            .transition(withCrossFade())
            .into(imageView)
    }
}