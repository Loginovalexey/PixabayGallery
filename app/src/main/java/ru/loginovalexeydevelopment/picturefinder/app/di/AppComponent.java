package ru.loginovalexeydevelopment.picturefinder.app.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.loginovalexeydevelopment.picturefinder.ui.detail.viewmodel.DetailViewModel;
import ru.loginovalexeydevelopment.picturefinder.ui.detail.viewmodel.FileHandler;
import ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel.CacheHandler;
import ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel.GalleryViewModel;
import ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel.HitsDataSource;
import ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel.ServerHandler;
import ru.loginovalexeydevelopment.picturefinder.ui.search.viewmodel.SearchViewModel;
import ru.loginovalexeydevelopment.picturefinder.ui.search.viewmodel.SharedPreferencesHandler;


/**
 * Класс, используемые при внедрении зависимостей. Определяем клссы, в которые возможно внедрение
 */

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject ( GalleryViewModel galleryViewModel );

    void inject ( DetailViewModel detailViewModel );

    void inject ( SearchViewModel searchViewModel );

    void inject ( HitsDataSource hitsDataSource );

    void inject ( ServerHandler serverHandler );

    void inject ( CacheHandler cacheHandler );

    void inject ( FileHandler fileHandler );

    void inject ( SharedPreferencesHandler sharedPreferencesHandler );

}
