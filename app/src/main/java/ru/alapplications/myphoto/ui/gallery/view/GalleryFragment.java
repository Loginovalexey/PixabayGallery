package ru.alapplications.myphoto.ui.gallery.view;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.ui.OnDetailScreenCall;
import ru.alapplications.myphoto.ui.OnDialogResponse;
import ru.alapplications.myphoto.ui.OnMessage;
import ru.alapplications.myphoto.ui.OnSearchScreenCall;
import ru.alapplications.myphoto.ui.gallery.viewmodel.GalleryViewModel;
import ru.alapplications.myphoto.ui.gallery.viewmodel.HitDiffUtilCallback;
import ru.alapplications.myphoto.ui.gallery.viewmodel.LoadingState;


import static android.view.MotionEvent.ACTION_DOWN;


public class GalleryFragment extends Fragment {
    private GalleryViewModel viewModel;
    private GalleryAdapter   adapter;
    private RecyclerView                                                     recyclerView;
    private FloatingActionButton repeatButton;
    EditText searchEditText;


    public static GalleryFragment getInstance ( ) {
        return new GalleryFragment ( );
    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        Log.d ( App.TAG , "GalleryFragment onCreateView" );
        return inflater.inflate ( R.layout.fragment_hits_gallery , container , false );
    }

