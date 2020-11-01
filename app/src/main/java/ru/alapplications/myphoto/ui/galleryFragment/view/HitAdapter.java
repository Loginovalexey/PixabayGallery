package ru.alapplications.myphoto.ui.galleryFragment.view;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.model.entities.Hit;

public class HitAdapter extends PagedListAdapter<Hit, HitAdapter.HitViewHolder> {

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick ( int position );
    }

    public static FrameLayout.LayoutParams getLayoutParamsInContainer ( FrameLayout container , ImageView child ) {
        FrameLayout.LayoutParams lp = ( FrameLayout.LayoutParams ) child.getLayoutParams ( );
        lp.gravity = Gravity.CENTER;
        double imageRatio = ( double ) child.getDrawable ( ).getIntrinsicHeight ( ) / child.getDrawable ( ).getIntrinsicWidth ( );
        double frameRatio = ( double ) container.getMeasuredHeight ( ) / container.getMeasuredWidth ( );

        if ( imageRatio > frameRatio ) {
            lp.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            lp.height = FrameLayout.LayoutParams.MATCH_PARENT;
        } else {
            lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
            lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        }
        return lp;

    }

    public HitAdapter ( DiffUtil.ItemCallback<Hit> diffUtilCallback , OnItemClickListener onItemClickListener ) {
        super ( diffUtilCallback );
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HitViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.item_hit_small , parent , false );
        HitViewHolder h = new HitViewHolder ( view );
        view.setOnClickListener ( it -> {
            int adapterPosition = h.getAbsoluteAdapterPosition ();
            if (adapterPosition != RecyclerView.NO_POSITION){
                onItemClickListener.onItemClick (
                        adapterPosition );
            }

        });
        return h;
    }

    @Override
    public void onBindViewHolder ( @NonNull HitViewHolder holder , int position ) {
        holder.bind ( getItem ( position ) );

    }


    class HitViewHolder extends RecyclerView.ViewHolder {
        private ImageView   picture;
        private FrameLayout itemLayout;


        HitViewHolder ( @NonNull final View itemView ) {
            super ( itemView );

            itemLayout = itemView.findViewById ( R.id.itemLayout );
            picture = itemView.findViewById ( R.id.itemImage );

        }

        private void bind ( Hit hit ) {
            FrameLayout.LayoutParams lp = ( FrameLayout.LayoutParams ) picture.getLayoutParams ();
            lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
            lp.height = FrameLayout.LayoutParams.MATCH_PARENT;
            picture.setBackground ( itemView.getResources ().getDrawable ( R.drawable.simple_rect ));
            if (hit!=null) {
//                picture.setImageDrawable ( itemView.getResources ().getDrawable ( R.drawable.ic_action_name));
//                picture.setBackground ( itemView.getResources ().getDrawable ( R.drawable.whitebackground ));
//                picture.setLayoutParams ( new FrameLayout.LayoutParams ( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
                //picture.setBackground ( itemView.getResources ( ).getDrawable ( R.drawable.whitebackground ) );
//                picture.setLayoutParams ( new FrameLayout.LayoutParams ( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT ) );
//                picture.setBackground ( itemView.getResources ().getDrawable ( R.drawable.simple_rect ));
                Picasso.get ( )
                        .load ( hit.getPreviewURL ( ) )
                        .placeholder ( R.drawable.none )
                        .error ( R.drawable.ic_outline_broken_image_24 )
                        .tag ( "imageView" )
                        .into ( picture , new Callback ( ) {
                            @Override
                            public void onSuccess ( ) {

                                picture.setBackground ( itemView.getResources ( ).getDrawable ( R.drawable.pyramidbitmap ) );
                                itemLayout.getViewTreeObserver ( ).addOnPreDrawListener ( new ViewTreeObserver.OnPreDrawListener ( ) {
                                    @Override
                                    public boolean onPreDraw ( ) {
//                                        Log.d(App.TAG,"onPre");
//                                        Log.d( App.TAG, "itemLayout.getMeasuredHeight ( )"+itemLayout.getMeasuredHeight ( )+"itemLayout.getMeasuredWidth ( )"+itemLayout.getMeasuredWidth ( ));
                                        picture.setLayoutParams ( getLayoutParamsInContainer ( itemLayout , picture ) );
                                        itemLayout.getViewTreeObserver ( ).removeOnPreDrawListener ( this );

                                        return true;
                                    }
                                } );

                            }

                            @Override
                            public void onError ( Exception e ) {

                            }
                        } );
            } else {
                picture.setImageDrawable ( itemView.getResources ().getDrawable ( R.drawable.none ));
            }
        }
    }

    public void onDestroy(){
        Object tag = "imageView";
        Picasso.get().cancelTag(tag);
    }
}
