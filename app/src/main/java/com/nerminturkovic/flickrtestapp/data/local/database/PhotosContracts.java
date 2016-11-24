package com.nerminturkovic.flickrtestapp.data.local.database;

import android.provider.BaseColumns;

/**
 * Created by nerko on 23/11/16.
 */

public class PhotosContracts {

    public static abstract class PhotoContract implements BaseColumns {
        public static final String TABLE_NAME = "photo";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_FLICKR_ID = "flickrid";
    }

    public static abstract class OwnerContract implements BaseColumns {
        public static final String TABLE_NAME = "owner";
        public static final String COLUMN_PHOTO_ID = "photoid";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_REALNAME = "realname";
        public static final String COLUMN_FLICKR_ID = "flickrid";
    }

    public static abstract class LocationContract implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_PHOTO_ID = "photoid";
        public static final String COLUMN_LATITUDE = "username";
        public static final String COLUMN_LONGITUDE = "realname";
        public static final String COLUMN_LOCALITY = "locality";
        public static final String COLUMN_COUNTY = "county";
        public static final String COLUMN_REGION = "region";
        public static final String COLUMN_COUNTRY = "country";
    }

    public static abstract class PhotoSIzeContract implements BaseColumns {
        public static final String TABLE_NAME = "photosize";
        public static final String COLUMN_PHOTO_ID = "photoid";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_LABEL = "label";
        public static final String COLUMN_WIDTH= "width";
        public static final String COLUMN_HEIGHT = "height";
    }
}
