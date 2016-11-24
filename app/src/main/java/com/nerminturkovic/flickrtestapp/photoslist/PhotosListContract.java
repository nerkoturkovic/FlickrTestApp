package com.nerminturkovic.flickrtestapp.photoslist;

import com.nerminturkovic.flickrtestapp.BasePresenter;
import com.nerminturkovic.flickrtestapp.BaseView;
import com.nerminturkovic.flickrtestapp.data.model.Photo;

import java.util.List;

/**
 * Created by nerko on 23/11/16.
 */

public class PhotosListContract {

    public interface PhotosListView extends BaseView<Presenter> {
        void addPhotoToList(Photo photo);

        void showPhotos(List<Photo> photos);

        void hideProgressBar();
    }

    public interface Presenter extends BasePresenter {

        void loadPhotos();
    }
}
