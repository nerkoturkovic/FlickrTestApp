package com.nerminturkovic.flickrtestapp.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nerminturkovic.flickrtestapp.data.PhotosDataSource;
import com.nerminturkovic.flickrtestapp.data.local.database.PhotosContracts;
import com.nerminturkovic.flickrtestapp.data.local.database.PhotosDBHelper;
import com.nerminturkovic.flickrtestapp.data.model.Location;
import com.nerminturkovic.flickrtestapp.data.model.Owner;
import com.nerminturkovic.flickrtestapp.data.model.Photo;
import com.nerminturkovic.flickrtestapp.data.model.PhotoSize;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;

/**
 * Created by nerko on 21/11/16.
 */

public class PhotosLocalDataSource implements PhotosDataSource {

    private SQLiteDatabase database;

    private Func1<Cursor, Photo> cursorToPhotoFunc = new Func1<Cursor, Photo>() {
        @Override
        public Photo call(Cursor cursor) {
            Photo photo = new Photo();
            photo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(PhotosContracts.PhotoContract._ID)));
            photo.setFlickrId(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.PhotoContract.COLUMN_FLICKR_ID)));
            photo.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.PhotoContract.COLUMN_TITLE)));
            return photo;
        }
    };

    private Func1<Cursor, Owner> cursorToOwnerFunc = new Func1<Cursor, Owner>() {
        @Override
        public Owner call(Cursor cursor) {
            Owner owner = new Owner();
            owner.setId(cursor.getLong(cursor.getColumnIndexOrThrow(PhotosContracts.OwnerContract._ID)));
            owner.setFlickrId(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.OwnerContract.COLUMN_FLICKR_ID)));
            owner.setRealName(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.OwnerContract.COLUMN_REALNAME)));
            owner.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.OwnerContract.COLUMN_USERNAME)));
            return owner;
        }
    };

    private Func1<Cursor, Location> cursorToLocationFunc = new Func1<Cursor, Location>() {
        @Override
        public Location call(Cursor cursor) {
            Location location = new Location();
            location.setId(cursor.getLong(cursor.getColumnIndexOrThrow(PhotosContracts.LocationContract._ID)));
            location.setLatitude(cursor.getFloat(cursor.getColumnIndexOrThrow(PhotosContracts.LocationContract.COLUMN_LATITUDE)));
            location.setLongitude(cursor.getFloat(cursor.getColumnIndexOrThrow(PhotosContracts.LocationContract.COLUMN_LONGITUDE)));
            location.setLocality(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.LocationContract.COLUMN_LOCALITY)));
            location.setCounty(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.LocationContract.COLUMN_COUNTY)));
            location.setRegion(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.LocationContract.COLUMN_REGION)));
            location.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.LocationContract.COLUMN_COUNTRY)));
            return location;
        }
    };

    private Func1<Cursor, PhotoSize> cursorToPhotoSizeFunc = new Func1<Cursor, PhotoSize>() {
        @Override
        public PhotoSize call(Cursor cursor) {
            PhotoSize size = new PhotoSize();
            size.setId(cursor.getLong(cursor.getColumnIndexOrThrow(PhotosContracts.PhotoSIzeContract._ID)));
            size.setLabel(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.PhotoSIzeContract.COLUMN_LABEL)));
            size.setSource(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.PhotoSIzeContract.COLUMN_SOURCE)));
            size.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(PhotosContracts.PhotoSIzeContract.COLUMN_URL)));
            size.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(PhotosContracts.PhotoSIzeContract.COLUMN_WIDTH)));
            size.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(PhotosContracts.PhotoSIzeContract.COLUMN_HEIGHT)));
            return size;
        }
    };

    public PhotosLocalDataSource(PhotosDBHelper photosDBHelper) {
//        databaseHelper = new SqlBrite.Builder().build().wrapDatabaseHelper(photosDBHelper, Schedulers.io());
        this.database = photosDBHelper.getWritableDatabase();
    }

    @Override
    public Observable<List<Photo>> getPhotos() {
//        String sql = "SELECT * FROM " + PhotosContracts.PhotoContract.TABLE_NAME;
        String[] columns = {
                PhotosContracts.PhotoContract._ID,
                PhotosContracts.PhotoContract.COLUMN_FLICKR_ID,
                PhotosContracts.PhotoContract.COLUMN_TITLE
        };
        final Cursor query = database.query(false, PhotosContracts.PhotoContract.TABLE_NAME, columns, null, null, null, null, null, null);
        List<Photo> photos = new ArrayList<>();
        query.moveToFirst();
        while (!query.isAfterLast()) {
            photos.add(cursorToPhotoFunc.call(query));
            query.moveToNext();
        }
        return Observable.just(photos)
                .flatMap(new Func1<List<Photo>, Observable<Photo>>() {
                    @Override
                    public Observable<Photo> call(List<Photo> photos) {
                        return Observable.from(photos);
                    }
                })
                .concatMap(new Func1<Photo, Observable<Photo>>() {
                    @Override
                    public Observable<Photo> call(final Photo photo) {
                        return Observable.zip(getLocation(photo.getId()), getOwner(photo.getId()), getSizes(photo.getId()),
                                new Func3<Location, Owner, List<PhotoSize>, Photo>() {
                                    @Override
                                    public Photo call(Location location, Owner owner, List<PhotoSize> photoSizes) {
                                        photo.setLocation(location);
                                        photo.setOwner(owner);
                                        photo.setSizes(photoSizes);
                                        return photo;
                                    }
                                });
                    }
                })
                .toList();
    }

    private Observable<Location> getLocation(long photoId) {
        String[] columns = {
                PhotosContracts.LocationContract._ID,
                PhotosContracts.LocationContract.COLUMN_COUNTRY,
                PhotosContracts.LocationContract.COLUMN_COUNTY,
                PhotosContracts.LocationContract.COLUMN_LATITUDE,
                PhotosContracts.LocationContract.COLUMN_LONGITUDE,
                PhotosContracts.LocationContract.COLUMN_REGION,
                PhotosContracts.LocationContract.COLUMN_LOCALITY
        };
        String where = PhotosContracts.PhotoSIzeContract.COLUMN_PHOTO_ID + " = ?";
        String[] whereArgs = {
                String.valueOf(photoId)
        };
        final Cursor query = database.query(false, PhotosContracts.LocationContract.TABLE_NAME, columns, where, whereArgs, null, null, null, null);
        query.moveToFirst();
        Location location = null;
        if (query.getCount() > 0) {
            location = cursorToLocationFunc.call(query);
        }
        return Observable.just(location);
    }

    private Observable<Owner> getOwner(long photoId) {
        String[] columns = {
                PhotosContracts.OwnerContract._ID,
                PhotosContracts.OwnerContract.COLUMN_FLICKR_ID,
                PhotosContracts.OwnerContract.COLUMN_REALNAME,
                PhotosContracts.OwnerContract.COLUMN_USERNAME
        };
        String where = PhotosContracts.PhotoSIzeContract.COLUMN_PHOTO_ID + " = ?";
        String[] whereArgs = {
                String.valueOf(photoId)
        };
        final Cursor query = database.query(false, PhotosContracts.OwnerContract.TABLE_NAME, columns, where, whereArgs, null, null, null, null);
        query.moveToFirst();
        Owner owner = null;
        if (query.getCount() > 0) {
            owner = cursorToOwnerFunc.call(query);
        }
        return Observable.just(owner);
    }

    private Observable<List<PhotoSize>> getSizes(long photoId) {
        String[] columns = {
                PhotosContracts.PhotoSIzeContract._ID,
                PhotosContracts.PhotoSIzeContract.COLUMN_HEIGHT,
                PhotosContracts.PhotoSIzeContract.COLUMN_LABEL,
                PhotosContracts.PhotoSIzeContract.COLUMN_SOURCE,
                PhotosContracts.PhotoSIzeContract.COLUMN_WIDTH,
                PhotosContracts.PhotoSIzeContract.COLUMN_URL
        };
        String where = PhotosContracts.PhotoSIzeContract.COLUMN_PHOTO_ID + " = ?";
        String[] whereArgs = {
            String.valueOf(photoId)
        };
        final Cursor query = database.query(false, PhotosContracts.PhotoSIzeContract.TABLE_NAME, columns, where, whereArgs, null, null, null, null);
        List<PhotoSize> sizes = new ArrayList<>();
        query.moveToFirst();
        while (!query.isAfterLast()) {
            sizes.add(cursorToPhotoSizeFunc.call(query));
            query.moveToNext();
        }
        return Observable.just(sizes);
    }

    @Override
    public Observable<Photo> getPhoto(String photoId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void savePhoto(Photo photo) {
        ContentValues photoValues = new ContentValues();
        photoValues.put(PhotosContracts.PhotoContract.COLUMN_TITLE, photo.getTitle());
        photoValues.put(PhotosContracts.PhotoContract.COLUMN_FLICKR_ID, photo.getFlickrId());
        long newId = database.insert(PhotosContracts.PhotoContract.TABLE_NAME, null, photoValues);

        if (photo.getLocation() != null) {
            saveLocation(photo.getLocation(), newId);
        }
        saveOwner(photo.getOwner(), newId);
        saveSizes(photo.getSizes(), newId);
    }

    private void saveLocation(Location location, long photoId) {
        ContentValues locationValues = new ContentValues();
        locationValues.put(PhotosContracts.LocationContract.COLUMN_LATITUDE, location.getLatitude());
        locationValues.put(PhotosContracts.LocationContract.COLUMN_LONGITUDE, location.getLongitude());
        locationValues.put(PhotosContracts.LocationContract.COLUMN_LOCALITY, location.getLocality());
        locationValues.put(PhotosContracts.LocationContract.COLUMN_COUNTY, location.getCounty());
        locationValues.put(PhotosContracts.LocationContract.COLUMN_REGION, location.getRegion());
        locationValues.put(PhotosContracts.LocationContract.COLUMN_COUNTRY, location.getCountry());
        locationValues.put(PhotosContracts.LocationContract.COLUMN_PHOTO_ID, photoId);
        database.insert(PhotosContracts.LocationContract.TABLE_NAME, null, locationValues);
    }

    private void saveOwner(Owner owner, long photoId) {
        ContentValues ownerValues = new ContentValues();
        ownerValues.put(PhotosContracts.OwnerContract.COLUMN_FLICKR_ID, owner.getFlickrId());
        ownerValues.put(PhotosContracts.OwnerContract.COLUMN_USERNAME, owner.getUsername());
        ownerValues.put(PhotosContracts.OwnerContract.COLUMN_REALNAME, owner.getRealName());
        ownerValues.put(PhotosContracts.OwnerContract.COLUMN_PHOTO_ID, photoId);
        database.insert(PhotosContracts.OwnerContract.TABLE_NAME, null, ownerValues);
    }

    private void saveSizes(List<PhotoSize> sizes, long photoId) {
        for (PhotoSize size : sizes) {
            ContentValues sizeValues = new ContentValues();
            sizeValues.put(PhotosContracts.PhotoSIzeContract.COLUMN_URL, size.getUrl());
            sizeValues.put(PhotosContracts.PhotoSIzeContract.COLUMN_SOURCE, size.getSource());
            sizeValues.put(PhotosContracts.PhotoSIzeContract.COLUMN_LABEL, size.getLabel());
            sizeValues.put(PhotosContracts.PhotoSIzeContract.COLUMN_WIDTH, size.getWidth());
            sizeValues.put(PhotosContracts.PhotoSIzeContract.COLUMN_HEIGHT, size.getHeight());
            sizeValues.put(PhotosContracts.PhotoSIzeContract.COLUMN_PHOTO_ID, photoId);
            database.insert(PhotosContracts.PhotoSIzeContract.TABLE_NAME, null, sizeValues);
        }
    }

}
