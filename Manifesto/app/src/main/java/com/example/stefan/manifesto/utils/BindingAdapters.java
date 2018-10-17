package com.example.stefan.manifesto.utils;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.stefan.manifesto.ManifestoApplication;
import com.example.stefan.manifesto.R;

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

    @BindingAdapter(value = {"imageUrl_round", "errorImageResId_round"}, requireAll = false)
    public static void loadRoundImage(ImageView imageView, String url, Drawable errorImageResourceId) {
        if (url == null) {
            imageView.setImageDrawable(errorImageResourceId);
        } else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .circleCrop()
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

    @BindingAdapter("isEmergency")
    public static void cardColor(View view, boolean emergency) {
        ((CardView) view).setCardBackgroundColor(emergency ?
                ManifestoApplication.getContext().getResources().getColor(R.color.lightRed)
        :                ManifestoApplication.getContext().getResources().getColor(android.R.color.white));
    }



}
