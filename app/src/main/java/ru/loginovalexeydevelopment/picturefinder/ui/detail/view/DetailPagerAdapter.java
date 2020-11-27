package ru.loginovalexeydevelopment.picturefinder.ui.detail.view;

import android.content.Context;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.loginovalexeydevelopment.picturefinder.R;
import ru.loginovalexeydevelopment.picturefinder.model.entities.Hit;
import ru.loginovalexeydevelopment.picturefinder.ui.detail.viewmodel.OnChangeItem;
import ru.loginovalexeydevelopment.picturefinder.ui.detail.viewmodel.OnClickDetailImage;

public class DetailPagerAdapter extends PagerAdapter {

    private static final String PICASSO_LOAD_DETAIL_TAG = "itemDetail";

    /**
     * Список, используемый адаптером
     */
    private       List<Hit>          hits;
    /**
     * Колбэк при нажатии на изображение - должна отображаться/скрываться панель инструементов
     */
    private final OnClickDetailImage onClickDetailImage;
    /**
     * Колбэк дял установки актуального индекса при пролистывании страниц
     */
    private final OnChangeItem       onChangeItem;


    DetailPagerAdapter ( OnChangeItem onChangeItem ,
                         OnClickDetailImage onClickDetailImage ) {
        this.onClickDetailImage = onClickDetailImage;
        this.onChangeItem = onChangeItem;
    }

    public void setHits ( List<Hit> hits ) {
        this.hits = hits;
        notifyDataSetChanged ( );
    }

    @Override
    public int getCount ( ) {
        return hits.size ( );
    }

    @Override
    public boolean isViewFromObject ( @NonNull View view , @NonNull Object object ) {
        return view == object;
    }

    @Override
    public void setPrimaryItem ( @NotNull ViewGroup container , int position , @NotNull Object object ) {
        onChangeItem.setCurrentHit ( hits.get ( position ) );
    }

    //Действия при перелистывании на новую страницу
    @NonNull
    @Override
    public Object instantiateItem ( @NonNull ViewGroup container , int position ) {
        LayoutInflater inflater = ( LayoutInflater ) container
                .getContext ( )
                .getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        View itemView = inflater.inflate ( R.layout.item_detail , container , false );

        ProgressBar progressBar = itemView.findViewById ( R.id.detailProgressBar );
        progressBar.setVisibility ( View.VISIBLE );

        PhotoView detailImageView = itemView.findViewById ( R.id.detailImageView );
        detailImageView.setOnClickListener ( view -> onClickDetailImage.onClick ( ) );

        //Картинка фона - сетка для прозрачных изображений
        ImageView backImage = itemView.findViewById ( R.id.detailBackImageView );

        container.addView ( itemView );

        hits.get ( position ).setIsLoaded ( false );

        //Загрузка изображения по ссылке из поля LargeImageURL
        Picasso.get ( )
                .load ( hits.get ( position ).getLargeImageURL ( ) )
                .placeholder ( R.drawable.whitebackground )
                .error ( R.drawable.ic_outline_broken_image_24 )
                .tag ( PICASSO_LOAD_DETAIL_TAG )
                .into ( detailImageView , new Callback ( ) {
                    @Override
                    public void onSuccess ( ) {
                        progressBar.setVisibility ( View.GONE );
                        hits.get ( position ).setIsLoaded ( true );
                        putBackground ( backImage , detailImageView.getDisplayRect ( ) );
                        //При масштабировании основного изображения масштабируется и фон
                        detailImageView.setOnMatrixChangeListener ( rect ->
                                putBackground ( backImage , rect ) );
                    }

                    @Override
                    public void onError ( Exception e ) {
                        progressBar.setVisibility ( View.GONE );
                        //Запрет масштабирования изображения с ошибкой
                        detailImageView.setZoomable ( false );
                    }
                } );
        return itemView;
    }

    /**
     * Отображение фона в границах основного изображения
     */
    private void putBackground ( ImageView background , RectF rect ) {
        //Получение границ основного изображения
        FrameLayout.LayoutParams scaleLp = ( FrameLayout.LayoutParams ) background
                .getLayoutParams ( );
        scaleLp.width = ( int ) (rect.right - rect.left);
        scaleLp.height = ( int ) (rect.bottom - rect.top);
        //Установка таких же границ для фона
        background.setLayoutParams ( scaleLp );
    }


    @Override
    public void destroyItem ( @NonNull ViewGroup container , int position ,
                              @NonNull Object object ) {
        container.removeView ( ( View ) object );
    }

    protected void onDestroy ( ) {
        //Отмена загрузок изображений
        Picasso.get ( ).cancelTag ( PICASSO_LOAD_DETAIL_TAG );
    }
}

