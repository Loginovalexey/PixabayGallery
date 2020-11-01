package ru.alapplications.myphoto.ui.galleryFragment.presenter;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;

import java.util.concurrent.Executors;

import javax.inject.Inject;

import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.Model;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.ui.galleryFragment.view.GalleryView;
import ru.alapplications.myphoto.ui.galleryFragment.view.HitAdapter;

public class GalleryPresenter {
    private GalleryView galleryView;
    private HitAdapter  adapter;
    LiveData<PagedList<Hit>> pagedListLiveData;


    @Inject
    Model model;


    public GalleryPresenter ( GalleryView galleryView ) {
        App.getAppComponent ( ).inject ( this );
        this.galleryView = galleryView;
        adapter = createNewAdapter ( );
        ConnectionStateMonitor connectionStateMonitor = new ConnectionStateMonitor ( galleryView.getFragment ( ).getContext ( ) );
        connectionStateMonitor.observe ( galleryView.getFragment ( ) , aBoolean -> {
            if ( aBoolean ) {
                Log.d ( App.TAG , "Сеть доступна" );
                onButtonClick ();
//                    galleryView.hideRepeatButton ( );
                //adapter.notifyItemRangeChanged ( 0, adapter.getCurrentList ().getLoadedCount () );
            } else {
                Log.d ( App.TAG , "Сеть пропала" );
                galleryView.sendMessage ( "Проверьте подключение к интернету" );
                //galleryView.showRepeatButton ( );
            }
        } );
//        connectionStateMonitor
//                .hasConnection ( )
//                .subscribeOn ( Schedulers.io ( ) )
//                .observeOn ( AndroidSchedulers.mainThread ( ) )
//                .subscribe ( result -> {
//                    Log.d ( App.TAG , "pixabay" + String.valueOf ( result ) );
//                } );


    }

    public HitAdapter createNewAdapter ( ) {
        HitsDataSourceFactory sourceFactory = new HitsDataSourceFactory ( );
        PagedList.Config config = new PagedList.Config.Builder ( )
                .setPageSize ( 20 )
                .setEnablePlaceholders ( true )
                .build ( );
        pagedListLiveData = new LivePagedListBuilder<> ( sourceFactory , config )
                .setFetchExecutor ( Executors.newSingleThreadExecutor ( ) )
                .build ( );
        HitDiffUtilCallback diffUtilCallback = new HitDiffUtilCallback ( );
        HitAdapter resultAdapter = new HitAdapter ( diffUtilCallback , position -> {
            if ( model.getHits ( ).size ( ) > position && model.getHits ( ).get ( position ) != null ) {
                model.setCurrentIndex ( position );
                galleryView.detailHitCall ( );
            }
        } );
        pagedListLiveData.observe ( galleryView.getFragment ( ) , hits -> {
            resultAdapter.submitList ( hits );
            Log.d ( App.TAG , "submitList ( hits )" );
        } );
        resultAdapter.setStateRestorationPolicy ( RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY );


        resultAdapter.registerAdapterDataObserver ( new AdapterDataObserver ( ) {
            @Override
            public void onItemRangeInserted ( int positionStart , int itemCount ) {
                super.onItemRangeInserted ( positionStart , itemCount );
                Log.d ( App.TAG , "onItemRangeInserted " + positionStart + " " + itemCount );
                if ( !adapter.getCurrentList ( ).isEmpty ( ) ) {
                    model.setHits ( adapter.getCurrentList ( ) );
                    model.setLoadedCount ( adapter.getCurrentList ( ).getLoadedCount ( ) );
                }
            }

            @Override
            public void onItemRangeChanged ( int positionStart , int itemCount ) {
                super.onItemRangeChanged ( positionStart , itemCount );
                Log.d ( App.TAG , "onItemRangeChanged " + positionStart + " " + itemCount );
                model.setHits ( adapter.getCurrentList ( ) );
                model.setLoadedCount ( adapter.getCurrentList ( ).getLoadedCount ( ) );
            }
        } );
        return resultAdapter;

    }

    public void onViewCreated ( ) {
        if ( model.isNeedReload ( ) ) {
            model.reset ( );
            CacheHelper.setNeedSaveCache ( false );
            CacheHelper.clearCache ( onFinish -> {
                adapter = createNewAdapter ( );
                adapter.setStateRestorationPolicy ( RecyclerView.Adapter.StateRestorationPolicy.ALLOW );
                galleryView.setAdapter ( adapter );

            } );
        } else galleryView.setAdapter ( adapter );
    }


    public void onDestroy ( ) {
        adapter.onDestroy ( );
        if ( CacheHelper.isNeedSaveCache ( ) && !model.getHits ().isEmpty ( ) )
            CacheHelper.saveCache ( model.getHits ( ).subList ( 0 , model.getLoadedCount ( ) ) , model.getHits ( ).size ( ) );

    }

    public void onButtonClick ( ) {

        Log.d ( App.TAG , "OnClick" );

        adapter.getCurrentList ( ).getDataSource ( ).invalidate ( );
        adapter.notifyDataSetChanged ( );

        galleryView.hideRepeatButton ();
    }
}