package com.nerminturkovic.flickrtestapp.data.remote;

import android.util.Log;

import com.nerminturkovic.flickrtestapp.data.PhotosDataSource;
import com.nerminturkovic.flickrtestapp.data.model.PhotoSize;
import com.nerminturkovic.flickrtestapp.data.remote.network.PhotosService;
import com.nerminturkovic.flickrtestapp.data.remote.network.model.PhotoItem;
import com.nerminturkovic.flickrtestapp.data.remote.network.model.PhotoRemote;
import com.nerminturkovic.flickrtestapp.data.remote.network.model.PhotosContainer;
import com.nerminturkovic.flickrtestapp.data.remote.network.model.Size;
import com.nerminturkovic.flickrtestapp.data.remote.network.model.SizesContainer;
import com.nerminturkovic.flickrtestapp.data.transformer.NetworkToModelTransformer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by nerko on 21/11/16.
 */

public class PhotosRemoteDataSource implements PhotosDataSource {

    private PhotosService photosService;
    private NetworkToModelTransformer transformer;

    public PhotosRemoteDataSource(PhotosService photosService) {
        this.photosService = photosService;

        this.transformer = new NetworkToModelTransformer();
    }

    @Override
    public Observable<List<com.nerminturkovic.flickrtestapp.data.model.Photo>> getPhotos() {
        Observable<PhotosContainer> photoListObservable = photosService.getPhotos(1, 100);

        return photoListObservable
                .flatMapIterable(new Func1<PhotosContainer, Iterable<PhotoItem>>() {
                    @Override
                    public Iterable<PhotoItem> call(PhotosContainer photosContainer) {
                        return photosContainer.getPhotos().getPhoto();
                    }
                })
                .concatMap(new Func1<PhotoItem, Observable<com.nerminturkovic.flickrtestapp.data.model.Photo>>() {
                    @Override
                    public Observable<com.nerminturkovic.flickrtestapp.data.model.Photo> call(final PhotoItem photoItem) {
                        Observable<PhotoRemote> photoObservable = photosService.getPhoto(photoItem.getId());
                        Observable<SizesContainer> sizesContainerObservable = photosService.getSizes(photoItem.getId());

                        return Observable.zip(photoObservable, sizesContainerObservable, new Func2<PhotoRemote, SizesContainer, com.nerminturkovic.flickrtestapp.data.model.Photo>() {
                            @Override
                            public com.nerminturkovic.flickrtestapp.data.model.Photo call(PhotoRemote photoRemote, SizesContainer sizesContainer) {
                                com.nerminturkovic.flickrtestapp.data.model.Photo photo = transformer.photoNetworkToPhoto(photoRemote.getPhoto());
                                List<PhotoSize> sizes = new ArrayList<>();
                                for (Size size : sizesContainer.getSizes().getSize()) {
                                    sizes.add(transformer.sizeNetworkToPhotoSize(size));
                                }
                                photo.setSizes(sizes);
                                return photo;
                            }
                        });
                    }
                })
                .toList();
    }

    @Override
    public Observable<com.nerminturkovic.flickrtestapp.data.model.Photo> getPhoto(String photoId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void savePhoto(com.nerminturkovic.flickrtestapp.data.model.Photo photo) {
        throw new UnsupportedOperationException();
    }
}
