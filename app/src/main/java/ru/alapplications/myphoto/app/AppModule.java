package ru.alapplications.myphoto.app;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.ui.gallery.viewmodel.ServerHelper;


@Module
class AppModule {

    AppModule ( ) {
    }

    @Provides
    App provideApp ( ) {
        return App.getInstance ( );
    }


    @Singleton
    @Provides
    Model provideModel ( ) {
        return new Model ( );
    }

//    @Singleton
//    @Provides
//    CacheHelper provideCacheHelper ( ) {
//        return new CacheHelper ( );
//    }

    @Singleton
    @Provides
    ServerHelper provideServerHelper ( ) {
        return new ServerHelper ( );
    }


}
