package com.tspro.android.beatifulgirl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.tspro.android.beatifulgirl.model.Entry;
import com.tspro.android.beatifulgirl.model.From;

import java.util.ArrayList;

/**
 * Created by truonglx on 19/03/2017.
 */

public class EbookDatabaseManager extends BookDatabaseDAO {
    public static boolean READING = true;
    public static boolean FAVORITE = false;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private Callback callback;

    public EbookDatabaseManager(Context context) {
        super(context);
    }

    private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_RAW + " =?";


    public long save(Entry entry, boolean isReading) {
        if (entry == null) return -1;

        long insert = -1;
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.FULL_PICTURE, entry.getFull_picture());
        values.put(DataBaseHelper.ID, entry.getId());
        values.put(DataBaseHelper.MESSAGE, entry.getMessage());
        values.put(DataBaseHelper.USER_NAME, entry.getFrom().getNameUser());
        values.put(DataBaseHelper.UPDATED_TIME, entry.getUpdated_time());
        values.put(DataBaseHelper.USER_ID, entry.getFrom().getGirlId());
        values.put(DataBaseHelper.ID_RAW, System.currentTimeMillis());
        if (isReading) {
            values.put(DataBaseHelper.DOWNLOAD_PATH, entry.getPathDownload());
            insert = database.insert(DataBaseHelper.READING_TABLE, null, values);
        } else
            insert = database.insert(DataBaseHelper.FAVORITE_TABLE, null, values);
        if (insert != -1) callback.updateList();
        return insert;
    }

    /* public long update(Entry entry, boolean isReading) {
         if (entry == null) return -1;
         ContentValues values = new ContentValues();
         long result = -1;
         try {


             values.put(DataBaseHelper.FULL_PICTURE, entry.getItemCategoryName());
             values.put(DataBaseHelper.ID, entry.getItemCatId());
             values.put(DataBaseHelper.MESSAGE, entry.getItemEbookAuthor());
             values.put(DataBaseHelper.USER_NAME, entry.getItemDescription());
             values.put(DataBaseHelper.UPDATED_TIME, entry.getItemUrlEpub());
             values.put(DataBaseHelper.USER_ID, entry.getItemImageurl());
             values.put(DataBaseHelper.SHARE_COLUMN, entry.getItemUrlShare());
             values.put(DataBaseHelper.ID_RAW, System.currentTimeMillis());
             if (isReading) {
                 values.put(DataBaseHelper.SHARE_COLUMN, entry.getItemUrlShare());

                 result = database.update(DataBaseHelper.READING_TABLE, values,
                         WHERE_ID_EQUALS,
                         new String[]{String.valueOf(entry.getItemCatId())});
             } else {
                 result = database.update(DataBaseHelper.FAVORITE_TABLE, values,
                         WHERE_ID_EQUALS,
                         new String[]{String.valueOf(entry.getItemCatId())});
             }
             Log.d("Update Result:", "=" + result);
         } catch (Exception e) {
         }
         if (result > 0) callback.updateList();
         return result;

     }
 */
    public int delete(Entry entry, boolean isReading) {
        int delete = -1;
        try {
            if (isReading) {
                delete = database.delete(DataBaseHelper.READING_TABLE,
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

    public ArrayList<Entry> getListItemEbook(boolean isReading) {
        ArrayList<Entry> items = new ArrayList<>();
        Cursor cursor = null;
        try {
            if (isReading)
                cursor = database.query(DataBaseHelper.READING_TABLE, DataBaseHelper.PROJECTION_READING, null, null, null, null,
                        DataBaseHelper.ID_RAW + " DESC");
            else
                cursor = database.query(DataBaseHelper.FAVORITE_TABLE, DataBaseHelper.PROJECTION_FAVORITE, null, null, null, null,
                        DataBaseHelper.ID_RAW + " DESC");
            while (cursor.moveToNext()) {
                Entry item = new Entry();
                item.setIdRaw(String.valueOf(cursor.getInt(0)));
                item.setId(cursor.getLong(1));
                item.setFull_picture(cursor.getString(2));
                item.setMessage(cursor.getString(3));
                item.setUpdated_time(cursor.getLong(4));
                item.setFrom(new From(cursor.getString(5), cursor.getString(6)));

                if (isReading)
                    item.setPathDownload(cursor.getString(7));


                items.add(item);
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
                cursor = database.query(DataBaseHelper.READING_TABLE, DataBaseHelper.PROJECTION_FAVORITE, null, null, null, null,
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
