package com.nerminturkovic.flickrtestapp.data.remote;

import com.nerminturkovic.flickrtestapp.data.remote.network.PhotosService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nerko on 26/11/16.
 */

public class RemoteServices {

    private static RemoteServices instance;

    private PhotosService photosService;

    private RemoteServices() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", "4f0e57ddcbd61473176d4ae71cc9ac1d")
                                .addQueryParameter("format", "json")
                                .addQueryParameter("nojsoncallback", "?")
                                .build();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        photosService = retrofit.create(PhotosService.class);
    }

    public static RemoteServices getInstance() {
        if (instance == null) {
            instance = new RemoteServices();
        }

        return instance;
    }

    public PhotosService getPhotosService() {
        return photosService;
    }
}
