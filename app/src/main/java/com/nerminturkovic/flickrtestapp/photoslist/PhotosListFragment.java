package com.nerminturkovic.flickrtestapp.photoslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nerminturkovic.flickrtestapp.R;
import com.nerminturkovic.flickrtestapp.data.model.Photo;
import com.nerminturkovic.flickrtestapp.photogallery.PhotoGalleryActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nerko on 23/11/16.
 */

public class PhotosListFragment extends Fragment implements PhotosListContract.PhotosListView {

    private PhotosListContract.Presenter presenter;

    @BindView(R.id.photos_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.photos_list_loading_bar)
    ProgressBar progressBar;

    private OnPhotoSelected photoSelectedCallback = new OnPhotoSelected() {
        @Override
        public void photoSelected(String photoId) {
            showPhotoInGallery(photoId);
        }
    };

    private PhotosListAdapter adapter;
    
    public PhotosListFragment() {
        
    }
    
    public static PhotosListFragment newInstance() {
        return new PhotosListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photos_list, container, false);

        ButterKnife.bind(this, rootView);

        adapter = new PhotosListAdapter(getContext(), photoSelectedCallback);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return rootView;
    }

    @Override
    public void setPresenter(PhotosListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPhotos(List<Photo> photos) {
        adapter.setPhotos(photos);
    }

    @Override
    public void addPhotoToList(Photo photo) {
        adapter.addPhoto(photo);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPhotoInGallery(String photoId) {
        Intent intent = new Intent(getContext(), PhotoGalleryActivity.class);
        intent.putExtra(PhotoGalleryActivity.PHOTO_ID, photoId);
        startActivity(intent);
    }

    public interface OnPhotoSelected {
        void photoSelected(String photoId);
    }
}
