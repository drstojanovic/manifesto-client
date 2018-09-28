package com.example.stefan.manifesto.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class BindingAdapters {

    @BindingAdapter(value = {"imageUrl", "errorImageResId"}, requireAll = false)
    public static void loadImage(ImageView imageView, String url, Drawable errorImageResourceId) {
        if (url == null) {
            imageView.setImageDrawable(errorImageResourceId);
        } else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(errorImageResourceId)
                    .error(errorImageResourceId);

            Glide.with(imageView.getContext())
                    .applyDefaultRequestOptions(options)
                    .load(url)
                    .into(imageView);
        }
    }

    @BindingAdapter("visible")
    public static void viewVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}
