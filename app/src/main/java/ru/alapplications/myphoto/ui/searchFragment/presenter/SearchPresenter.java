package ru.alapplications.myphoto.ui.searchFragment.presenter;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.SearchOptions;
import ru.alapplications.myphoto.ui.searchFragment.view.SearchView;

public class SearchPresenter {


    @Inject
    SearchOptions searchOptions;

    @Inject
    Model model;

    private SearchView searchView;

    public SearchPresenter ( SearchView searchView ) {
        App.getAppComponent ( ).inject ( this );
        this.searchView = searchView;
    }

    public void actionSearch () {
        searchOptions.setQuery (searchView.getQuery ());
        searchOptions.setImageTypeIndex (searchView.getImageTypeIndex ());
        searchOptions.setImageTypeChoice (searchView.getImageTypeChoice ());
        searchOptions.setOrientationIndex (searchView.getOrientationIndex ());
        searchOptions.setOrientationChoice (searchView.getOrientationChoice ());
        searchOptions.setCategoryIndex (searchView.getCategoryIndex ());
        searchOptions.setCategoryChoice (searchView.getCategoryChoice ());
        searchOptions.setColorsChecks (searchView.getColorsChecks ());
        searchOptions.setColorsChoice (searchView.getColorsChoice ());
        searchOptions.setEditorChoice ( searchView.getEditorChoice() );
        searchOptions.setSafeSearchChoice ( searchView.getSafeSearchChoice() );
        searchOptions.setOrderIndex ( searchView.getOrderIndex() );
        searchOptions.setOrderChoice ( searchView.getOrderChoice() );
        searchOptions.saveToPref ();
        model.setNeedReload ( true );
        searchView.back ();
    }

    public void initRequest ( ) {
        searchView.initQuery ( searchOptions.getQuery () );
        searchView.initImageTypeIndex (searchOptions.getImageTypeIndex () );
        searchView.initImageTypeChoice ( searchOptions.getImageTypeChoice () );
        searchView.initOrientationIndex (searchOptions.getOrientationIndex () );
        searchView.initOrientationChoice( searchOptions.getOrientationChoice ());
        searchView.initCategoryIndex (searchOptions.getCategoryIndex ());
        searchView.initCategoryChoice ( searchOptions.getCategoryChoice ());
        searchView.initColorsChecks (searchOptions.getColorsChecks ());
        searchView.initColorsChoice ( searchOptions.getColorsChoice ());
        searchView.intEditorChoice( searchOptions.getEditorChoice ());
        searchView.initSafeSearchChoice( searchOptions.getSafeSearchChoice ());
        searchView.initOrderIndex( searchOptions.getOrderIndex ());
        searchView.initOrderChoice( searchOptions.getOrderChoice ());
    }
}
