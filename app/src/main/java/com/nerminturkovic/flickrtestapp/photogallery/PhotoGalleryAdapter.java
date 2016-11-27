package com.nerminturkovic.flickrtestapp.photogallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nerminturkovic.flickrtestapp.R;
import com.nerminturkovic.flickrtestapp.data.model.Photo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nerko on 27/11/16.
 */

public class PhotoGalleryAdapter extends PagerAdapter {

    private Context context;
    private List<Photo> photos;

    public PhotoGalleryAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Photo photo = photos.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.photo_gallery_item, container, false);

        final ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.photo_progress_bar);
        final ImageView imageView = (ImageView) layout.findViewById(R.id.image_view);

        progressBar.setVisibility(View.VISIBLE);
        Picasso
                .with(context)
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

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
