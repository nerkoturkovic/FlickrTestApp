package com.nerminturkovic.flickrtestapp.data.local.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nerko on 21/11/16.
 */

public class PhotosDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "fickr.db";

    public static final int DATABASE_VERSION = 1;

    public PhotosDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPhotoTable(db);
        createOwnerTable(db);
        createLocationTable(db);
        createSizeTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       //Not necessary
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);

        //Not necessary
    }

    private void createPhotoTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + PhotosContracts.PhotoContract.TABLE_NAME + " ( " +
                PhotosContracts.PhotoContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PhotosContracts.PhotoContract.COLUMN_FLICKR_ID + " TEXT," +
                PhotosContracts.PhotoContract.COLUMN_TITLE + " TEXT" +
            ");";

        db.execSQL(sql);
    }

    private void createOwnerTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + PhotosContracts.OwnerContract.TABLE_NAME + " ( " +
                PhotosContracts.OwnerContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PhotosContracts.OwnerContract.COLUMN_PHOTO_ID + " INTEGER," +
                PhotosContracts.OwnerContract.COLUMN_FLICKR_ID + " TEXT," +
                PhotosContracts.OwnerContract.COLUMN_REALNAME + " TEXT," +
                PhotosContracts.OwnerContract.COLUMN_USERNAME + " TEXT" +
                ");";

        db.execSQL(sql);
    }

    private void createLocationTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + PhotosContracts.LocationContract.TABLE_NAME + " ( " +
                PhotosContracts.LocationContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PhotosContracts.LocationContract.COLUMN_PHOTO_ID + " INTEGER," +
                PhotosContracts.LocationContract.COLUMN_LONGITUDE + " REAL," +
                PhotosContracts.LocationContract.COLUMN_LATITUDE + " REAL," +
                PhotosContracts.LocationContract.COLUMN_LOCALITY + " TEXT," +
                PhotosContracts.LocationContract.COLUMN_COUNTY + " TEXT," +
                PhotosContracts.LocationContract.COLUMN_REGION + " TEXT," +
                PhotosContracts.LocationContract.COLUMN_COUNTRY + " TEXT" +
                ");";

        db.execSQL(sql);
    }

    private void createSizeTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + PhotosContracts.PhotoSIzeContract.TABLE_NAME + " ( " +
                PhotosContracts.PhotoSIzeContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PhotosContracts.PhotoSIzeContract.COLUMN_PHOTO_ID + " INTEGER," +
                PhotosContracts.PhotoSIzeContract.COLUMN_HEIGHT + " INTEGER," +
                PhotosContracts.PhotoSIzeContract.COLUMN_WIDTH + " INTEGER," +
                PhotosContracts.PhotoSIzeContract.COLUMN_LABEL + " TEXT," +
                PhotosContracts.PhotoSIzeContract.COLUMN_SOURCE + " TEXT," +
                PhotosContracts.PhotoSIzeContract.COLUMN_URL + " TEXT" +
                ");";

        db.execSQL(sql);
    }
}
