package ru.alapplications.myphoto.ui.gallery.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.ui.gallery.viewmodel.LoadingState;

public class HitsDataSource extends PositionalDataSource<Hit> {

    private IOnLoadResult onLoadResult;

    @Inject
    ServerHelper serverHelper;


    @Inject
    Model model;

    public HitsDataSource ( IOnLoadResult onLoadResult ) {

        App.getAppComponent ( ).inject ( this );
        this.onLoadResult = onLoadResult;
    }


    @Override
    public void loadInitial ( @NonNull LoadInitialParams params , @NonNull LoadInitialCallback<Hit> callback ) {
        if ( model.getHits ( ) != null ) {
            callback.onResult ( model.getHits ( ).subList ( 0 , model.getLoadedCount ( ) ) , 0 , model.getHits ( ).size ( ) );
            Log.d ( App.TAG , "init from model" );
        } else if ( !CacheHelper.isCacheBeenLoaded ( ) )
            CacheHelper.loadCache ( ( isExist , hits , totalHits ) -> {
                if ( isExist ) {
                    CacheHelper.setCacheBeenLoaded ( true );
                    Log.d ( App.TAG , "init from cache" );
                    CacheHelper.setNeedSaveCache ( false );
                    CacheHelper.prevLastIndex = hits.size ( );
                    callback.onResult ( hits , 0 , totalHits );
                    onLoadResult.callOnLoadResult ( LoadingState.OK);
                } else
                    serverHelper.request ( 0 , params.pageSize , serverResponse -> {
                                if ( serverResponse == null )
                                    onLoadResult.callOnLoadResult ( LoadingState.FIRST_LOAD_ERROR );
                                else if ( serverResponse.getHits ( ).isEmpty () )
                                    onLoadResult.callOnLoadResult ( LoadingState.NO_MORE_DATA );
                                else {
                                    Log.d ( App.TAG , "init from server" );
                                    CacheHelper.setNeedSaveCache ( true );
                                    onLoadResult.callOnLoadResult ( LoadingState.OK );
                                    callback.onResult ( serverResponse.getHits ( ) , 0 , serverResponse.getTotalHits ( ) );

                                }
                            }
                    );
            } );
        else
            serverHelper.request ( 0 , params.pageSize , serverResponse -> {
                        if ( serverResponse == null ) onLoadResult.callOnLoadResult ( LoadingState.FIRST_LOAD_ERROR );
                        else if ( serverResponse.getHits ( ).isEmpty ())
                            onLoadResult.callOnLoadResult ( LoadingState.NO_MORE_DATA );
                        else {
                            Log.d ( App.TAG , "init from server" );
                            CacheHelper.setNeedSaveCache ( true );
                            onLoadResult.callOnLoadResult ( LoadingState.OK );
                            callback.onResult ( serverResponse.getHits ( ) , 0 , serverResponse.getTotalHits ( ) );
                        }
                    }
            );
    }

    @Override
    public void loadRange ( @NonNull LoadRangeParams
                                    params , @NonNull LoadRangeCallback<Hit> callback ) {
        serverHelper.request ( params.startPosition , params.loadSize , serverResponse -> {
            if ( serverResponse != null ) {
                CacheHelper.setNeedSaveCache ( true );
                callback.onResult ( serverResponse.getHits ( ) );
                onLoadResult.callOnLoadResult ( LoadingState.OK );
            } else {
                onLoadResult.callOnLoadResult ( LoadingState.ERROR );
            }
        } );
    }
}