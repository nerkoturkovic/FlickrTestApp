package com.nerminturkovic.flickrtestapp.photogallery;

import com.nerminturkovic.flickrtestapp.BasePresenter;
import com.nerminturkovic.flickrtestapp.BaseView;
import com.nerminturkovic.flickrtestapp.data.model.Photo;

/**
 * Created by nerko on 26/11/16.
 */

public class PhotoGalleryContract {

    public interface PhotoGalleryView extends BaseView<Presenter> {
        void showPhoto(Photo photo);
    }

    public interface Presenter extends BasePresenter {
        void loadPhoto(String photoId);
    }
}
