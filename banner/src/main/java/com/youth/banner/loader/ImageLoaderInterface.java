package com.youth.banner.loader;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

public interface ImageLoaderInterface extends Serializable {

    void displayImage(Context context, Object path, View imageView);

    View createImageView(Context context, Object path);

}
