package com.nerminturkovic.flickrtestapp.data;

import android.content.Context;

import com.nerminturkovic.flickrtestapp.data.local.PhotosLocalDataSource;
import com.nerminturkovic.flickrtestapp.data.local.database.PhotosDBHelper;
import com.nerminturkovic.flickrtestapp.data.model.Photo;
import com.nerminturkovic.flickrtestapp.data.remote.PhotosRemoteDataSource;
import com.nerminturkovic.flickrtestapp.data.remote.RemoteServices;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nerko on 21/11/16.
 */

public class PhotosRepository implements PhotosDataSource {

    private static PhotosRepository instance;

    private PhotosDataSource localDataSource;
    private PhotosDataSource remoteDataSource;

    private List<Photo> photos;

    private PhotosRepository(Context context) {
        localDataSource = new PhotosLocalDataSource(new PhotosDBHelper(context));
        remoteDataSource = new PhotosRemoteDataSource(RemoteServices.getInstance().getPhotosService());

    }

    public static PhotosRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PhotosRepository(context);
        }

        return instance;
    }

    @Override
    public Observable<List<Photo>> getPhotos() {

        if (photos != null && !photos.isEmpty()) {
            return Observable.just(photos);
        }

        Observable<List<Photo>> localObservable = localDataSource.getPhotos();
        Observable<List<Photo>> remoteObservable = remoteDataSource.getPhotos()
                .flatMap(new Func1<List<Photo>, Observable<Photo>>() {
                    @Override
                    public Observable<Photo> call(List<Photo> photos) {
                        return Observable.from(photos);
                    }
                })
                .doOnNext(new Action1<Photo>() {
                    @Override
                    public void call(Photo photo) {
                        savePhoto(photo);
                    }
                })
                .toList();

        return Observable.concat(localObservable, remoteObservable)
                .filter(new Func1<List<Photo>, Boolean>() {
                    @Override
                    public Boolean call(List<Photo> photos) {
                        return photos != null && !photos.isEmpty();
                    }
                })
                .first()
                .doOnNext(new Action1<List<Photo>>() {
                    @Override
                    public void call(List<Photo> photos) {
                        PhotosRepository.this.photos = photos;
                    }
                });
    }

    @Override
    public Observable<Photo> getPhoto(String photoId) {
        for (Photo photo : photos) {
            if (photo.getId() == Long.valueOf(photoId)) {
                return Observable.just(photo);
            }
        }
        return Observable.empty();
    }

    @Override
    public void savePhoto(Photo photo) {
        localDataSource.savePhoto(photo);
    }
}
