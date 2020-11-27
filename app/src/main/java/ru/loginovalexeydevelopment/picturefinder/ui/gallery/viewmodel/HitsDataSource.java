package ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.List;

import javax.inject.Inject;

import ru.loginovalexeydevelopment.picturefinder.app.App;
import ru.loginovalexeydevelopment.picturefinder.model.Model;
import ru.loginovalexeydevelopment.picturefinder.model.entities.Hit;

public class HitsDataSource extends PositionalDataSource<Hit> {

    private final IOnLoadResult onLoadResult;

    @Inject
    ServerHandler serverHandler;

    @Inject
    CacheHandler cacheHandler;

    @Inject
    Model model;

    public HitsDataSource ( IOnLoadResult onLoadResult ) {
        App.getAppComponent ( ).inject ( this );
        this.onLoadResult = onLoadResult;
    }

    //Загрузка первой страницы
    @Override
    public void loadInitial ( @NonNull LoadInitialParams params ,
                              @NonNull LoadInitialCallback<Hit> callback ) {
        //Первый источник данных для начальной загрузки - модель.
        if ( model.getLoadedHits ( ) != null ) initFromModel ( callback );
        else
            //Второй источник данных - кэш
            cacheHandler.loadCache ( ( cacheIsExist , hits , totalHits ) -> {
                if ( cacheIsExist )
                    initFromCache ( callback , hits , totalHits );
                else
                    //В последнюю очередь данные загружаются с веб-сервера
                    initFromServer ( params , callback );
            } );
    }

    private void initFromServer ( @NonNull LoadInitialParams params ,
                                  @NonNull LoadInitialCallback<Hit> callback ) {
        //Запрос к серверу и проверка результата
        serverHandler.request ( 0 , params.pageSize , serverResponse -> {
                    if ( serverResponse == null )
                        onLoadResult.callOnLoadResult ( LoadingState.FIRST_LOAD_ERROR );
                    else if ( serverResponse.getHits ( ).isEmpty ( ) )
                        onLoadResult.callOnLoadResult ( LoadingState.EMPTY_DATA );
                    else {
                        //Новые данные получены, значит потребуется обновить кэш
                        model.setNeedSaveCache ( true );
                        onLoadResult.callOnLoadResult ( LoadingState.OK );
                        callback.onResult ( serverResponse.getHits ( ) , 0 ,
                                serverResponse.getTotalHits ( ) );
                    }
                }
        );
    }

    private void initFromCache ( @NonNull LoadInitialCallback<Hit> callback ,
                                 List<Hit> hits ,
                                 Integer totalHits ) {
        model.setNeedSaveCache ( false );
        callback.onResult ( hits , 0 , totalHits );
        onLoadResult.callOnLoadResult ( LoadingState.OK );
    }

    private void initFromModel ( @NonNull LoadInitialCallback<Hit> callback ) {
        callback.onResult ( model.getLoadedHits (),
                0 , model.getHitsSize ( ) );
        model.setNeedSaveCache ( false );
    }

    //Загрузка новой страницы
    @Override
    public void loadRange ( @NonNull LoadRangeParams
                                    params , @NonNull LoadRangeCallback<Hit> callback ) {
        serverHandler.request ( params.startPosition , params.loadSize , serverResponse -> {
            //Анализ ответа от сервера
            if ( serverResponse != null ) {
                //Получены новые данные, которые нужно будет закэшировать
                model.setNeedSaveCache ( true );
                callback.onResult ( serverResponse.getHits ( ) );
                onLoadResult.callOnLoadResult ( LoadingState.OK );
            } else {
                onLoadResult.callOnLoadResult ( LoadingState.LOAD_ERROR );
            }
        } );
    }
}