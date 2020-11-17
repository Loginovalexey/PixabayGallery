package ru.alapplications.myphoto.ui.gallery.view;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.model.entities.Hit;

/**
 * Адаптер с поддержкой пагинации
 */

public class GalleryAdapter extends PagedListAdapter<Hit, GalleryAdapter.HitViewHolder> {

    private static final String PICASSO_LOAD_GALLERY_TAG = "itemGallery";

    private final OnItemClickListener onItemClickListener;

    /**
     * Интерфейс для обработки нажатий элементов списка
     * В колбэк передается позиция нажатого элемента
     */
    public interface OnItemClickListener {
        void onItemClick ( int position );
    }

    /**
     * @param diffUtilCallback    - объект для оптимизации отрисовки элементов
     * @param onItemClickListener - колбэк нажатия на элемент списка
     */
    public GalleryAdapter (
            DiffUtil.ItemCallback<Hit> diffUtilCallback ,
            OnItemClickListener onItemClickListener ) {
        super ( diffUtilCallback );
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    //Создание ViewHolder c установкой слушателя нажатия на элемент
    public HitViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from ( parent.getContext ( ) ).inflate (
                R.layout.item_hit_small , parent , false );
        HitViewHolder hitViewHolder = new HitViewHolder ( view );
        view.setOnClickListener ( setClickListenerByPosition ( hitViewHolder ) );
        return hitViewHolder;
    }

    @NotNull
    private View.OnClickListener setClickListenerByPosition ( HitViewHolder hitViewHolder ) {
        return it -> {
            int position = hitViewHolder.getAbsoluteAdapterPosition ( );
            if ( position != RecyclerView.NO_POSITION ) {
                onItemClickListener.onItemClick (
                        position );
            }
        };
    }

    //Запрет на переиспользование элементов
    @Override
    public int getItemViewType ( int position ) {
        return position;
    }

    @Override
    public void onBindViewHolder ( @NonNull HitViewHolder holder , int position ) {
        holder.bind ( getItem ( position ) );
    }

    static class HitViewHolder extends RecyclerView.ViewHolder {
        @SuppressWarnings("FieldMayBeFinal")
        private final ImageView   image;
        private final FrameLayout layout;


        HitViewHolder ( @NonNull final View itemView ) {
            super ( itemView );
            layout = itemView.findViewById ( R.id.itemGalleryLayout );
            image = itemView.findViewById ( R.id.itemGalleryImageView );

        }

        //Определение размеров размещения в зависимости от ориентации контейнера и рисунка
        private FrameLayout.LayoutParams definePlacementLayoutParameters (
                FrameLayout container , ImageView child ) {
            FrameLayout.LayoutParams childLayoutParams =
                    ( FrameLayout.LayoutParams ) child.getLayoutParams ( );

            double childRatio = ( double ) child.getDrawable ( ).getIntrinsicHeight ( ) /
                    child.getDrawable ( ).getIntrinsicWidth ( );
            double containerRatio = ( double ) container.getMeasuredHeight ( ) /
                    container.getMeasuredWidth ( );

            if ( childRatio > containerRatio ) {
                childLayoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
                childLayoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
            } else {
                childLayoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
                childLayoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            }
            return childLayoutParams;

        }

        private void bind ( Hit hit ) {

            //Размещение пустого элемента - до загрузки и в случае ошибки
            setEmptyImage ( );

            //Загрузка изображения из поля PreviewURL с помощью Picasso
            if ( hit != null ) {
                Picasso.get ( )
                        .load ( hit.getPreviewURL ( ) )
                        .placeholder ( R.drawable.none )
                        .tag ( PICASSO_LOAD_GALLERY_TAG )
                        //Обработка результата загрузки
                        .into ( image , new Callback ( ) {
                            @Override
                            public void onSuccess ( ) {
                                //Определить момент перед отрисовкой изображения
                                //для задания правильных размеров размещения
                                layout.getViewTreeObserver ( )
                                        .addOnPreDrawListener ( getPreDrawListener ( ) );

                            }

                            private OnPreDrawListener getPreDrawListener ( ) {
                                return new OnPreDrawListener ( ) {
                                    @Override
                                    //Определение параметров размещения в контейнере
                                    public boolean onPreDraw ( ) {
                                        image.setLayoutParams ( definePlacementLayoutParameters (
                                                layout , image ) );
                                        layout.getViewTreeObserver ( )
                                                .removeOnPreDrawListener ( this );
                                        //Установка фона в виде клеток, который виден на прозрачных
                                        //изображениях
                                        setTransparentBackground ( );
                                        return true;
                                    }
                                };
                            }

                            @Override
                            public void onError ( Exception e ) {
                                Log.e ( "Picasso" , "Load error" );
                            }
                        } );
            }


        }

        private void setEmptyImage ( ) {
            FrameLayout.LayoutParams startLayoutParams = new FrameLayout.LayoutParams (
                    ViewGroup.LayoutParams.MATCH_PARENT ,
                    ViewGroup.LayoutParams.MATCH_PARENT );
            startLayoutParams.gravity = Gravity.CENTER;
            image.setLayoutParams ( startLayoutParams );
            image.setBackground ( itemView.getResources ( )
                    .getDrawable ( R.drawable.simple_rect ) );
        }

        private void setTransparentBackground ( ) {
            image.setBackground ( itemView.getResources ( ).getDrawable (
                    R.drawable.pyramidbitmap ) );
        }
    }

    //При завершении работы прекратить все загрузки
    public void onDestroy ( ) {
        Picasso.get ( ).cancelTag ( PICASSO_LOAD_GALLERY_TAG );
    }
}