    @Override
    public void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );
        Log.d ( App.TAG , "GalleryFragment onViewCreated " );
        initRecyclerView ( );
        initSearchEditText ( );
        initRepeatButton ( );
        initViewModel ( );
        initAdapter ( );
        observe ( );
    }

    private void initAdapter ( ) {
        HitDiffUtilCallback diffUtilCallback = new HitDiffUtilCallback ( );
        adapter = new GalleryAdapter ( diffUtilCallback , position -> {
            if ( adapter.getCurrentList ( ).get ( position ) != null ) {
                viewModel.setCurrentIndex ( position );
                detailHitCall ( );
            }
        } );
        recyclerView.setAdapter ( adapter );
    }

    private void initRepeatButton ( ) {
        repeatButton = getActivity ( ).findViewById ( R.id.galleryRepeatActionButton );

    }

    private void initViewModel ( ) {
        viewModel = ViewModelProviders.of ( getActivity ( ) ).get ( GalleryViewModel.class );
        viewModel.onViewCreated ( );

    }

    private void observe ( ) {

        viewModel.searchQuery.observe ( getViewLifecycleOwner ( ) , searchQuery -> {
            if (searchQuery == "") searchEditText.setText ( getContext ().getResources ().getString ( R.string.search ) );
            else searchEditText.setText(searchQuery);
        });

        viewModel.loadingState.observe ( getViewLifecycleOwner ( ) , loadingState -> {
            Log.d(App.TAG,"получен "+loadingState.toString ());
            if ( loadingState == LoadingState.FIRST_LOAD_ERROR ) {
                (( OnMessage ) getActivity ( )).showMessage ( "Ошибка" , "Нет связи с сервером" ,
                        new OnDialogResponse ( ) {
                            @Override
                            public void onOkButton ( ) {
                                firstReload ( );
                            }
                        } );

            } else if ( (loadingState == LoadingState.ERROR) ) {
                (( OnMessage ) getActivity ( )).showMessage ( "Ошибка" , "Нет связи с сервером" ,
                        new OnDialogResponse ( ) {
                            @Override
                            public void onOkButton ( ) {
                                reload ( );
                            }
                        } );
            } else if ( loadingState == LoadingState.NO_MORE_DATA ) {
                (( OnMessage ) getActivity ( )).showMessage ( "Нет данных" , "Попробуйте другие условия поиска" , null );
            }
        } );

        viewModel.pagedListLiveData.observe ( getViewLifecycleOwner ( ) , hits -> {
            adapter.submitList ( hits );
        } );

        viewModel.isNewDataSet.observe ( getViewLifecycleOwner ( ) , isNewDataSet -> {
            if ( isNewDataSet ) {
                adapter.setStateRestorationPolicy ( RecyclerView.Adapter.StateRestorationPolicy.PREVENT );
                adapter.setStateRestorationPolicy ( RecyclerView.Adapter.StateRestorationPolicy.ALLOW );
            }

        } );
    }


    private void initRecyclerView ( ) {
        recyclerView = getActivity ( ).findViewById ( R.id.galleryRecyclerView );
        recyclerView.setHasFixedSize ( true );
        int spanCount;
        if ( getResources ( ).getConfiguration ( ).orientation == Configuration.ORIENTATION_PORTRAIT ) {
            spanCount = 3;
        } else {
            spanCount = 4;
        }
        recyclerView.setLayoutManager ( new GridLayoutManager ( getContext ( ) , spanCount ) );
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initSearchEditText ( ) {
        searchEditText = getActivity ( ).findViewById ( R.id.gallerySearchEditText );
        searchEditText.setOnTouchListener ( ( view1 , motionEvent ) -> {
            if ( motionEvent.getAction ( ) == ACTION_DOWN ) {
                (( OnSearchScreenCall ) getActivity ( )).callSearchScreen ( );
            }
            return true;
        } );
    }


    public void sendMessage ( String string ) {
        (( OnMessage ) getActivity ( )).showMessage ( "" , string , null );
    }


    public void setAdapter ( GalleryAdapter galleryAdapter ) {

        recyclerView.setAdapter ( galleryAdapter );

    }


    public Fragment getFragment ( ) {
        return this;
    }


    public void hideRepeatButton ( ) {
        repeatButton.hide ( );
    }


    public void reload ( ) {
        repeatButton.show ( );
        repeatButton.setOnClickListener ( view1 -> {
            viewModel.reload ( );
            Log.d ( App.TAG, "reload" );
            hideRepeatButton ( );
        } );
    }

    public void removeAllObservers(){
        viewModel.loadingState.removeObservers ( getViewLifecycleOwner ( ) );
        viewModel.pagedListLiveData.removeObservers ( getViewLifecycleOwner ( ) );
        viewModel.isNewDataSet.removeObservers ( getViewLifecycleOwner ( ) );
    }

    public void firstReload ( ) {
        repeatButton.show ( );
        repeatButton.setOnClickListener ( view1 -> {
            removeAllObservers ();
            viewModel.firstReload ( );
            observe ();
            hideRepeatButton ( );
        } );
    }

    public void detailHitCall ( ) {
        (( OnDetailScreenCall ) getActivity ( )).callDetailScreen ( );
    }

    private boolean isTopFragment ( ) {
        return (getActivity ( ).getSupportFragmentManager ( ).getFragments ( ).get (
                getActivity ( ).getSupportFragmentManager ( ).getFragments ( ).size ( ) - 1
        ) == this);
    }

    @Override
    public void onDestroy ( ) {
        super.onDestroy ( );

        if ( isTopFragment ( ) ) {
            Log.d ( App.TAG , "GalleryFragment onDestroy" );
            viewModel.saveData ( );
            adapter.onDestroy ( );
        }
    }

    public void createNewAdapter ( ) {


//        viewModel.connectionStateMonitor.observe ( getViewLifecycleOwner () , aBoolean -> {
//            if ( aBoolean ) {
//                sendMessage ( "Сеть доступна" );
//            } else {
//                sendMessage ( "Проверьте подключение к интернету" );
//            }
//        } );
//        adapter.registerAdapterDataObserver ( new RecyclerView.AdapterDataObserver ( ) {
//
//            @Override
//            public void onItemRangeInserted ( int positionStart , int itemCount ) {
//                super.onItemRangeInserted ( positionStart , itemCount );
//                Log.d ( App.TAG , "onItemRangeInserted " + positionStart + " " + itemCount );
//                if ( !adapter.getCurrentList ( ).isEmpty ( ) ) {
////                    model.setHits ( adapter.getCurrentList ( ) );
////                    model.setLoadedCount ( adapter.getCurrentList ( ).getLoadedCount ( ) );
//                }
//            }
//
//            @Override
//            public void onItemRangeChanged ( int positionStart , int itemCount ) {
//                super.onItemRangeChanged ( positionStart , itemCount );
//                Log.d ( App.TAG , "onItemRangeChanged " + positionStart + " " + itemCount );
////                model.setHits ( adapter.getCurrentList ( ) );
////                model.setLoadedCount ( adapter.getCurrentList ( ).getLoadedCount ( ) );
//            }
//        } );

    }
}
