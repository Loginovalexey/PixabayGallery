package ru.alapplications.myphoto.ui.gallery.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.gson.Gson;

import java.util.concurrent.Executors;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.model.entities.SearchOptions;
import ru.alapplications.myphoto.ui.search.viewmodel.SearchViewModel;


public class GalleryViewModel extends ViewModel {

    public  LiveData<PagedList<Hit>>      pagedListLiveData;
    private MutableLiveData<Integer>      currentIndex;
    public  IOnLoadResult                 onLoadResult;
    public  MutableLiveData<LoadingState> loadingState;
    public  MutableLiveData<String>       message;
    public  MutableLiveData<String>       searchQuery;
    public  MutableLiveData<Boolean>      isNewDataSet;

    @Inject
    App app;

    @Inject
    Model model;


    @SuppressLint("CheckResult")
    public GalleryViewModel ( ) {
        App.getAppComponent ( ).inject ( this );
        if ( model.getSearchOptions ( ) == null ) initSearchOptions ( );
        Log.d ( App.TAG , "ViewModel created" );
        currentIndex = new MutableLiveData<> ( 0 );
        message = new MutableLiveData<> ( );
        loadingState = new MutableLiveData<> ( );
        isNewDataSet = new MutableLiveData<> ( );
        searchQuery = new MutableLiveData<> ( );
        onLoadResult = new IOnLoadResult ( ) {
            @Override
            public void callOnLoadResult ( LoadingState result ) {
                loadingState.postValue ( result );
                Log.d ( App.TAG , "GalleryViewModel:" + result.toString ( ) );
            }
        };
        initDataSource ( );
    }

    private void initDataSource ( ) {
        HitsDataSourceFactory sourceFactory = new HitsDataSourceFactory ( onLoadResult );
        PagedList.Config config = new PagedList.Config.Builder ( )
                .setPageSize ( 20 )
                .setEnablePlaceholders ( true )
                .build ( );
        pagedListLiveData = new LivePagedListBuilder<> ( sourceFactory , config )
                .setFetchExecutor ( Executors.newSingleThreadExecutor ( ) )
                .build ( );
    }


    public void onViewCreated ( ) {
        if ( model.isNeedReload ( ) ) {
            model.reset ( );
            CacheHelper.setNeedSaveCache ( false );
            CacheHelper.clearCache ( );
            initDataSource ( );
            isNewDataSet.setValue ( true );
            loadingState.setValue ( LoadingState.OK );
        }
        searchQuery.setValue ( model.getSearchOptions ( ).getQuery ( ) );


    }

    private void initSearchOptions ( ) {
        try {
            SharedPreferences sharedPreferences = app.getSharedPreferences ( SearchViewModel.SEARCH_OPTIONS_SHARED_PREFERENCES_FILE_NAME , Context.MODE_PRIVATE );
            String json = sharedPreferences.getString ( SearchViewModel.SEARCH_OPTIONS_KEY , null );
            model.setSearchOptions ( new Gson ( ).fromJson ( json , SearchOptions.class ) );
            if ( model.getSearchOptions ( ) == null )
                model.setSearchOptions ( new SearchOptions ( ) );
        } catch (Exception e) {
            model.setSearchOptions ( new SearchOptions ( ) );
        }
    }

    public void saveData ( ) {
        //adapter.onDestroy ( );
        if ( !pagedListLiveData.getValue ( ).isEmpty ( ) ) {
            model.setHits ( pagedListLiveData.getValue ( ) );
            model.setLoadedCount ( pagedListLiveData.getValue ( ).getLoadedCount ( ) );
            if ( CacheHelper.isNeedSaveCache ( ) &&
                    !model.getHits ( ).isEmpty ( ) &&
                    (!CacheHelper.prevLastIndex.equals ( model.getLoadedCount ( ) )) ) {
                CacheHelper.saveCache ( model.getHits ( ).subList (
                        CacheHelper.prevLastIndex , model.getLoadedCount ( ) ) ,
                        model.getHits ( ).size ( ) );
                CacheHelper.prevLastIndex = pagedListLiveData.getValue ( ).getLoadedCount ( );
            }
        }

    }

    public void reload ( ) {
        saveData ( );
//        initDataSource ();
        pagedListLiveData.getValue ( ).getDataSource ( ).invalidate ( );
    }

    public LiveData<Integer> getCurrentIndex ( ) {
        return currentIndex;
    }

    public void setCurrentIndex ( int position ) {
        currentIndex.setValue ( position );
    }

    public void firstReload ( ) {
        loadingState.setValue ( LoadingState.WAITING );
        initDataSource ( );
    }
}