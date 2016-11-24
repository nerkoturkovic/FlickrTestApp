package com.nerminturkovic.flickrtestapp.data.remote.network;

import com.nerminturkovic.flickrtestapp.data.remote.network.model.PhotoRemote;
import com.nerminturkovic.flickrtestapp.data.remote.network.model.PhotosContainer;
import com.nerminturkovic.flickrtestapp.data.remote.network.model.SizesContainer;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nerko on 21/11/16.
 */

public interface PhotosService {

    @GET("services/rest/?method=flickr.photos.search&text=football")
    Observable<PhotosContainer> getPhotos(@Query("page") int page, @Query("per_page") int itemsPerPage);

    @GET("services/rest/?method=flickr.photos.getInfo")
    Observable<PhotoRemote> getPhoto(@Query("photo_id") String photoId);

    @GET("services/rest/?method=flickr.photos.getSizes")
    Observable<SizesContainer> getSizes(@Query("photo_id") String photoId);
}
