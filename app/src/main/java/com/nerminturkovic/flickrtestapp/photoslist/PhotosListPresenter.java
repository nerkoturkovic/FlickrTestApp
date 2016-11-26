package com.nerminturkovic.flickrtestapp.photoslist;

import com.nerminturkovic.flickrtestapp.data.PhotosRepository;
import com.nerminturkovic.flickrtestapp.data.model.Photo;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nerko on 23/11/16.
 */

public class PhotosListPresenter implements PhotosListContract.Presenter {

    private PhotosRepository repository;

    private WeakReference<PhotosListContract.PhotosListView> view;

    public PhotosListPresenter(PhotosRepository repository, PhotosListContract.PhotosListView view) {
        this.repository = repository;
        this.view = new WeakReference<>(view);
        this.view.get().setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadPhotos();
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadPhotos() {
        repository.getPhotos()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<Photo> photos) {
                        PhotosListContract.PhotosListView view = PhotosListPresenter.this.view.get();
                        if (view != null) {
                            view.showPhotos(photos);
                            view.hideProgressBar();
                        }
                    }
                });
    }
}
