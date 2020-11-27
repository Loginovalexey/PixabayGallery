package ru.loginovalexeydevelopment.picturefinder.app.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.loginovalexeydevelopment.picturefinder.app.App;
import ru.loginovalexeydevelopment.picturefinder.model.Model;
import ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel.CacheHandler;
import ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel.ServerHandler;
import ru.loginovalexeydevelopment.picturefinder.ui.search.viewmodel.SharedPreferencesHandler;

/**
 * Класс, используемые при внедрении зависимостей. Предоставляет внедряемые объекты
 */
@Module
public class AppModule {

    @Provides
    App provideApp ( ) {
        return App.getInstance ( );
    }

    @Singleton
    @Provides
    Model provideModel ( ) {
        return new Model ( );
    }

    @Singleton
    @Provides
    CacheHandler provideCacheHandler ( ) {
        return new CacheHandler ( );
    }

    @Singleton
    @Provides
    ServerHandler provideServerHandler ( ) {
        return new ServerHandler ( );
    }

    @Singleton
    @Provides
    SharedPreferencesHandler provideSharedPreferencesHandler ( ) {
        return new SharedPreferencesHandler ( );
    }

}
