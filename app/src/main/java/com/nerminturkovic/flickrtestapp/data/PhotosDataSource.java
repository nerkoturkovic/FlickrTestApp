package com.nerminturkovic.flickrtestapp.data;

import com.nerminturkovic.flickrtestapp.data.model.Photo;

import java.util.List;

import rx.Observable;

/**
 * Created by nerko on 21/11/16.
 */

public interface PhotosDataSource {

    Observable<List<Photo>> getPhotos();

    Observable<Photo> getPhoto(String photoId);

    void savePhoto(Photo photo);
}
