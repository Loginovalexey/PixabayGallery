package ru.alapplications.myphoto.app;

import javax.inject.Singleton;

import dagger.Component;
import ru.alapplications.myphoto.ui.gallery.viewmodel.GalleryViewModel;
import ru.alapplications.myphoto.ui.gallery.viewmodel.HitsDataSource;
import ru.alapplications.myphoto.ui.gallery.viewmodel.ServerHelper;
import ru.alapplications.myphoto.ui.search.viewmodel.SearchViewModel;


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject ( GalleryViewModel galleryViewModel );

    void inject ( SearchViewModel searchViewModel );

    void inject ( HitsDataSource hitsDataSource );

    void inject ( ServerHelper serverHelper );
}
