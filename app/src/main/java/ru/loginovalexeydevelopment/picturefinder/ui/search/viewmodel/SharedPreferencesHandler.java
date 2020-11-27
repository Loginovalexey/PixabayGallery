package ru.loginovalexeydevelopment.picturefinder.ui.search.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;

import ru.loginovalexeydevelopment.picturefinder.app.App;
import ru.loginovalexeydevelopment.picturefinder.model.entities.SearchOptions;

public class SharedPreferencesHandler {

    private static final String SEARCH_OPTIONS_SHARED_PREFERENCES_FILE_NAME = "searchOptions.pref";
    private static final String SEARCH_OPTIONS_KEY                          = "searchOptions";

    @Inject
    App app;

    public SharedPreferencesHandler ( ) {
        App.getAppComponent ( ).inject ( this );
    }


    /**
     * Запись параметров поиска в SharedPreferences
     *
     * @param searchOptions набор параметров
     */
    public void saveSearchOptionsToPref ( SearchOptions searchOptions ) {
        SharedPreferences sharedPreferences = app.getSharedPreferences (
                SEARCH_OPTIONS_SHARED_PREFERENCES_FILE_NAME ,
                Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit ( );
        String json = new Gson ( ).toJson ( searchOptions );
        editor.putString ( SEARCH_OPTIONS_KEY , json ).apply ( );
        editor.apply ( );
    }

    /**
     * Загрузка параметров поиска из SharedPreferences
     *
     * @return набор параметров
     */
    public SearchOptions loadSearchOptionsFromPref ( ) {
        SharedPreferences sharedPreferences = app.getSharedPreferences (
                SEARCH_OPTIONS_SHARED_PREFERENCES_FILE_NAME ,
                Context.MODE_PRIVATE
        );
        String json = sharedPreferences.getString ( SEARCH_OPTIONS_KEY , null );
        return new Gson ( ).fromJson ( json , SearchOptions.class );
    }

}
