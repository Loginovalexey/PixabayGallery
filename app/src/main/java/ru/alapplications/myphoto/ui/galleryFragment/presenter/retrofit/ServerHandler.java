package ru.alapplications.myphoto.ui.galleryFragment.presenter.retrofit;

import android.util.Log;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.SearchOptions;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.model.entities.ServerResponse;

public class ServerHandler {

    public interface OnAddPage {
        void reportResult ( PageAddResult pageAddResult , List<Hit> resultHits );
    }

    enum PageAddResult {
        OK,
        EMPTY,
        MAXIMUM,
        ERROR
    }

    private final static int        pageSize = 20;
    private              Integer    totalHits;
    private              Disposable disposable;

    @Inject
    SearchOptions searchOptions;

    public ServerHandler ( ) {
        //App.getAppComponent ( ).inject ( this );
    }

    public Single<ServerResponse> paramsRequest (int nextPageNumber) {
        return new ApiBuilder ( )
                .requestServerSingleData (
                        searchOptions.getQuery ( ) ,
                        Locale.getDefault ( ).getLanguage ( ) ,
                        searchOptions.getImageTypeValue ( ) ,
                        searchOptions.getOrientationValue ( ) ,
                        searchOptions.getCategoryValue ( ) ,
                        searchOptions.getColorValues ( ) ,
                        searchOptions.getEditorChoice ( ) ,
                        searchOptions.getSafeSearchChoice ( ) ,
                        searchOptions.getOrderValue ( ) ,
                        nextPageNumber,
                        pageSize );
    }

    private boolean isMaximumHitsLoaded ( int hitsSize ) {
        return ((hitsSize % pageSize) != 0) ||
                ((totalHits != null) && (hitsSize == totalHits));
    }

    private int calculateNextPage ( int hitsSize ) {
        return hitsSize / pageSize + 1;
    }

    public void getNextPage ( int hitsSize , OnAddPage onAddPage ) {
        Log.d ( App.TAG , "loadFromServer" );
        if ( isMaximumHitsLoaded ( hitsSize ) )
            onAddPage.reportResult ( PageAddResult.MAXIMUM , null );
        else
            disposable =
                    paramsRequest (calculateNextPage (hitsSize))
                            .observeOn ( AndroidSchedulers.mainThread ( ) )
                            .subscribeOn ( Schedulers.io ( ) )
                            .subscribe (
                                    serverResponse -> {
                                        if ( serverResponse.getTotalHits ( ) == 0 ) {
                                            onAddPage.reportResult (
                                                    PageAddResult.EMPTY , null );
                                        } else {
                                            totalHits = serverResponse.getTotalHits ( );
                                            onAddPage.reportResult (
                                                    PageAddResult.OK , serverResponse.getHits ( ) );
                                        }
                                    } ,
                                    throwable -> onAddPage.reportResult (
                                            PageAddResult.ERROR , null ) );
    }

    public void onDestroy ( ) {
        if ( (disposable != null) && (!disposable.isDisposed ( )) ) {
            disposable.dispose ( );
        }
    }
}