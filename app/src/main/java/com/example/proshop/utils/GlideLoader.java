package com.example.proshop.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.proshop.R;
import com.google.protobuf.Any;

public class GlideLoader {

    public void loadUserPicture(String image, ImageView imageView, Context context) {
        Glide
                .with(context)
                .load(Uri.parse(image.toString()))
                .centerCrop()
                .placeholder(R.drawable.user_placeholder)
                .into(imageView);
    }
}
