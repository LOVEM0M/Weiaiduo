package com.miyin.zhenbaoqi.utils;

import android.content.Context;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImageCacheUtils {

    public static File getCacheFileTo4x(Context context, String url) {
        try {
            return Glide.with(context).downloadOnly().load(url).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
