package ru.alapplications.myphoto.ui.old;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.ServerHelper;

public class HitsDataSource extends PositionalDataSource<Hit> {

    @Inject
    ServerHelper serverHelper;

    @Inject
    Model model;

    private Integer lastPageNumber;
    AtomicBoolean isLastPageReached;
    Integer totalHits;

    public HitsDataSource ( ) {
        //App.getAppComponent ( ).inject ( this );
        isLastPageReached = new AtomicBoolean ( false );

    }

    @Override
    public void loadInitial ( @NonNull LoadInitialParams params , @NonNull LoadInitialCallback<Hit> callback ) {
        if ( model.getHits () != null ) {
            lastPageNumber = getPageNumber ( model.getHits ().size (), params.pageSize );
            callback.onResult ( model.getHits (), 0 );
        }
 //       else
//            serverHelper.request ( 0 , params.pageSize , serverResponse -> {
//                        if ( serverResponse != null ) {
//                            lastPageNumber = getPageNumber ( serverResponse.getTotalHits ( ), params.pageSize );
//                            if (getPageNumber ( params.requestedStartPosition+params.pageSize, params.pageSize)==lastPageNumber)
//                                isLastPageReached.set ( true );
//                            if ( serverResponse.getHits ( ) != null )
//                                callback.onResult ( serverResponse.getHits ( ) , 0 );
//                        }
//                    }
//            );
    }

    @Override
    public void loadRange ( @NonNull LoadRangeParams
                                    params , @NonNull LoadRangeCallback<Hit> callback ) {
//        if ( !isLastPageReached.get ( ) ) {
//            if (getPageNumber ( params.startPosition+params.loadSize, params.loadSize)==lastPageNumber)
//                isLastPageReached.set ( true );
//                serverHelper.request ( params.startPosition , params.loadSize , serverResponse -> {
//                    if ( serverResponse != null ) {
//                        if ( serverResponse.getTotalHits ( ) == null )
//                            lastPageNumber = getPageNumber ( serverResponse.getTotalHits ( ) , params.loadSize );
//                        callback.onResult ( serverResponse.getHits ( ) );
//                    }
//                });
//        }
    }

    private int getPageNumber ( int position , int pageSize ) {
        if ( position / pageSize == ( double ) position / pageSize )
            return position / pageSize;
        else return position / pageSize + 1;
    }
}