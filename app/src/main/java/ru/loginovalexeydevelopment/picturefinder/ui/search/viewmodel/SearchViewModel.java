package ru.loginovalexeydevelopment.picturefinder.ui.search.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import ru.loginovalexeydevelopment.picturefinder.app.App;
import ru.loginovalexeydevelopment.picturefinder.model.Model;
import ru.loginovalexeydevelopment.picturefinder.model.entities.SearchOptions;

/**
 * ViewModel для работы с параметрами поиска
 */

public class SearchViewModel extends ViewModel {

    @Inject
    Model model;

    @Inject
    SharedPreferencesHandler sharedPreferencesHandler;

    public final MutableLiveData<SearchOptions> searchOptionsState;

    public SearchViewModel ( ) {
        App.getAppComponent ( ).inject ( this );
        searchOptionsState = new MutableLiveData<> ( );
    }

    //Передача параметров поиска во фрагмент для соответствующего отображения визуальных элементов
    public void onViewCreated ( ) {
        searchOptionsState.setValue ( model.getSearchOptions ( ) );
    }

    //Сохранение параметров поиска
    public void actionSearch ( SearchOptions searchOptions ) {
        model.setSearchOptions ( searchOptions );
        sharedPreferencesHandler.saveSearchOptionsToPref ( model.getSearchOptions ( ) );
    }
}