package com.nerminturkovic.flickrtestapp.photoslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nerminturkovic.flickrtestapp.R;
import com.nerminturkovic.flickrtestapp.data.PhotosDataSource;
import com.nerminturkovic.flickrtestapp.data.PhotosRepository;
import com.nerminturkovic.flickrtestapp.data.local.PhotosLocalDataSource;
import com.nerminturkovic.flickrtestapp.data.local.database.PhotosDBHelper;
import com.nerminturkovic.flickrtestapp.data.remote.PhotosRemoteDataSource;
import com.nerminturkovic.flickrtestapp.data.remote.network.PhotosService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotosListActivity extends AppCompatActivity {

    private PhotosListContract.Presenter presenter;

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

        PhotosDataSource localDataSource = new PhotosLocalDataSource(new PhotosDBHelper(getApplicationContext()));



        //This whole block of Retrofit init should be moved to another part, I would probably use Dagger2.
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


        PhotosService photosService = retrofit.create(PhotosService.class);
        PhotosDataSource remoteDataSource = new PhotosRemoteDataSource(photosService);

        PhotosRepository repository = new PhotosRepository(localDataSource, remoteDataSource);

        presenter = new PhotosListPresenter(repository, fragment);
    }
}
