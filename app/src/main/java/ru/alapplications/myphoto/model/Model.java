package ru.alapplications.myphoto.model;

import android.util.Log;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.entities.Hit;

public class Model {
    private List<Hit> hits;
    private Integer   selectedIndex;
    private boolean   needReload = false;
    private int       loadedCount;

    public int getLoadedCount ( ) {
        return loadedCount;
    }

    public void setLoadedCount ( int loadedCount ) {
        this.loadedCount = loadedCount;
    }


    public boolean isNeedReload ( ) {
        return needReload;
    }

    public void setNeedReload ( boolean needReload ) {
        this.needReload = needReload;
    }

    public void setHits ( List<Hit> hits ) {
        this.hits = hits;
        Log.d ( App.TAG , "setHits" );
        checkDuplicates ( );
    }

    public List<Hit> getHits ( ) {
        return hits;
    }

    public void setCurrentIndex ( Integer index ) {
        selectedIndex = index;
    }

    public Integer getCurrentIndex ( ) {
        return selectedIndex;
    }


    private void checkDuplicates ( ) {

        if ( hits.size ( ) > 0 ) {
            Single.create ( emitter -> {
                for (int i = 0; i < hits.size ( ) - 1; i++) {
                    for (int j = i + 1; j < hits.size ( ); j++) {
                        if ( hits.get ( i ) != null && hits.get ( j ) != null && hits.get ( i ).getId ( ).equals ( hits.get ( j ).getId ( ) ) ) {
                            Log.d ( App.TAG , i + "," + j + ":Id: " + hits.get ( i ).getId ( ) );
                        }
                    }
                }
                emitter.onSuccess ( true );
            } )
                    .observeOn ( Schedulers.io ( ) )
                    .subscribeOn ( Schedulers.io ( ) )
                    .subscribe ( result -> Log.d ( App.TAG , "-----------------" ) );

        }
    }

    public void reset ( ) {
        hits = null;
        selectedIndex = 0;
        needReload = false;
        loadedCount = 0;
    }
}