package ru.alapplications.myphoto.ui.gallery.viewmodel.retrofit;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.locks.ReentrantLock;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.alapplications.myphoto.model.entities.ServerResponse;


public class ApiBuilder {
    private final String BASE_URL = "https://pixabay.com/api/";

    public Single<ServerResponse> requestServerSingleData ( String q ,
                                                            String lang ,
                                                            String imageType ,
                                                            String orientation ,
                                                            String category ,
                                                            String colors ,
                                                            boolean editorsChoice ,
                                                            boolean safeSearch ,
                                                            String order ,
                                                            int page ,
                                                            int per_page ) {
        Gson gson = new GsonBuilder ( )
                .excludeFieldsWithoutExposeAnnotation ( )
                .create ( );

        OkHttpClient client = new OkHttpClient.Builder ( )
                .addNetworkInterceptor ( new StethoInterceptor ( ) )
                .build ( );

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create ( gson );
        ApiGetter api = new Retrofit.Builder ( )
                .baseUrl ( BASE_URL )
                .client ( client )
                .addCallAdapterFactory ( RxJava2CallAdapterFactory.create ( ) )
                .addConverterFactory ( gsonConverterFactory )
                .build ( )
                .create ( ApiGetter.class );
        return api.getJsonScript (
                q ,
                lang ,
                imageType ,
                orientation ,
                category ,
                colors ,
                editorsChoice ,
                safeSearch ,
                order ,
                page ,
                per_page );

    }
}
