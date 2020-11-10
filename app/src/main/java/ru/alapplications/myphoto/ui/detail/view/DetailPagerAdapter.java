package ru.alapplications.myphoto.ui.detail.view;

import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.ui.gallery.viewmodel.LoadingState;

public class DetailPagerAdapter extends PagerAdapter {

    private List<Hit>           hits;
    private OnClickDetailScreen onClickDetailScreen;
    private OnChangeImageView   onPrimaryItem;
    private OnLoadingResult onLoadingResult;

    DetailPagerAdapter ( OnChangeImageView onPrimaryItem ,
                         OnClickDetailScreen onClickDetailScreen,
                         OnLoadingResult onLoadingResult
    ) {
        this.onClickDetailScreen = onClickDetailScreen;
        this.onPrimaryItem = onPrimaryItem;
        this.onLoadingResult = onLoadingResult;
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
    public void setPrimaryItem ( ViewGroup container , int position , Object object ) {
        onPrimaryItem.setCurrentHit ( hits.get ( position ) );
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Object instantiateItem ( @NonNull ViewGroup container , int position ) {
        LayoutInflater inflater = ( LayoutInflater ) container.getContext ( )
                .getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        View itemView = inflater.inflate ( R.layout.item_detail , container , false );
        ProgressBar detailProgressBar = itemView.findViewById ( R.id.detailProgressBar );
        detailProgressBar.setVisibility ( View.VISIBLE );
        PhotoView detailImageView = itemView.findViewById ( R.id.detailImageView );
        ImageView backImage = itemView.findViewById ( R.id.detailBackImageView );
        detailImageView.setOnClickListener ( view1 -> onClickDetailScreen.click ( ) );
        container.addView ( itemView );

        onLoadingResult.setLoadingResult ( LoadingState.WAITING );
        hits.get ( position ).setIsLoaded ( false );
        Picasso.get ( )
                .load ( hits.get ( position ).getLargeImageURL ( ) )
                .placeholder ( R.drawable.whitebackground)
                .error ( R.drawable.ic_outline_broken_image_24 )
                .tag ( "detailImageView" )
                .into ( detailImageView , new Callback ( ) {
                    @Override
                    public void onSuccess ( ) {
                        detailProgressBar.setVisibility ( View.GONE );
                        hits.get ( position ).setIsLoaded ( true );
                        putBackground ( backImage, detailImageView.getDisplayRect ());
                        detailImageView.setOnMatrixChangeListener ( rect -> putBackground ( backImage,rect ) );
                    }

                    @Override
                    public void onError ( Exception e ) {
                        detailProgressBar.setVisibility ( View.GONE );
                        FrameLayout.LayoutParams lp = ( FrameLayout.LayoutParams ) detailImageView.getLayoutParams ( );
                        lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
                        lp.height = FrameLayout.LayoutParams.MATCH_PARENT;
                        detailImageView.setLayoutParams ( lp );
                        detailImageView.setZoomable ( false );
                        hits.get ( position ).setIsLoaded ( false );
                    }
                } );
        return itemView;
    }

    private void putBackground(ImageView background, RectF rect){
        FrameLayout.LayoutParams scaleLp1 = ( FrameLayout.LayoutParams ) background.getLayoutParams ( );
        scaleLp1.width = (int)(rect.right-rect.left);
        scaleLp1.height = (int)(rect.bottom-rect.top);
        background.setLayoutParams ( scaleLp1 );
    }



    @Override
    public void destroyItem ( @NonNull ViewGroup container , int position ,
                              @NonNull Object object ) {
        container.removeView ( ( View ) object );
    }
}

