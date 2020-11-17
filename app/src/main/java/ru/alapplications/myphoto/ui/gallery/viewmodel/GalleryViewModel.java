package ru.alapplications.myphoto.ui.gallery.viewmodel;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executors;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.model.entities.SearchOptions;
import ru.alapplications.myphoto.ui.search.viewmodel.SharedPreferencesHandler;


/**
 * ViewModel для экрана с галереей
 */
public class GalleryViewModel extends ViewModel {

    //Размер подгружаемых страниц
    private static final int PAGE_SIZE = 20;

    /**
     * Список загруженных миниатюр с поддержкой пагинации
     */
    public LiveData<PagedList<Hit>>      pagedList;
    /**
     * Колбэк для отслеживания статуса загрузки
     */
    public IOnLoadResult                 onLoadResult;
    /**
     * Передатчик статуса загрузок
     */
    public MutableLiveData<LoadingState> loadingState;
    /**
     * Передатчик для строки поиска
     */
    public MutableLiveData<String>       searchQuery;
    /**
     * Передатчик, сигнализирующий о новых данных
     */
    public MutableLiveData<Boolean>      isNewDataSet;

    @Inject
    App app;

    @Inject
    Model model;

    @Inject
    CacheHandler cacheHandler;

    @Inject
    SharedPreferencesHandler sharedPreferencesHandler;


    @SuppressLint("CheckResult")
    public GalleryViewModel ( ) {
        App.getAppComponent ( ).inject ( this );
        initSearchOptionsIfNotExist ( );
        createLiveDataObjects ( );
        createLoadingStateCallBack ( );
        createPagedList ( );
    }

    //Функция запускается в самом начале работы приложения для определения параметров поиска
    private void initSearchOptionsIfNotExist ( ) {
        if ( model.getSearchOptions ( ) == null ) initSearchOptions ( );
    }

    private void initSearchOptions ( ) {
        try {
            //Попытка чтения записанных в SharedPreferences  параметров поиска
            model.setSearchOptions (sharedPreferencesHandler.loadSearchOptionsFromPref ());
            //При неудаче чтения - сброс настроек поиска
            if ( model.getSearchOptions ( ) == null )
                model.setSearchOptions ( new SearchOptions ( ) );
        } catch (Exception e) {
            model.setSearchOptions ( new SearchOptions ( ) );
        }
    }

    private void createLiveDataObjects ( ) {
        loadingState = new MutableLiveData<> ( );
        isNewDataSet = new MutableLiveData<> ( );
        searchQuery = new MutableLiveData<> ( );
    }

    private void createLoadingStateCallBack ( ) {
        onLoadResult = result -> loadingState.postValue ( result );
    }

    //Создание списка данных с поддержкой пагинации
    private void createPagedList ( ) {
        //В фабрику для источника данных передается колбэк для получения статусов загрузки
        HitsDataSourceFactory sourceFactory = new HitsDataSourceFactory ( onLoadResult );
        PagedList.Config config = new PagedList.Config.Builder ( )
                .setPageSize ( PAGE_SIZE )
                .setEnablePlaceholders ( true )
                .build ( );
        pagedList = new LivePagedListBuilder<> ( sourceFactory , config )
                .setFetchExecutor ( Executors.newSingleThreadExecutor ( ) )
                .build ( );
    }

    public void onViewCreated ( ) {
        //Галерея может быть выведена на экран после задания новых параметров поиска,
        //тогда необходимо сбросить все старые данные
        resetAllIfNecessary ( );
        //Передача поискового запроса
        searchQuery.setValue (
                model.getSearchOptions ( ).getQuery ( ) );
    }

    private void resetAllIfNecessary ( ) {

        if ( model.isNeedReload ( ) ) {
            //Сброс всех данных в модели
            model.reset ( );
            //Очистка кэша
            cacheHandler.clearCache ( );
            //Создание списка для новых данных
            createPagedList ( );
            //Сигнализирование о новом наборе данных
            isNewDataSet.setValue ( true );
            //Сигнализирование об ожидании данных
            loadingState.setValue ( LoadingState.WAITING );
        }
    }


    /**
     * Перезагрузка первой страницы
     */
    public void firstReload ( ) {
        loadingState.setValue ( LoadingState.WAITING );
        //Создать новый лист с пагинацией, поскольку старый - пустой и не может быть прокручен
        createPagedList ( );
    }

    /**
     * Перезагрузка очередной страницы
     */
    public void reload ( ) {
        //Сохранение уже имеющихся данных
        saveData ( );
        //Возобновление отслеживания скроллинга
        pagedList.getValue ( ).getDataSource ( ).invalidate ( );
    }


    /**
     * Сохранение данных в модель и кэширование последних неучтенных записей
     */
    public void saveData ( ) {
        if ( !pagedList.getValue ( ).isEmpty ( ) ) {
            copyDataToModel ( );
        }
    }

    private void copyDataToModel ( ) {
        model.setHits ( pagedList.getValue ( ), pagedList.getValue ( ).getLoadedCount ( ) );
    }

    //Сохранение в модель индекса выбранной в галерее картинки
    public void setCurrentIndex ( int position ) {
        copyDataToModel ();
        model.setCurrentIndex ( position );
    }


    //Кэширование загруженных данных при удалении активити
    @Override
    protected void onCleared ( ) {
        super.onCleared ( );
        copyDataToModel ();
        if (model.isNeedSaveCache ( )) saveCache ( );
    }

    private void saveCache ( ) {
        cacheHandler.saveCache ( model.getLoadedHits () ,
                model.getHitsSize() );
    }
}