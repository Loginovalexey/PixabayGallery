package ru.alapplications.myphoto.ui.galleryFragment.presenter;

import android.util.Log;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.SearchOptions;
import ru.alapplications.myphoto.model.entities.ServerResponse;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.retrofit.ApiBuilder;

public class ServerHelper {

    @Inject
    SearchOptions searchOptions;
    public ServerHelper ( ) {

        App.getAppComponent ( ).inject ( this );
    }

    public Single<ServerResponse> paramsRequest ( int nextPageNumber , int pageSize ) {

        return new ApiBuilder ( )
                .requestServerSingleData (
                        searchOptions.getQuery ( ) ,
                        Locale.getDefault ( ).getLanguage ( ) ,
                        searchOptions.getImageTypeValue ( ) ,
                        searchOptions.getOrientationValue ( ) ,
                        searchOptions.getCategoryValue ( ) ,
                        searchOptions.getColorValues ( ) ,
                        searchOptions.getEditorChoice ( ) ,
                        searchOptions.getSafeSearchChoice ( ) ,
                        searchOptions.getOrderValue ( ) ,
                        nextPageNumber ,
                        pageSize );
    }

    public void request ( int position , int pageSize , OnLoadDataListener onLoadDataListener ) {
        int nextPage = position / pageSize + 1;
        Log.d ( App.TAG , "Load from server. Position = " + position + " nextPage= " + nextPage );
        paramsRequest ( nextPage , pageSize )
                .observeOn ( Schedulers.io ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        serverResponse -> onLoadDataListener.getLoadResult ( serverResponse )  ,
                        throwable -> onLoadDataListener.getLoadResult ( null ) );
    }

}
