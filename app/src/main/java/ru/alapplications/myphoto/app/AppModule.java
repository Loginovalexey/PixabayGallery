package ru.alapplications.myphoto.app;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.alapplications.myphoto.db.MyPhotoDao;
import ru.alapplications.myphoto.db.MyPhotoDatabase;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.SearchOptions;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.CacheHelper;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.ServerHelper;

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

    @Singleton
    @Provides
    SearchOptions provideSearchOptions ( ) {
        return new SearchOptions ( );
    }

}
