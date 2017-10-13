package com.tspro.android.beatifulgirl.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

/**
 * Created by truonglx on 19/03/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "book.db";
    private static final int DATABASE_VERSION = 1;

    public static final String FAVORITE_TABLE = "favorite";
    public static final String READING_TABLE = "download";

    public static final String ID_RAW = "id_raw";
    public static final String ID = "id";
    public static final String FULL_PICTURE = "full_picture";
    public static final String MESSAGE = "message";
    public static final String UPDATED_TIME = "update_time";
    public static final String USER_NAME = "name_user";
    public static final String USER_ID = "id_user";
    //    public static final String SHARE_COLUMN = "b_share" ;
    public static final String DOWNLOAD_PATH = "path_download";

    public static final String CREATE_FAVORITE_TABLE = "CREATE TABLE "
            + FAVORITE_TABLE + "(" + ID_RAW + " TEXT PRIMARY KEY, "
            + FULL_PICTURE + " TEXT, " + MESSAGE + " TEXT, "
            + UPDATED_TIME + " INTEGER, " + USER_NAME + " TEXT, "
            + USER_ID + " TEXT, "
            + ID + " INTEGER"
            + " )";

    public static final String CREATE_READING_TABLE = "CREATE TABLE "
            + READING_TABLE + "(" + ID_RAW + " TEXT PRIMARY KEY, "
            + FULL_PICTURE + " TEXT, " + MESSAGE + " TEXT, "
            + UPDATED_TIME + " INTEGER, " + USER_NAME + " TEXT,"
            + USER_ID + " TEXT, "
            + ID + " INTEGER, " + DOWNLOAD_PATH + " TEXT"
            + " )";
    static final String[] PROJECTION_FAVORITE = new String[]{
            ID_RAW,                          // 0
            ID,                      // 1
            FULL_PICTURE,                    // 2
            MESSAGE,                  // 3
            UPDATED_TIME,                    // 4
            USER_NAME,             // 5
            USER_ID,                   // 6

    };
    static final String[] PROJECTION_READING = new String[]{
            ID_RAW,                      // 0
            ID,                          // 1
            FULL_PICTURE,                    // 2
            MESSAGE,                  // 3
            UPDATED_TIME,                    // 4
            USER_NAME,             // 5
            USER_ID,                   // 6
            DOWNLOAD_PATH,                           // 7

    };
    private static final boolean DEBUG = false;
    private static final String TAG = DataBaseHelper.class.getSimpleName();

    private static DataBaseHelper instance;
    //private final Context mContext;

    public static synchronized DataBaseHelper getInstance(Context context) {
        if (DEBUG) {
            Log.v(TAG, "Getting Instance");
        }
        if (instance == null) {
            // Use application context instead of activity context because this is a singleton,
            // and we don't want to leak the activity if the activity is not running but the
            // dialer database helper is still doing work.
            instance = new DataBaseHelper(context.getApplicationContext(),
                    DATABASE_NAME);
        }
        return instance;
    }

    @VisibleForTesting
    static DataBaseHelper getNewInstanceForTest(Context context) {
        return new DataBaseHelper(context, null);
    }

    protected DataBaseHelper(Context context, String databaseName) {
        this(context, databaseName, DATABASE_VERSION);
    }

    protected DataBaseHelper(Context context, String databaseName, int dbVersion) {
        super(context, databaseName, null, dbVersion);
        //   mContext = Preconditions.checkNotNull(context, "Context must not be null");
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_READING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + READING_TABLE);

        // Create tables again
        onCreate(db);
    }
}
