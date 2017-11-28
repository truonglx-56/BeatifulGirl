package com.tspro.project.girl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.tspro.project.girl.model.Entry;
import com.tspro.project.girl.model.From;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by truonglx on 19/03/2017.
 */

public class GirlDatabaseManager extends GirlDatabaseDAO {
    public static boolean DOWNLOAD = true;
    public static boolean FAVORITE = false;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private Callback callback;

    public GirlDatabaseManager(Context context, Callback callback) {
        super(context);
        this.callback = callback;
    }

    private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_RAW + " =?";


    public long save(Entry entry, boolean isDownloaded) {
        if (entry == null) return -1;

        long insert = -1;
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.FULL_PICTURE, entry.getFull_picture());
        values.put(DataBaseHelper.ID, entry.getId());
        values.put(DataBaseHelper.MESSAGE, entry.getMessage());
        values.put(DataBaseHelper.USER_NAME, entry.getFrom().getNameUser());
        values.put(DataBaseHelper.UPDATED_TIME, System.currentTimeMillis());
        values.put(DataBaseHelper.USER_ID, entry.getFrom().getGirlId());
        values.put(DataBaseHelper.ID_RAW, entry.getIdRaw());
        if (isDownloaded) {
            values.put(DataBaseHelper.DOWNLOAD_PATH, entry.getPathDownload());
            insert = database.insert(DataBaseHelper.DOWNLOAD_TABLE, null, values);
        } else
            insert = database.insert(DataBaseHelper.FAVORITE_TABLE, null, values);
        if (insert != -1) callback.updateList();
        return insert;
    }

    public int delete(Entry entry, boolean isDownload) {
        int delete = -1;
        try {
            if (isDownload) {
                delete = database.delete(DataBaseHelper.DOWNLOAD_TABLE,
                        WHERE_ID_EQUALS, new String[]{entry.getIdRaw() + ""});
            } else {
                delete = database.delete(DataBaseHelper.FAVORITE_TABLE,
                        WHERE_ID_EQUALS, new String[]{entry.getIdRaw() + ""});
            }
        } catch (Exception e) {
        }
        if (delete > 0) callback.updateList();
        return delete;
    }

    public ConcurrentHashMap<String, Entry> getListItemGirl(boolean isDownloaded) {
        ConcurrentHashMap<String, Entry> items = new ConcurrentHashMap<>();
        Cursor cursor = null;
        try {
            if (isDownloaded)
                cursor = database.query(DataBaseHelper.DOWNLOAD_TABLE, DataBaseHelper.PROJECTION_DOWNLOAD, null, null, null, null,
                        DataBaseHelper.ID_RAW + " DESC");
            else
                cursor = database.query(DataBaseHelper.FAVORITE_TABLE, DataBaseHelper.PROJECTION_FAVORITE, null, null, null, null,
                        DataBaseHelper.ID_RAW + " DESC");
            while (cursor.moveToNext()) {
                Entry item = new Entry();
                item.setIdRaw(cursor.getString(0));
                item.setId(cursor.getLong(1));
                item.setFull_picture(cursor.getString(2));
                item.setMessage(cursor.getString(3));
                item.setUpdated_time(cursor.getLong(4));
                item.setFrom(new From(cursor.getString(5), cursor.getString(6)));

                if (isDownloaded)
                    item.setPathDownload(cursor.getString(7));


                items.put(item.getIdRaw(), item);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return items;
    }

    /*public ConcurrentHashMap<String, Entry> getItemEbook(boolean isReading) {
        ConcurrentHashMap<String, Entry> items = new ConcurrentHashMap<>();
        Cursor cursor = null;
        try {
            if (isReading)
                cursor = database.query(DataBaseHelper.DOWNLOAD_TABLE, DataBaseHelper.PROJECTION_FAVORITE, null, null, null, null,
                        DataBaseHelper.ID + " DESC");
            else
                cursor = database.query(DataBaseHelper.FAVORITE_TABLE, DataBaseHelper.PROJECTION_FAVORITE, null, null, null, null,
                        DataBaseHelper.ID + " DESC");
            while (cursor.moveToNext()) {
                Entry item = new Entry();
                item.setItemCatId(String.valueOf(cursor.getInt(0)));
                item.setItemCategoryName(cursor.getString(1));
                item.setItemEbookAuthor(cursor.getString(2));
                item.setItemUrlEpub(cursor.getString(3));
                item.setItemDescription(cursor.getString(4));
                item.setItemImageurl(cursor.getString(5));
                item.setItemUrlShare(cursor.getString(6));
                item.setItemIndex(cursor.getInt(7));
                if (isReading) item.setItemEbookAuthor(cursor.getString(8));
                items.put(item.getItemCatId(), item);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return items;
    }*/

    public interface Callback {
        void updateList();
    }
}
