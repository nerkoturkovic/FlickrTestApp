package com.nerminturkovic.flickrtestapp.photogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nerminturkovic.flickrtestapp.R;
import com.nerminturkovic.flickrtestapp.data.PhotosRepository;

/**
 * Created by nerko on 26/11/16.
 */

public class PhotoGalleryActivity extends AppCompatActivity {

    public static final String PHOTO_ID = "photo_id";

    private PhotoGalleryContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_main_layout);

        Bundle bundle = getIntent().getExtras();
        String photoId = bundle.getString(PHOTO_ID);


        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putString(PHOTO_ID, photoId);

        PhotoGalleryFragment fragment = PhotoGalleryFragment.newInstance();
        fragment.setArguments(fragmentBundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();

        presenter = new PhotoGalleryPresenter(PhotosRepository.getInstance(getApplicationContext()), fragment);
    }
}
