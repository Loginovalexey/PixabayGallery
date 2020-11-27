package ru.loginovalexeydevelopment.picturefinder.app;

import android.app.Application;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import ru.loginovalexeydevelopment.picturefinder.app.di.AppComponent;
import ru.loginovalexeydevelopment.picturefinder.app.di.AppModule;
import ru.loginovalexeydevelopment.picturefinder.app.di.DaggerAppComponent;


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