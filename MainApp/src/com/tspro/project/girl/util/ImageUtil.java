package com.tspro.project.girl.util;

import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.tspro.project.girl.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageUtil {

    public static void displayImage(ImageView view, String path, ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        try {
            loader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            loader.clearMemoryCache();
        }
    }

    public static void displayRoundImage(ImageView view, String path, ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        try {
            loader.displayImage(path, view, ROUND_DISPLAY_IMAGE_OPTIONS, listener);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            loader.clearMemoryCache();
        }
    }

    public static void loadImage(String path, ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        try {
            loader.loadImage(path, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void displayRoundImageProfile(ImageView view, String path, ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        try {
            loader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_PROFILE_OPTIONS_BUIDLER, listener);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            loader.clearMemoryCache();
        }
    }

    //TODO Change default image
    private static final DisplayImageOptions.Builder DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .displayer(new FadeInBitmapDisplayer(300, true, false, false))
            .showImageForEmptyUri(R.drawable.ic_sexygirlicon)
            .showImageOnLoading(R.drawable.ic_sexygirlicon)
            .showImageOnFail(R.drawable.ic_sexygirlicon).cacheOnDisk(true)
            .cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

    private static final DisplayImageOptions DEFAULT_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
            .build();
    private static final DisplayImageOptions ROUND_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
            .displayer(new RoundedBitmapDisplayer(500)).build();

    //TODO Change default image
    private static final DisplayImageOptions DEFAULT_DISPLAY_IMAGE_PROFILE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .showImageForEmptyUri(R.drawable.default_imagess)
            .showImageOnLoading(R.drawable.default_imagess)
            .showImageOnFail(R.drawable.default_imagess).cacheOnDisk(true)
            .cacheInMemory(true).bitmapConfig(Config.ARGB_8888)
            .displayer(new FadeInBitmapDisplayer(300, true, false, false))
            .build();


}
