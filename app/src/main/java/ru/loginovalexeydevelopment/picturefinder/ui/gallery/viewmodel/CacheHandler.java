package ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel;

import android.annotation.SuppressLint;
import android.util.Pair;

import androidx.room.Room;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.loginovalexeydevelopment.picturefinder.app.App;
import ru.loginovalexeydevelopment.picturefinder.db.CacheDao;
import ru.loginovalexeydevelopment.picturefinder.db.CacheDatabase;
import ru.loginovalexeydevelopment.picturefinder.model.entities.Hit;
import ru.loginovalexeydevelopment.picturefinder.model.entities.TotalInfo;

/**
 * Класс, отвечающий за кэширование в базу данных
 */

public class CacheHandler {

    @Inject
    App app;

    private final static String     DATABASE_NAME = "cacheDb.db";
    private final        CacheDao   cacheDao;
    private final        DateFormat dateFormat;


    @SuppressLint("SimpleDateFormat")
    public CacheHandler ( ) {

        App.getAppComponent ( ).inject ( this );

        CacheDatabase db = createDb ( );
        cacheDao = db.getCacheDao ( );
        dateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );
    }

    @NotNull
    private CacheDatabase createDb ( ) {
        return Room.databaseBuilder (
                app.getApplicationContext ( ) ,
                CacheDatabase.class ,
                DATABASE_NAME )
                .build ( );
    }

    /**
     * Кэширование данных
     *
     * @param hits          - массив готовых(загруженных) данных
     * @param totalHitsSize - общий размер массива данных и дата создания
     */
    @SuppressLint("CheckResult")
    public void saveCache ( List<Hit> hits , int totalHitsSize ) {
        if ( !hits.isEmpty ( ) ) {
            //Создание объекта для записи в базу данных общего размера массива
            TotalInfo totalInfo = new TotalInfo ( totalHitsSize );
            saveToDb ( hits , totalInfo );
        }
    }

    /**
     * Фоновое кэширование в базу данных
     */
    private void saveToDb ( List<Hit> hits , TotalInfo totalInfo ) {
        Single.create ( emitter ->
                emitter.onSuccess ( cacheDao.saveCache ( hits , totalInfo ) ) )
                .observeOn ( Schedulers.io ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe ( );
    }

    /**
     * @param onCacheLoadListener колбэк вызывается по завершении загрузки данных из кэша
     */
    @SuppressLint("CheckResult")
    public void loadCache ( OnCacheLoadListener onCacheLoadListener ) {
        Single.create ( emitter -> emitter.onSuccess ( cacheDao.loadCache ( ) ) )
                .observeOn ( AndroidSchedulers.mainThread ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        cache -> checkCache ( onCacheLoadListener , cache ) ,
                        throwable -> returnNoCache ( onCacheLoadListener ) );
    }

    private void returnNoCache ( OnCacheLoadListener onCacheLoadListener ) {
        onCacheLoadListener.getResult ( false , null , null );
    }

    private void checkCache ( OnCacheLoadListener onCacheLoadListener , Object cache ) {
        //Первый параметр кэша - загруженные данные
        List<Hit> hits = ( List<Hit> ) (( Pair ) cache).first;
        //Второй параметр - общий размер массива
        TotalInfo totalInfo = ( TotalInfo ) (( Pair ) cache).second;
        //Проверка, существет ли кэш
        if ( isCacheExist ( hits , totalInfo ) &&
        //Проверка - не устарел ли кэш
                isCacheFresh ( totalInfo ) )
            //Вызов колбэка с кэшем
            returnCacheOk ( onCacheLoadListener , hits , totalInfo );
        else {
            //Вызов колбэка без кэша
            returnNoCache ( onCacheLoadListener );
            //Удаление устаревшего кэша
            if ( totalInfo != null && !isCacheFresh ( totalInfo ) )
                clearCache ( );
        }
    }

    private void returnCacheOk ( OnCacheLoadListener onCacheLoadListener ,
                                 List<Hit> hits ,
                                 TotalInfo totalInfo ) {
        onCacheLoadListener.getResult ( true , hits , totalInfo.getTotalHits () );
    }

    private boolean isCacheFresh ( TotalInfo totalInfo ) {
        return (totalInfo.getTotalHits ()).equals ( dateFormat.format ( new Date ( ) ) );
    }

    private boolean isCacheExist ( List<Hit> hits , TotalInfo totalInfo ) {
        return !hits.isEmpty ( ) && totalInfo.getTotalHits () > 0;
    }

    //Очистка кэша
    public void clearCache ( ) {
        Single.create ( emitter -> emitter.onSuccess (
                cacheDao.clearCache ( ) ) )
                .observeOn ( AndroidSchedulers.mainThread ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe ();
    }
}