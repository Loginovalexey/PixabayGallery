package ru.loginovalexeydevelopment.picturefinder.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.loginovalexeydevelopment.picturefinder.R;
import ru.loginovalexeydevelopment.picturefinder.ui.detail.view.DetailFragment;
import ru.loginovalexeydevelopment.picturefinder.ui.gallery.view.GalleryFragment;
import ru.loginovalexeydevelopment.picturefinder.ui.search.view.SearchFragment;
import ru.loginovalexeydevelopment.picturefinder.ui.wallpaper.WallPaperFragment;

/**
 * Главная активити
 */
public class MainActivity extends AppCompatActivity implements
        OnSearchScreenCall,
        OnDetailScreenCall,
        OnWallPaperScreenCall,
        OnMessage {

    //Переменная для показа диалогов
    private Dialog dialog;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        setTheme ( R.style.AppTheme );
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        //При запуске приложения - размещение стартового фрагмента
        if ( savedInstanceState == null ) callStartFragment ( );

    }

    //Стартовый фрагмент - фрагмент галереи
    private void callStartFragment ( ) {
        getSupportFragmentManager ( )
                .beginTransaction ( )
                .replace ( R.id.rootLayout , GalleryFragment.getInstance ( ) )
                .setTransition ( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                .commit ( );
    }

    //Размещение в активити фрагмента параметров поиска
    @Override
    public void callSearchScreen ( ) {
        addFragment ( SearchFragment.getInstance ( ) );
    }

    //Размещение в активити фрагмента увеличенного изображения
    @Override
    public void callDetailScreen ( ) {
        addFragment ( DetailFragment.getInstance ( ) );
    }

    //Размещение в активити фрагмента установки обоев
    @Override
    public void callWallPaperScreen ( ) {

        addFragment ( WallPaperFragment.newInstance ( ) );
    }

    //Добавление вызываемого фрагмента в стек
    private void addFragment ( Fragment fragment ) {
        getSupportFragmentManager ( )
                .beginTransaction ( )
                .replace ( R.id.rootLayout , fragment )
                .addToBackStack ( null )
                .setTransition ( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                .commit ( );
    }


    /**
     * Показ диалога
     *
     * @param title            - заголовок диалогового окна
     * @param message          - сообщение
     * @param onDialogResponse - колбэк при нажатии на кнопку
     */
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
                .setPositiveButton ( "Ok" , ( dialogInterface , i ) ->
                        dialogInterface.dismiss ( ) );
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
