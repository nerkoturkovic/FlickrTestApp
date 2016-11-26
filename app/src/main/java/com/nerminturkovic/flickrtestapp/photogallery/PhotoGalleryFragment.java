package com.nerminturkovic.flickrtestapp.photogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nerminturkovic.flickrtestapp.R;
import com.nerminturkovic.flickrtestapp.data.model.Photo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.photo_progress_bar)
    ProgressBar progressBar;

    private String photoId;
    private Photo currentPhoto;

    private List<Photo> photos;

    private PhotoGalleryContract.Presenter presenter;

    private GestureDetector gestureDetector;
    
    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoId = getArguments().getString(PhotoGalleryActivity.PHOTO_ID);

        presenter.loadPhotos(new OnLoadPhotos() {
            @Override
            public void onLoadPhotos(List<Photo> photoList) {
                photos = photoList;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_gallery, container, false);

        ButterKnife.bind(this, rootView);

        gestureDetector = new GestureDetector(getContext(), new CustomGestureListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        presenter.loadPhoto(photoId);

        return rootView;
    }

    @Override
    public void setPresenter(PhotoGalleryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPhoto(Photo photo) {
        photoId = String.valueOf(photo.getId());
        currentPhoto = photo;
        progressBar.setVisibility(View.VISIBLE);
        Picasso
                .with(getContext())
                .load(photo.getSizes().get(photo.getSizes().size() - 1).getSource())
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (photos == null || photos.isEmpty()) {
                return false;
            }

            int currentIndex = photos.indexOf(currentPhoto);
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if (currentIndex < photos.size() - 2) {
                    presenter.loadPhoto(String.valueOf(photos.get(currentIndex + 1).getId()));
                }
                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if (currentIndex > 0) {
                    presenter.loadPhoto(String.valueOf(photos.get(currentIndex - 1).getId()));
                }
                return false; // Left to right
            }

            return false;
        }
    }
}
