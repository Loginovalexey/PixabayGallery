package ru.alapplications.myphoto.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.ui.detail.view.DetailFragment;
import ru.alapplications.myphoto.ui.gallery.view.GalleryFragment;
import ru.alapplications.myphoto.ui.search.view.SearchFragment;
import ru.alapplications.myphoto.ui.wallpaper.WallPaperFragment;

public class MainActivity extends AppCompatActivity implements
        OnSearchScreenCall,
        OnDetailScreenCall,
        OnWallPaperScreenCall,
        OnMessage {

    private Dialog dialog;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        setTheme ( R.style.AppTheme );
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        if ( savedInstanceState == null ) callStartFragment();

    }

    private void callStartFragment ( ) {
        getSupportFragmentManager ( )
                .beginTransaction ( )
                .replace ( R.id.galleryLayout , GalleryFragment.getInstance () )
                .setTransition ( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                .commit ( );
    }

    @Override
    public void callSearchScreen ( ) {

        addFragment ( SearchFragment.getInstance ( ) );
    }

    @Override
    public void callDetailScreen ( ) {

        addFragment ( DetailFragment.getInstance ( ) );
    }

    @Override
    public void callWallPaperScreen ( ) {

        addFragment ( WallPaperFragment.newInstance ( ) );
    }

    private void addFragment ( Fragment fragment ) {
        getSupportFragmentManager ( )
                .beginTransaction ( )
                .replace ( R.id.galleryLayout , fragment )
                .addToBackStack ( null )
                .setTransition ( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                .commit ( );
    }

    @Override
    public void showMessage ( String title , String message , OnDialogResponse onDialogResponse ) {
        removeOldDialogIfExist ( );
        createNewDialog ( title , message );
        showDialog ( );
        setDialogClickListener ( onDialogResponse );
    }

    private void removeOldDialogIfExist ( ) {
        if ( dialog != null ) dialog.cancel ( );
    }

    private void createNewDialog ( String title , String message ) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder ( this );
        alertDialogBuilder
                .setTitle ( title )
                .setMessage ( message )
                .setPositiveButton ( "Ok" , ( dialogInterface , i ) -> {
                    dialogInterface.dismiss ( );
                } );
        dialog = alertDialogBuilder.create ( );
    }

    private void showDialog ( ) {
        dialog.show ( );
    }

    private void setDialogClickListener ( OnDialogResponse onDialogResponse ) {
        dialog.setOnDismissListener ( dialogInterface -> {
            if ( onDialogResponse != null ) onDialogResponse.onOkButton ( );
            else dialogInterface.cancel ( );
        } );
    }

}
