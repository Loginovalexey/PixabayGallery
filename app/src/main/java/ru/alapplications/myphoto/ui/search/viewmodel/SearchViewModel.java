package ru.alapplications.myphoto.ui.search.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.entities.SearchOptions;

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