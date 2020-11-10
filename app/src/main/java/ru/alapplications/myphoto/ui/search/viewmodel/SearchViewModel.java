package ru.alapplications.myphoto.ui.search.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.entities.SearchOptions;

public class SearchViewModel extends ViewModel{

    public static final String SEARCH_OPTIONS_SHARED_PREFERENCES_FILE_NAME = "searchOptions.pref";
    public static final String SEARCH_OPTIONS_KEY = "searchOptions";

    @Inject
    Model model;

    @Inject
    App app;

    public MutableLiveData<SearchOptions> searchOptionsState;

    public SearchViewModel ( ) {
        App.getAppComponent ( ).inject ( this );
        searchOptionsState = new MutableLiveData<> ( model.getSearchOptions () );
    }

    public void actionSearch ( SearchOptions searchOptions ) {
        model.setSearchOptions ( searchOptions );
        saveToPref ();
        model.setNeedReload ( true );
    }

    private void saveToPref ( ) {
        SharedPreferences sharedPreferences = app.getSharedPreferences ( SEARCH_OPTIONS_SHARED_PREFERENCES_FILE_NAME , Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit ( );
        String json = new Gson ( ).toJson ( model.getSearchOptions () );
        editor.putString ( SEARCH_OPTIONS_KEY , json ).apply ( );
        editor.apply ( );
    }
}