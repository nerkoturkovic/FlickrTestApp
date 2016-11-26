package com.nerminturkovic.flickrtestapp.photogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nerminturkovic.flickrtestapp.R;
import com.nerminturkovic.flickrtestapp.data.model.Photo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nerko on 26/11/16.
 */

public class PhotoGalleryFragment extends Fragment implements PhotoGalleryContract.PhotoGalleryView {

    @BindView(R.id.image_view)
    ImageView imageView;

    private String photoId;

    private PhotoGalleryContract.Presenter presenter;
    
    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoId = getArguments().getString(PhotoGalleryActivity.PHOTO_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_gallery, container, false);

        ButterKnife.bind(this, rootView);

        presenter.loadPhoto(photoId);

        return rootView;
    }

    @Override
    public void setPresenter(PhotoGalleryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPhoto(Photo photo) {
        Picasso
                .with(getContext())
                .load(photo.getSizes().get(photo.getSizes().size() - 1).getSource())
                .into(imageView);
    }
}
