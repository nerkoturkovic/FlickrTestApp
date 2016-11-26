package com.nerminturkovic.flickrtestapp.photoslist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerminturkovic.flickrtestapp.R;
import com.nerminturkovic.flickrtestapp.data.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nerko on 23/11/16.
 */

public class PhotosListAdapter extends RecyclerView.Adapter<PhotosListAdapter.PhotoListViewHolder> {

    private List<Photo> photos = new ArrayList<>();
    private Context context;
    private PhotosListFragment.OnPhotoSelected photoSelectedCallback;

    public PhotosListAdapter(Context context, PhotosListFragment.OnPhotoSelected photoSelectedCallback) {
        this.context = context;
        this.photoSelectedCallback = photoSelectedCallback;
    }

    @Override
    public PhotoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_list_item, parent, false);
        return new PhotoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoListViewHolder holder, int position) {
        final Photo photo = photos.get(position);
        holder.titleTextView.setText(photo.getTitle());
        Picasso.
                with(context)
                .load(photo.getSizes().get(0).getSource())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoSelectedCallback.photoSelected(Long.toString(photo.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        notifyItemInserted(photos.size());
    }

    static class PhotoListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo_image_view)
        ImageView imageView;
        @BindView(R.id.photo_title)
        TextView titleTextView;

        PhotoListViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }
}
