package ru.alapplications.myphoto.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.ui.detailFragment.view.DetailFragment;
import ru.alapplications.myphoto.ui.galleryFragment.view.GalleryFragment;
import ru.alapplications.myphoto.ui.searchFragment.view.SearchFragment;
import ru.alapplications.myphoto.ui.wallPaperFragment.WallPaperFragment;


public class MainActivity extends AppCompatActivity implements
        OnSearchScreenCall,
        OnDetailScreenCall,
        OnWallPaperScreenCall,
        OnMessage {

    Dialog dialog;


    private void addFragmentToContainer ( Fragment fragment ) {
        getSupportFragmentManager ( )
                .beginTransaction ( )
                .replace ( R.id.mainActivityRootFrameLayout , fragment )
                .addToBackStack ( null )
                .setTransition ( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                .commit ( );
    }

    private void replaceFragmentInContainer ( Fragment fragment ) {
        getSupportFragmentManager ( )
                .beginTransaction ( )
                .replace ( R.id.mainActivityRootFrameLayout , fragment )
                .setTransition ( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                .commit ( );
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        setTheme ( R.style.AppTheme );
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        replaceFragmentInContainer ( GalleryFragment.getInstance ( ) );
    }

    @Override
    public void callDetailScreen ( ) {
        addFragmentToContainer ( DetailFragment.newInstance ( ) );
    }


    @Override
    public void callSearchScreen ( ) {

        addFragmentToContainer ( SearchFragment.newInstance ( ) );

//        getSupportFragmentManager ( )
//                .beginTransaction ( )
//                .replace ( R.id.mainActivityRootFrameLayout , SearchFragment.newInstance ( ) )
//                .addToBackStack ( null )
//                .setTransition ( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
//                .commit ( );
//    }

//    @Override
//    public void onBackPressed ( ) {
//        Log.d ( App.TAG , getSupportFragmentManager ( ).getFragments ( ).toString ( ) );
//        if ( (getSupportFragmentManager ( ).getFragments ( ).size ( ) == 1) &&
//                (getSupportFragmentManager ( ).getFragments ( ).get ( 0 ) instanceof SearchFragment)
//        ) {
//            getSupportFragmentManager ( )
//                    .beginTransaction ( )
//                    .replace ( R.id.mainActivityRootFrameLayout , GalleryFragment.getInstance () )
//                    .setTransition ( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
//                    .commit ( );
//        } else {
//            super.onBackPressed ( );
//        }
//
    }

    @Override
    public void simpleMessage ( String title , String message ) {
        if ( dialog != null ) dialog.cancel ( );
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder ( this );
        alertDialogBuilder.setTitle ( title )
                .setMessage ( message )
                .setPositiveButton ( "Ok" , ( dialog , id ) -> dialog.cancel ( ) );
        dialog = alertDialogBuilder.create ( );
        dialog.show ( );
    }

    @Override
    public void callWallPaper ( ) {
        addFragmentToContainer ( WallPaperFragment.newInstance ( ) );
//        getSupportFragmentManager ( )
//                .beginTransaction ( )
//                .replace ( R.id.mainActivityRootFrameLayout , WallPaperFragment.newInstance ( ) )
//                .addToBackStack ( null )
//                .setTransition ( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
//                .commit ( );
    }
}
