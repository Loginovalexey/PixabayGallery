package ru.alapplications.myphoto.app.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.ui.gallery.viewmodel.CacheHandler;
import ru.alapplications.myphoto.ui.gallery.viewmodel.ServerHandler;
import ru.alapplications.myphoto.ui.search.viewmodel.SharedPreferencesHandler;

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
