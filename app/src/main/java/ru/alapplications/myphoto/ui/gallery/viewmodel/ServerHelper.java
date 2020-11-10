package ru.alapplications.myphoto.ui.gallery.viewmodel;

import android.util.Log;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.entities.ServerResponse;
import ru.alapplications.myphoto.ui.gallery.viewmodel.retrofit.ApiBuilder;

public class ServerHelper {

    @Inject
    Model model;

    @Inject
    App app;

    public ServerHelper ( ) {

        App.getAppComponent ( ).inject ( this );
    }

    public Single<ServerResponse> paramsRequest ( int nextPageNumber , int pageSize ) {

        return new ApiBuilder ( )
                .requestServerSingleData (
                        model.getSearchOptions ().getQuery ( ) ,
                        Locale.getDefault ( ).getLanguage ( ) ,
                        getImageTypeValue ( ) ,
                        getOrientationValue ( ) ,
                        getCategoryValue ( ) ,
                        getColorValues ( ) ,
                        model.getSearchOptions().getEditorChoice ( ) ,
                        model.getSearchOptions ().getSafeSearchChoice ( ) ,
                        getOrderValue ( ) ,
                        nextPageNumber,
                        pageSize );
    }

    public String getImageTypeValue ( ) {
        if ( model.getSearchOptions().getImageTypeChoice() ) {
            return app.getResources ( )
                    .getStringArray ( R.array.imageType )[model.getSearchOptions().getImageTypeIndex()];
        } else return "";
    }

    public String getOrientationValue ( ) {
        if ( model.getSearchOptions().getOrientationChoice() ) {
            return app.getResources ( ).getStringArray ( R.array.orientation )[model.getSearchOptions ().getOrientationIndex()];

        } else return "";
    }

    public String getCategoryValue ( ) {
        if ( model.getSearchOptions ().getCategoryChoice() ) {
            return app.getResources ( )
                    .getStringArray ( R.array.category )[model.getSearchOptions ().getCategoryIndex()];

        } else return "";
    }

    public String getColorValues ( ) {
        String result = "";
        if ( model.getSearchOptions ().getColorsChoice() ) {
            for (int i = 0; i < model.getSearchOptions ().getColorsChecks().length; i++) {
                if ( model.getSearchOptions ().getColorsChecks()[i] ) {
                    if ( result == "" ) {
                        result = app.getResources ( ).getStringArray ( R.array.color )[i];
                    } else {
                        result = result + "," + app.getResources ( ).getStringArray ( R.array.color )[i];
                    }
                }
            }
        }
        return result;
    }

    public String getOrderValue ( ) {
        if ( model.getSearchOptions ().getOrderChoice() ) {
            return app.getResources ( )
                    .getStringArray ( R.array.order )[model.getSearchOptions ().getOrderIndex()];

        } else return "";
    }

    public void request ( int position , int pageSize , OnLoadDataListener onLoadDataListener ) {
        int nextPage = position / pageSize + 1;
        Log.d ( App.TAG , "Load from server. Position = " + position + " nextPage= " + nextPage );
        paramsRequest ( nextPage , pageSize )
                .observeOn ( Schedulers.io ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        serverResponse -> {
                            onLoadDataListener.getLoadResult ( serverResponse );
                        } ,
                        throwable -> {onLoadDataListener.getLoadResult ( null );
                        });
    }

}
