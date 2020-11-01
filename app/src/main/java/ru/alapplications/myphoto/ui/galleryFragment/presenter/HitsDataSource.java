package ru.alapplications.myphoto.ui.galleryFragment.presenter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.entities.Hit;

public class HitsDataSource extends PositionalDataSource<Hit> {

    @Inject
    ServerHelper serverHelper;


    @Inject
    Model model;

    public HitsDataSource ( ) {
        App.getAppComponent ( ).inject ( this );
    }

    @Override
    public void loadInitial ( @NonNull LoadInitialParams params , @NonNull LoadInitialCallback<Hit> callback ) {
        if ( model.getHits ( ) != null  ) {
            callback.onResult (  model.getHits ().subList ( 0,model.getLoadedCount () ), 0 , model.getHits ().size () );
            Log.d ( App.TAG , "init from model" );
        } else if ( !CacheHelper.isCacheBeenLoaded ( ) )
            CacheHelper.loadCache ( ( isExist , hits , totalHits ) -> {
                if ( isExist ) {
                    CacheHelper.setCacheBeenLoaded ( true );
                    Log.d ( App.TAG , "init from cache" );
                    CacheHelper.setNeedSaveCache (false);
                    callback.onResult ( hits , 0 , totalHits );
                } else
                    serverHelper.request ( 0 , params.pageSize , serverResponse -> {
                                if ( (serverResponse != null) && (serverResponse.getHits ( ) != null) ) {
                                    Log.d ( App.TAG , "init from server" );
                                    CacheHelper.setNeedSaveCache (true);
                                    callback.onResult ( serverResponse.getHits ( ) , 0 , serverResponse.getTotalHits ( ) );
                                }
                            }
                    );
            } );
        else
            serverHelper.request ( 0 , params.pageSize , serverResponse -> {
                        if ( (serverResponse != null) && (serverResponse.getHits ( ) != null) ) {
                            Log.d ( App.TAG , "init from server" );
                            CacheHelper.setNeedSaveCache ( true );
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
                callback.onResult ( serverResponse.getHits ( ) );
            }
        } );
    }
}