package ru.alapplications.myphoto.app;

import android.app.Application;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import ru.alapplications.myphoto.app.di.AppComponent;
import ru.alapplications.myphoto.app.di.AppModule;
import ru.alapplications.myphoto.app.di.DaggerAppComponent;


/**
 * Класс приложения
 */
public class App extends Application {
    private static App          instance;
    private static AppComponent appComponent;

    public static App getInstance ( ) {
        return instance;
    }

    public void onCreate ( ) {
        super.onCreate ( );
        instance = this;
        appComponent = generateAppComponent ( );
        FirebaseCrashlytics.getInstance();
    }


    private AppComponent generateAppComponent ( ) {
        return DaggerAppComponent
                .builder ( )
                .appModule ( new AppModule ( ) )
                .build ( );
    }

    public static AppComponent getAppComponent ( ) {
        return appComponent;
    }

}