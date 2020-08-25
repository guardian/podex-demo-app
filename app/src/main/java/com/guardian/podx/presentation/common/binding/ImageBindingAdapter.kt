package com.guardian.podx.presentation.common.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.guardian.podx.R

@BindingAdapter(value = ["imageUrl", "placeHolder"], requireAll = false)
fun bindImageUrlString(imageView: ImageView, imageUrlString: String?, placeHolder: Drawable?) {
    if (imageUrlString != null) {
        Glide.with(imageView.context)
            .load(imageUrlString)
            .placeholder(placeHolder ?: imageView.drawable)
            .error(placeHolder ?: imageView.context.getDrawable(R.drawable.image_placeholder))
            .transition(withCrossFade())
            .into(imageView)
    }
}
