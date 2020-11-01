package ru.alapplications.myphoto.ui.galleryFragment.presenter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Pair;

import androidx.room.Room;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.db.MyPhotoDao;
import ru.alapplications.myphoto.db.MyPhotoDatabase;
import ru.alapplications.myphoto.model.entities.Hit;

public class CacheHelper {

    private static DateFormat dateFormat;
    private static boolean    cacheBeenLoaded;
    private static boolean needSaveCache;
    static MyPhotoDao myPhotoDao;

    static {
        MyPhotoDatabase db = Room.databaseBuilder (
                App.getInstance ( ).getApplicationContext ( ) ,
                MyPhotoDatabase.class ,
                "myPhotoDatabase.db" )
                .build ( );
        myPhotoDao = db.getMyPhotoDao ( );
        dateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );
        cacheBeenLoaded = false;
        needSaveCache = false;
    }

    public static boolean isNeedSaveCache ( ) {
        return needSaveCache;
    }

    public static void setNeedSaveCache ( boolean needSaveCache ) {
        CacheHelper.needSaveCache = needSaveCache;
    }

    public static boolean isCacheBeenLoaded ( ) {
        return cacheBeenLoaded;
    }

    public static void setCacheBeenLoaded ( boolean cacheBeenLoaded ) {
        CacheHelper.cacheBeenLoaded = cacheBeenLoaded;
    }

    @SuppressLint("CheckResult")
    public static void saveCache ( List<Hit> hits , int totalHits ) {
        TotalInfo totalInfo = new TotalInfo ( totalHits );
        if ( hits.size ( ) > 0 ) {
            Single.create ( emitter -> {
                emitter.onSuccess (
                        myPhotoDao.clearDbTableAndCacheHits ( hits , totalInfo ) );
            } )
                    .observeOn ( Schedulers.io ( ) )
                    .subscribeOn ( Schedulers.io ( ) )
                    .subscribe (
                            resultList -> {
                                Log.d ( App.TAG , "Cache Ok, size:" + resultList.toString ( ) );
                            } ,
                            throwable -> {
                            } );
        }

    }

    @SuppressLint("CheckResult")
    public static void loadCache ( OnLoadCacheListener onLoadCacheListener ) {
        Single.create ( emitter -> emitter.onSuccess (
                myPhotoDao.getTotalAndHits ( ) ) )
                .observeOn ( AndroidSchedulers.mainThread ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        result -> {
                            List<Hit> hits = ( List<Hit> ) (( Pair ) result).first;
                            TotalInfo totalInfo = ( TotalInfo ) (( Pair ) result).second;
                            if ( !hits.isEmpty ( ) && totalInfo.totalHits > 0 && (totalInfo.date).equals ( dateFormat.format ( new Date ( ) ) ) )
                                onLoadCacheListener.getResult ( true , hits , totalInfo.totalHits );
                            else
                                onLoadCacheListener.getResult ( false , null , null );
                            Log.d ( App.TAG , result.toString ( ) );


                        } ,
                        throwable -> {
                            Log.e ( App.TAG , "Cache Error" + throwable );
                            onLoadCacheListener.getResult ( false , null , null );
                        } );
    }


    public static void clearCache ( OnLoadDataListener onLoadDataListener ) {

        Single.create ( emitter -> emitter.onSuccess (
                myPhotoDao.clearTotalAndHits ( ) ) )
                .observeOn ( AndroidSchedulers.mainThread ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        result -> {
                            Log.d ( App.TAG , "Cache deleted" );
                            onLoadDataListener.getLoadResult ( null );
                        } ,
                        throwable -> {
                            Log.e ( App.TAG , "Cache Error" + throwable );
                            onLoadDataListener.getLoadResult ( null );
                        } );
    }
}