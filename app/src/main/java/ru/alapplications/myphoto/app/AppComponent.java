package ru.alapplications.myphoto.app;

import javax.inject.Singleton;

import dagger.Component;
import ru.alapplications.myphoto.model.SearchOptions;
import ru.alapplications.myphoto.ui.detailFragment.presenter.DetailPresenter;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.CacheHelper;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.GalleryPresenter;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.HitsDataSource;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.ServerHelper;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.retrofit.ServerHandler;
import ru.alapplications.myphoto.ui.searchFragment.presenter.SearchPresenter;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject ( GalleryPresenter galleryPresenter );

    void inject ( DetailPresenter detailPresenter );

    void inject ( SearchPresenter searchPresenter );

    void inject ( SearchOptions searchOptions );

//    void inject ( CacheHelper cacheHelper );

    void inject ( ServerHandler serverHandler );

    void inject ( HitsDataSource hitsDataSource );
//
    void inject ( ServerHelper serverHelper );
}
