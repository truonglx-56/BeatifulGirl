package com.tspro.project.girl;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tspro.project.girl.database.GirlDatabaseManager;
import com.tspro.project.girl.model.Entry;
import com.tspro.project.girl.model.Group;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by truonglx on 02/10/2017.
 */

public class BeatifulApplication extends Application implements GirlDatabaseManager.Callback {
    public static final String JSON_ARRAY_QUEUE_LIST = "json_queue_array";
    public static final String JSON_QUERY_GROUP = "json_query_group";
    public static final String STORE_FILE_NAME = "data_app_girl";
    private static final String GROUP_TAG = "group";
    private static final String TOKEN_CURRENT = "current_token";


    public static final String TAG = BeatifulApplication.class
            .getSimpleName();

    public static BeatifulApplication getInstance() {
        return mInstance;
    }

    private static BeatifulApplication mInstance;

    private RequestQueue mRequestQueue;

    private Map<Long, Entry> entryMap = new ConcurrentHashMap<>();

    private GirlDatabaseManager girlDatabaseManager;

    public ConcurrentHashMap<String, Entry> getAllFavorite() {
        return allFavorite;
    }

    private ConcurrentHashMap<String, Entry> allFavorite = new ConcurrentHashMap<>();

    public void addCallback(Callback callback) {
        callbacks.add(callback);
    }

    private List<Callback> callbacks;

    private String next;

    public synchronized String getNext() {
        return next;
    }

    public synchronized void setNext(String next) {
        this.next = next;
    }

    SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = BeatifulApplication.this;
        sharedPref = getApplicationContext().getSharedPreferences(STORE_FILE_NAME, Context.MODE_PRIVATE);
        callbacks = new ArrayList<>();
        //Downloader.init(this);
        girlDatabaseManager = new GirlDatabaseManager(this, this);
        allFavorite = girlDatabaseManager.getListItemGirl(false);

        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        }
    }

    public void initDownload() {
        Downloader.init(this);
    }

    public synchronized Map<Long, Entry> getMapEntry() {
        return entryMap;
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

    public GirlDatabaseManager getGirlDatabaseManager() {
        return girlDatabaseManager;
    }

    @Override
    public void updateList() {
        synchronized (allFavorite) {
            allFavorite = girlDatabaseManager.getListItemGirl(false);

            if (callbacks.size() > 0) {
                for (Callback callback : callbacks) {
                    callback.updateData();
                }
            }
        }
    }

    public boolean isFavorite(String idRaw) {
        return allFavorite.containsKey(idRaw);
    }

    public List<Group> getDataFromSharedPreferences() {
        try {
            Gson gson = new Gson();
            List<Group> groupFromData;
            SharedPreferences sharedPref = getSharedPreferences(STORE_FILE_NAME, Context.MODE_PRIVATE);
            String jsonPreferences = sharedPref.getString(GROUP_TAG, "");

            Type type = new TypeToken<List<Group>>() {
            }.getType();
            groupFromData = new ArrayList<>(new HashSet((List) gson.fromJson(jsonPreferences, type)));
            Collections.sort(groupFromData);

            return groupFromData;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public void addInJSONArray(Group productToAdd) throws Exception {

        Gson gson = new Gson();

        String jsonSaved = sharedPref.getString(GROUP_TAG, "");
        String jsonNewproductToAdd = gson.toJson(productToAdd);

        JSONArray jsonArrayProduct = new JSONArray();


        if (jsonSaved.length() != 0) {
            jsonArrayProduct = new JSONArray(jsonSaved);
        }

        jsonArrayProduct.put(new JSONObject(jsonNewproductToAdd));

        //SAVE NEW ARRAY
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GROUP_TAG, String.valueOf(jsonArrayProduct));
        editor.commit();
    }

    public void updateInJSONArray(Group productToUpdate) throws Exception {

        Gson gson = new Gson();

        String jsonSaved = sharedPref.getString(GROUP_TAG, "");

        JSONArray jsonArrayProduct = new JSONArray();


        if (jsonSaved.length() != 0) {
            jsonArrayProduct = new JSONArray(jsonSaved);
        }
        int pos = -1;
        for (int i = 0; i < jsonArrayProduct.length(); i++) {
            JSONObject jsonObject = jsonArrayProduct.getJSONObject(i);
            Group group = gson.fromJson(jsonObject.toString(), Group.class);
            if (group != null & group.equals(productToUpdate)) {
                pos = i;
                break;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            jsonArrayProduct.remove(pos);
            jsonArrayProduct.put(productToUpdate);
        }
        //SAVE NEW ARRAY
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GROUP_TAG, String.valueOf(jsonArrayProduct));
        editor.commit();
    }

    public void removeInJSONArray(Group productToAdd) throws Exception {

        Gson gson = new Gson();

        String jsonSaved = sharedPref.getString(GROUP_TAG, "");

        JSONArray jsonArrayProduct = new JSONArray();


        if (jsonSaved.length() != 0) {
            jsonArrayProduct = new JSONArray(jsonSaved);
        }
        int pos = -1;
        for (int i = 0; i < jsonArrayProduct.length(); i++) {
            JSONObject jsonObject = jsonArrayProduct.getJSONObject(i);
            Group group = gson.fromJson(jsonObject.toString(), Group.class);
            if (group != null & group.equals(productToAdd)) {
                pos = i;
                break;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            jsonArrayProduct.remove(pos);
        }
        //SAVE NEW ARRAY
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GROUP_TAG, String.valueOf(jsonArrayProduct));
        editor.commit();
    }

    public void putCurrentToken(String token) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TOKEN_CURRENT, token);
        editor.commit();
    }

    public String getCurrentToken() {
        return sharedPref.getString(TOKEN_CURRENT, null);
    }

    public interface Callback {
        void updateData();
    }
}
