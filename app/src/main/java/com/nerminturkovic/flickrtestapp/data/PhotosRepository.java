package com.nerminturkovic.flickrtestapp.data;

import android.util.Log;

import com.nerminturkovic.flickrtestapp.data.model.Photo;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nerko on 21/11/16.
 */

public class PhotosRepository implements PhotosDataSource {

    private PhotosDataSource localDataSource;
    private PhotosDataSource remoteDataSource;

    public PhotosRepository(PhotosDataSource localDataSource, PhotosDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<List<Photo>> getPhotos() {
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
                .first();
    }

    @Override
    public Observable<Photo> getPhoto(String photoId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void savePhoto(Photo photo) {
        localDataSource.savePhoto(photo);
    }
}
