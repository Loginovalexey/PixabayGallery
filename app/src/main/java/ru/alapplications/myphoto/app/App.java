package ru.alapplications.myphoto.app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;


public class App extends Application {
    public static final String TAG = "PixabayViewer";
    private static App          instance;
    private static AppComponent appComponent;

    public static App getInstance ( ) {
        return instance;
    }

    public void onCreate ( ) {
        super.onCreate ( );

        //Fabric.with ( this, new Crashlytics( ) );

        if ( LeakCanary.isInAnalyzerProcess ( this ) ){
            return;
        }
        LeakCanary.install ( this );

        Stetho.initializeWithDefaults(this);

        instance = this;
        appComponent = generateAppComponent ( );
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