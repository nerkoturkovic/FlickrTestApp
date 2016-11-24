package com.nerminturkovic.flickrtestapp.photoslist;

import com.nerminturkovic.flickrtestapp.data.PhotosRepository;
import com.nerminturkovic.flickrtestapp.data.model.Photo;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nerko on 23/11/16.
 */

public class PhotosListPresenter implements PhotosListContract.Presenter {

    private PhotosRepository repository;

    private PhotosListContract.PhotosListView view;

    public PhotosListPresenter(PhotosRepository repository, PhotosListContract.PhotosListView view) {
        this.repository = repository;
        this.view = view;
        this.view.setPresenter(this);
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
                        view.showPhotos(photos);
                        view.hideProgressBar();
                    }
                });
    }
}
