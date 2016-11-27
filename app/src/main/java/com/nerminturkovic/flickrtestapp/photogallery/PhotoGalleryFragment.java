package com.nerminturkovic.flickrtestapp.photogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nerminturkovic.flickrtestapp.R;
import com.nerminturkovic.flickrtestapp.data.model.Photo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nerko on 26/11/16.
 */

public class PhotoGalleryFragment extends Fragment implements PhotoGalleryContract.PhotoGalleryView {

    public interface OnLoadPhotos {
        void onLoadPhotos(List<Photo> photoList);
    }

    @BindView(R.id.gallery_view_pager)
    ViewPager viewPager;

    private List<Photo> photos;

    private PhotoGalleryContract.Presenter presenter;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.loadPhotos(new OnLoadPhotos() {
            @Override
            public void onLoadPhotos(List<Photo> photoList) {
                photos = photoList;
                viewPager.setAdapter(new PhotoGalleryAdapter(getContext(), photos));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_gallery, container, false);

        ButterKnife.bind(this, rootView);

        presenter.loadPhoto(getArguments().getString(PhotoGalleryActivity.PHOTO_ID));

        return rootView;
    }

    @Override
    public void setPresenter(PhotoGalleryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPhoto(Photo photo) {
        viewPager.setCurrentItem(photos.indexOf(photo));
    }
}
