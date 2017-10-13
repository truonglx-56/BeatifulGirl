package com.tspro.android.beatifulgirl;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tspro.android.beatifulgirl.model.Entry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by truonglx on 02/10/2017.
 */

public class MainApplication extends Application {
    public static final String JSON_ARRAY_QUEUE_LIST = "json_queue_array";

    public static final String TAG = MainApplication.class
            .getSimpleName();

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    private static MainApplication mInstance;

    private RequestQueue mRequestQueue;

    private Map<Long, Entry> entryMap = new ConcurrentHashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        }
    }

    public synchronized Map<Long,Entry> getMapEntry() {
        return entryMap;
    }

    public Entry getEntry(long id) {
        return entryMap.get(id);
    }

    public void addEntry(Entry entry) {
        this.entryMap.put(entry.getId(), entry);
    }

    public synchronized RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
