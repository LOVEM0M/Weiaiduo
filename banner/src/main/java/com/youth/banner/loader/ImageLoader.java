package com.youth.banner.loader;

import android.content.Context;
import android.widget.ImageView;

public abstract class ImageLoader implements ImageLoaderInterface {

    @Override
    public ImageView createImageView(Context context, Object path) {
        return new ImageView(context);
    }

}