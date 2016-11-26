package com.nerminturkovic.flickrtestapp.photogallery;

import com.nerminturkovic.flickrtestapp.data.PhotosDataSource;
import com.nerminturkovic.flickrtestapp.data.model.Photo;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by nerko on 26/11/16.
 */

public class PhotoGalleryPresenter implements PhotoGalleryContract.Presenter {

    private PhotosDataSource repository;
    private WeakReference<PhotoGalleryContract.PhotoGalleryView> view;

    public PhotoGalleryPresenter(PhotosDataSource repository, PhotoGalleryContract.PhotoGalleryView view) {
        this.repository = repository;
        this.view = new WeakReference<>(view);
        this.view.get().setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadPhoto(String photoId) {
        final PhotoGalleryContract.PhotoGalleryView view = this.view.get();

        repository
                .getPhoto(photoId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Photo>() {
                    @Override
                    public void call(Photo photo) {
                        view.showPhoto(photo);
                    }
                });
    }

    @Override
    public void loadPhotos(final PhotoGalleryFragment.OnLoadPhotos callback) {
        repository
                .getPhotos()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Photo>>() {
                    @Override
                    public void call(List<Photo> photos) {
                        callback.onLoadPhotos(photos);
                    }
                });
    }
}
