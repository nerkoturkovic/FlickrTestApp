package com.nerminturkovic.flickrtestapp.photoslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nerminturkovic.flickrtestapp.R;
import com.nerminturkovic.flickrtestapp.data.PhotosRepository;

public class PhotosListActivity extends AppCompatActivity {

    private PhotosListContract.Presenter presenter;

    public PhotosListActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PhotosListFragment fragment = (PhotosListFragment) getSupportFragmentManager().findFragmentById(R.id.main_content);

        if (fragment == null) {
            fragment = PhotosListFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_content, fragment)
                    .commit();
        }

        presenter = new PhotosListPresenter(PhotosRepository.getInstance(getApplicationContext()), fragment);
    }
}
