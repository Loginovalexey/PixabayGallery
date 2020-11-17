package ru.alapplications.myphoto.ui.detail.view;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.ui.OnMessage;
import ru.alapplications.myphoto.ui.OnWallPaperScreenCall;
import ru.alapplications.myphoto.ui.detail.viewmodel.DetailViewModel;
import ru.alapplications.myphoto.ui.detail.viewmodel.OnChangeItem;
import ru.alapplications.myphoto.ui.detail.viewmodel.OnClickDetailImage;

/**
 * Фрагмент для детализированного представления изображения
 */

public class DetailFragment extends Fragment implements
        OnClickDetailImage,
        OnChangeItem {
    private DetailViewModel    viewModel;
    private DetailPagerAdapter adapter;
    private ZoomablelePager    pager;
    private LinearLayout       downLayout;
    private boolean            isDownLayoutVisible = true;
    private Hit                currentHit;


    public static DetailFragment getInstance ( ) {
        return new DetailFragment ( );
    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        return inflater.inflate ( R.layout.fragment_detail , container , false );
    }

    @Override
    public void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );
        initAdapter ( );
        initViewModel ( );
        initPager ( );
        initDownLayout ( );
        observe ( );
    }

    private void initAdapter ( ) {
        adapter = new DetailPagerAdapter (
                this ,
                this
        );
    }

    private void initViewModel ( ) {
        viewModel = ViewModelProviders.of ( getActivity ( ) ).get ( DetailViewModel.class );
        viewModel.onViewCreated ( );
    }

    private void initPager ( ) {
        pager = getActivity ( ).findViewById ( R.id.detailViewPager );
    }

    /**
     * Установка действий для панели инструментов
     */
    private void initDownLayout ( ) {
        downLayout = getActivity ( ).findViewById ( R.id.detailDownLayout );
        setDownLayoutVisibility ( );
        ImageView downloadImageView = getActivity ( ).
                findViewById ( R.id.downloadImageView );
        ImageView setWallPaperImageView = getActivity ( ).
                findViewById ( R.id.setWallPaper );
        ImageView shareImageView = getActivity ( ).
                findViewById ( R.id.shareImageView );

        downloadImageView.setOnClickListener ( view -> {
                    if ( currentHit.isLoaded ( ) ) {
                        viewModel.downLoad (
                                pager.getCurrentItem ( ) ,
                                getCurrentBitmap ( ) );
                    }
                }
        );
        setWallPaperImageView.setOnClickListener ( view -> {
            if ( currentHit.isLoaded ( ) ) {
                viewModel.setWallPaper ( pager.getCurrentItem ( ), getCurrentBitmap ( ) );
            }
        } );
        shareImageView.setOnClickListener ( view -> {
            if ( currentHit.isLoaded ( ) ) {
                viewModel.share (pager.getCurrentItem ( ), getCurrentBitmap ( ) );
            }
        } );
    }

    private void observe ( ) {
        //Получние данных
        viewModel.hits.observe ( getViewLifecycleOwner ( ) , hits -> {
            adapter.setHits ( hits );
            pager.setAdapter ( adapter );
            viewModel.currentIndex.observe (
                    getViewLifecycleOwner ( ) ,
                    //Показ выбранной страницы
                    index -> pager.setCurrentItem ( index )
            );
        } );
        //Запрос разрешений на запись
        viewModel.requestPermission.observe ( getViewLifecycleOwner ( ) , isRequest -> {
            if ( isRequest ) requestPermission ( );
        } );
        //Показ сообщений
        viewModel.message.observe ( getViewLifecycleOwner ( ) , message -> {
            if ( message != null ) showMessage ( message.first , message.second );
        } );
        //Переход на экран установки обоев
        viewModel.wallPaperScreenCall.observe (
                getViewLifecycleOwner ( ) ,
                isWallPaperScreenCall -> {
                    if ( isWallPaperScreenCall != null )
                        (( OnWallPaperScreenCall ) getActivity ( )).callWallPaperScreen ( );
                } );
    }

    /**
     * Вызов диалоговых оконо для получения разрешения на запись
     */
    public void requestPermission ( ) {
        final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
        if ( ActivityCompat.shouldShowRequestPermissionRationale (
                getActivity ( ) ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) ) {
            Toast.makeText ( getActivity ( ) ,
                    R.string.uploadFileExplanation ,
                    Toast.LENGTH_LONG ).show ( );
        }
        ActivityCompat.requestPermissions ( getActivity ( ) ,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} ,
                REQUEST_WRITE_EXTERNAL_STORAGE );
    }

    public void showMessage ( String title , String message ) {
        (( OnMessage ) getActivity ( )).showMessage ( title , message , null );
    }

    public void setCurrentHit ( Hit currentHit ) {
        this.currentHit = currentHit;
    }

    //Колбэк вызывается из адаптера
    @Override
    public void onClick ( ) {
        //Показать/спрятать панель инструментов
        isDownLayoutVisible = !isDownLayoutVisible;
        setDownLayoutVisibility ( );
    }

    private void setDownLayoutVisibility ( ) {
        if ( isDownLayoutVisible )
            downLayout.setVisibility ( View.VISIBLE );
        else
            downLayout.setVisibility ( View.GONE );
    }

    /**
     * Получение изображения в формате Bitmap c текущей страницы
     */
    private Bitmap getCurrentBitmap ( ) {
        return (( BitmapDrawable ) getCurrentImageView ( ).getDrawable ( )).getBitmap ( );
    }

    // Получение ImageView c текущей страницы
    private ImageView getCurrentImageView ( ) {
        return getCurrentContainer ().findViewById ( R.id.detailImageView );
    }

    //Получение контенера с изображением для текущей страницы
    private View getCurrentContainer ( ) {
        try {
            final int currentItem = pager.getCurrentItem ( );
            for (int i = 0; i < pager.getChildCount ( ); i++) {
                final View child = pager.getChildAt ( i );
                final ViewPager.LayoutParams layoutParams = ( ViewPager.LayoutParams )
                        child.getLayoutParams ( );
                Field f = layoutParams.getClass ( )
                        .getDeclaredField ( "position" ); //NoSuchFieldException
                f.setAccessible ( true );
                int position = ( Integer ) f.get ( layoutParams ); //IllegalAccessException
                if ( !layoutParams.isDecor && currentItem == position ) {
                    return child;
                }
            }
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            Log.e ( "Exception" , e.toString ( ) );
        }
        return null;
    }



    private boolean isTopFragment ( ) {
        return (getActivity ( ).getSupportFragmentManager ( ).getFragments ( ).get (
                getActivity ( ).getSupportFragmentManager ( ).getFragments ( ).size ( ) - 1
        ) == this);
    }

    //Действия при уничтожении фрагмента
    @Override
    public void onDestroy ( ) {
        super.onDestroy ( );
        if ( isTopFragment ( ) ) {
            //Сохранение номера текущей страницы на случай переворота экрана
            viewModel.setCurrentIndex ( pager.getCurrentItem ( ) );
            adapter.onDestroy ();
        }
    }
}

