package ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.loginovalexeydevelopment.picturefinder.model.entities.ServerResponse;

/**
 * Класс для обращения к веб-серверу с использованием Retrofit
 */

public class ApiBuilder {

    private static final String BASE_URL = "https://pixabay.com/api/";

    /**
     * Асинхронное получение json-скрипта от веб-верера с наобором данных
     * согласно заданным параметрам
     */
    public Single<ServerResponse> serverRequest ( String q ,
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
        Gson gson = createGson ( );
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create ( gson );
        ApiGetter api = createApi ( gsonConverterFactory );
        return api.getJson (
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

    @NotNull
    private ApiGetter createApi ( GsonConverterFactory gsonConverterFactory ) {
        return new Retrofit.Builder ( )
                .baseUrl ( BASE_URL )
                .addCallAdapterFactory ( RxJava2CallAdapterFactory.create ( ) )
                .addConverterFactory ( gsonConverterFactory )
                .build ( )
                .create ( ApiGetter.class );
    }

    @NotNull
    private Gson createGson ( ) {
        return new GsonBuilder ( )
                    .excludeFieldsWithoutExposeAnnotation ( )
                    .create ( );
    }
}
