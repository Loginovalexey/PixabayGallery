package ru.alapplications.myphoto.ui.galleryFragment.view;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.ui.OnDetailScreenCall;
import ru.alapplications.myphoto.ui.OnMessage;
import ru.alapplications.myphoto.ui.OnSearchScreenCall;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.GalleryPresenter;

import static android.view.MotionEvent.ACTION_DOWN;


public class GalleryFragment extends Fragment implements GalleryView {

    private RecyclerView         recyclerView;
    private FloatingActionButton repeatButton;
    private              GalleryPresenter galleryPresenter;


    public static GalleryFragment getInstance (  ) {
        GalleryFragment fragment = new GalleryFragment ( );
        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        Log.d ( App.TAG , "GalleryFragment onCreate" );
        super.onCreate ( savedInstanceState );
        galleryPresenter = new GalleryPresenter ( this );
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
        repeatButton = getActivity ( ).findViewById ( R.id.repeatActionButton );
        repeatButton.setOnClickListener ( view1 -> galleryPresenter.onButtonClick() );
        galleryPresenter.onViewCreated ( );

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
        EditText searchEditText = getActivity ( ).findViewById ( R.id.searchEditText );
        searchEditText.setOnTouchListener ( ( view1 , motionEvent ) -> {
            if ( motionEvent.getAction ( ) == ACTION_DOWN ) {
                (( OnSearchScreenCall ) getActivity ( )).callSearchScreen ( );
            }
            return true;
        } );
    }


    @Override
    public void sendMessage ( String string ) {
        (( OnMessage ) getActivity ( )).simpleMessage ( "" , string );
    }

    @Override
    public void setAdapter ( HitAdapter hitAdapter ) {

        recyclerView.setAdapter ( hitAdapter );

    }

    @Override
    public Fragment getFragment ( ) {
        return this;
    }

    @Override
    public void hideRepeatButton ( ) {
//        repeatButton.hide ();
        recyclerView.invalidate ();
    }

    @Override
    public void showRepeatButton ( ) {
        repeatButton.show ();

    }

    @Override
    public void detailHitCall ( ) {
        (( OnDetailScreenCall ) getActivity ( )).callDetailScreen ( );
    }

    @Override
    public void onDestroy ( ) {
        super.onDestroy ( );
        Log.d ( App.TAG , "GalleryFragment onDestroy" );
        galleryPresenter.onDestroy ( );
    }
}
