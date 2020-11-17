package ru.alapplications.myphoto.ui.gallery.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.ui.OnDetailScreenCall;
import ru.alapplications.myphoto.ui.OnMessage;
import ru.alapplications.myphoto.ui.OnSearchScreenCall;
import ru.alapplications.myphoto.ui.gallery.viewmodel.GalleryViewModel;
import ru.alapplications.myphoto.ui.gallery.viewmodel.HitDiffUtilCallback;
import ru.alapplications.myphoto.ui.gallery.viewmodel.LoadingState;

import static android.view.MotionEvent.ACTION_DOWN;

/**
 * Фрагмент для показа галереи картинок
 */

public class GalleryFragment extends Fragment {
    private GalleryAdapter       adapter;
    private RecyclerView         recyclerView;
    private FloatingActionButton repeatButton;
    private EditText             searchEditText;
    private GalleryViewModel     viewModel;

    public static GalleryFragment getInstance ( ) {
        return new GalleryFragment ( );
    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        return inflater.inflate ( R.layout.fragment_gallery , container , false );
    }

    @Override
    public void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );
        initRecyclerView ( );
        initSearchEditText ( );
        initLogo ( );
        initRepeatButton ( );
        initViewModel ( );
        initAdapter ( );
        observe ( );
    }

    private void initRecyclerView ( ) {
        recyclerView = getActivity ( ).findViewById ( R.id.galleryRecyclerView );
        recyclerView.setHasFixedSize ( true );
        int spanCount;
        if ( getResources ( ).getConfiguration ( ).orientation ==
                Configuration.ORIENTATION_PORTRAIT )
            spanCount = 3;
        else
            spanCount = 4;
        recyclerView.setLayoutManager ( new GridLayoutManager ( getContext ( ) , spanCount ) );
        recyclerView.setHasFixedSize ( true );
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSearchEditText ( ) {
        searchEditText = getActivity ( ).findViewById ( R.id.gallerySearchEditText );

        //Установка слушателя на нажатие на поле поиска. Переход на экран опций поиска
        searchEditText.setOnTouchListener ( ( view , motionEvent ) -> {
            searchScreenCalling ( motionEvent );
            return true;
        } );
    }

    private void initLogo ( ) {
        ImageView logoImageView = getActivity ( ).findViewById ( R.id.logoImageView );
        logoImageView.setOnClickListener ( view -> {
            Uri uri = Uri.parse ( getString ( R.string.pixabayPage ) );
            Intent intent = new Intent ( Intent.ACTION_VIEW , uri );
            if ( intent.resolveActivity ( getActivity ( ).getPackageManager ( ) ) != null )
                startActivity ( intent );
        } );
    }

    private void searchScreenCalling ( MotionEvent motionEvent ) {
        if ( motionEvent.getAction ( ) == ACTION_DOWN ) {
            (( OnSearchScreenCall ) getActivity ( )).callSearchScreen ( );
        }
    }

    private void initAdapter ( ) {
        //В конструктор адаптера передается колбэк на выбор рисунка в галерее.
        //Вызывается экран детального изображения риснука согласно переданной из адаптера позиции
        adapter = new GalleryAdapter ( new HitDiffUtilCallback ( ) ,
                this::detailScreenCalling );
        recyclerView.setAdapter ( adapter );
    }

    private void detailScreenCalling ( int position ) {
        if ( adapter.getCurrentList ( ).get ( position ) != null ) {
            viewModel.setCurrentIndex ( position );
            detailScreenCall ( );
        }
    }

    private void initRepeatButton ( ) {
        repeatButton = getActivity ( ).findViewById ( R.id.galleryRepeatActionButton );
    }

    private void initViewModel ( ) {
        viewModel = ViewModelProviders.of ( getActivity ( ) ).get ( GalleryViewModel.class );
        viewModel.onViewCreated ( );
    }

    //Отслеживание изменений
    private void observe ( ) {

        //Установка текста в поле поиска.
        viewModel.searchQuery.observe ( getViewLifecycleOwner ( ) , this::observeSearchQuery );

        //Установка действий на ошибки при загрузке страниц
        viewModel.loadingState.observe ( getViewLifecycleOwner ( ) , this::observeLoadingStates );

        //Передача данных для адаптера
        viewModel.pagedList.observe ( getViewLifecycleOwner ( ) , hits ->
                adapter.submitList ( hits ) );

        //Перемотка на начало в случае получения новых данных
        viewModel.isNewDataSet.observe ( getViewLifecycleOwner ( ) , this::resetScrolling );
    }

    //Отображается либо текст запроса, либо слово "Поиск", если запрос пустой
    private void observeSearchQuery ( String searchQuery ) {
        if ( !searchQuery.equals ( "" ) )
            searchEditText.setText ( searchQuery );
        else
            searchEditText.setText ( getString ( R.string.search ) );
    }

    private void observeLoadingStates ( LoadingState loadingState ) {
        //Если не удалась первая загрузка
        if ( loadingState == LoadingState.FIRST_LOAD_ERROR ) reactFirstLoadError ( );

            //Если не удалась загрузка очередной страницы
        else if ( loadingState == LoadingState.LOAD_ERROR ) reactLoadError ( );

            //Если подходящих картинок не найдено
        else if ( loadingState == LoadingState.EMPTY_DATA ) reactEmptyData ( );
    }


    //Вывести ошибку и дать возможность перезагрузки первой страницы
    private void reactFirstLoadError ( ) {
        (( OnMessage ) getActivity ( )).showMessage (
                getString ( R.string.error ) ,
                getString ( R.string.noConnection ) ,

                //после закрытия диалогового окна - перезагрузка
                this::firstReload );
    }

    //Вывести ошибку и перезагрузить
    private void reactLoadError ( ) {
        (( OnMessage ) getActivity ( )).showMessage (
                getString ( R.string.error ) ,
                getString ( R.string.noConnection ) ,
                this::reload );
    }

    //Предложить установить другие условия поиска
    private void reactEmptyData ( ) {
        (( OnMessage ) getActivity ( )).showMessage (
                getString ( R.string.nothingFound ) ,
                getString ( R.string.tryOtherSearchParameters ) ,
                null );
    }

    //Сброс установок адаптера для возможности скроллинга сначала
    private void resetScrolling ( Boolean isNewDataSet ) {
        if ( isNewDataSet ) {
            adapter.setStateRestorationPolicy (
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT );
            adapter.setStateRestorationPolicy (
                    RecyclerView.Adapter.StateRestorationPolicy.ALLOW );
        }
    }

    public void firstReload ( ) {
        //Показать кнопку перезагрузки
        repeatButton.show ( );
        //При нажатии на кнопку перезагрузки сбросить слушатели,
        // получить новые данные, скрыть кнопку
        repeatButton.setOnClickListener ( view -> {
            removeAllObservers ( );
            viewModel.firstReload ( );
            observe ( );
            repeatButton.hide ( );
        } );
    }

    public void reload ( ) {

        repeatButton.show ( );
        //При нажатии на кнопку перезагрузки получить новые данные, скрыть кнопку
        repeatButton.setOnClickListener ( view -> {
            viewModel.reload ( );
            repeatButton.hide ( );
        } );
    }

    //Удаление слушателей для перезагрузки
    public void removeAllObservers ( ) {
        viewModel.searchQuery.removeObservers ( getViewLifecycleOwner ( ) );
        viewModel.loadingState.removeObservers ( getViewLifecycleOwner ( ) );
        viewModel.pagedList.removeObservers ( getViewLifecycleOwner ( ) );
        viewModel.isNewDataSet.removeObservers ( getViewLifecycleOwner ( ) );
    }

    public void detailScreenCall ( ) {
        (( OnDetailScreenCall ) getActivity ( )).callDetailScreen ( );
    }

    @Override
    public void onDestroy ( ) {
        super.onDestroy ( );
        //Сохранить данные при замене фрагмента или повороте экрана
        if ( isTopFragment ( ) ) {
            viewModel.saveData ( );
            adapter.onDestroy ( );
        }
    }

    private boolean isTopFragment ( ) {
        return getTopFragment ( ) == this;
    }

    private Fragment getTopFragment ( ) {
        return getFragments ( ).get ( getFragments ( ).size ( ) - 1 );
    }

    @NotNull
    private List<Fragment> getFragments ( ) {
        return getActivity ( ).getSupportFragmentManager ( ).getFragments ( );
    }

}
