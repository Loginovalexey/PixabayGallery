package ru.alapplications.myphoto.ui.detailFragment.presenter;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.ui.detailFragment.view.DetailView;


public class DetailPresenter {

    @Inject
    Model model;

    @Inject
    App app;

    private DetailView detailView;

    public DetailPresenter ( DetailView detailView ) {
        App.getAppComponent ( ).inject ( this );
        this.detailView = detailView;

    }

    public void onViewCreated ( ) {
        detailView.updateView ( model.getHits ( ) ,
                model.getCurrentIndex ( ) );
    }
}

