package ru.alapplications.myphoto.ui.detailFragment.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnViewDragListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.ui.OnMessage;
import ru.alapplications.myphoto.ui.OnWallPaperScreenCall;
import ru.alapplications.myphoto.ui.detailFragment.presenter.DetailPresenter;
import ru.alapplications.myphoto.ui.galleryFragment.view.GalleryFragment;
import ru.alapplications.myphoto.ui.galleryFragment.view.HitAdapter;

import static ru.alapplications.myphoto.ui.galleryFragment.view.HitAdapter.getLayoutParamsInContainer;


public class DetailFragment extends Fragment implements DetailView, OnClickDetailScreen, OnChangeImageView {
    private HackyProblematicPager detailViewPager;
    private DetailPagerAdapter    detailPagerAdapter;
    private DetailPresenter       detailPresenter;
    private LinearLayout          downLayout;
    private boolean               isDownLayoutVisible;
    private boolean               isDownLayoutEnabled;
    private OnMessage             onMessage;
    private OnWallPaperScreenCall onWallPaperScreenCall;
    private CompositeDisposable   compositeDisposable;
    private Hit                   currentHit;
    private String                TAG;


    public static DetailFragment newInstance ( ) {
        return new DetailFragment ( );
    }

    @Override
    public void onAttach ( @NonNull Context context ) {
        super.onAttach ( context );
        onMessage = ( OnMessage ) context;
        onWallPaperScreenCall = ( OnWallPaperScreenCall ) context;
        TAG = getString ( R.string.app_name ) + " DetailFragment: ";
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        detailPagerAdapter = new DetailPagerAdapter ( this , this );
        detailPresenter = new DetailPresenter ( this );
        compositeDisposable = new CompositeDisposable ( );
        isDownLayoutVisible = true;
        isDownLayoutEnabled = false;
    }


    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        return inflater.inflate ( R.layout.fragment_detail , container , false );
    }

    @Override
    public void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );
        super.onCreate ( savedInstanceState );

        downLayout = getActivity ( ).findViewById ( R.id.detailDownLayout );
        setDownLayoutVisibility ( );
        ImageView downloadImageView = getActivity ( ).findViewById ( R.id.downloadImageView );
        ImageView setWallPaperImageView = getActivity ( ).findViewById ( R.id.setWallPaperImageView );
        ImageView shareImageView = getActivity ( ).findViewById ( R.id.shareImageView );

        detailViewPager = getActivity ( ).findViewById ( R.id.detailViewPager );

        downloadImageView.setOnClickListener ( view1 -> downLoad ( ) );
        setWallPaperImageView.setOnClickListener ( view1 -> setWallPaper ( ) );
        shareImageView.setOnClickListener ( view1 -> share ( ) );
        detailPresenter.onViewCreated ( );
    }

    @Override
    public void updateView ( List<Hit> hits , Integer currentIndex ) {
        detailPagerAdapter.setHits ( hits );
        if ( detailViewPager.getAdapter ( ) == null ) {
            detailViewPager.setAdapter ( detailPagerAdapter );
        }
        detailViewPager.setCurrentItem ( currentIndex );
    }

    @Override
    public boolean showMessage ( String title , String message ) {
        onMessage.simpleMessage ( title , message );
        return true;
    }

    @Override
    public void setCurrentHit ( Hit currentHit ) {
        this.currentHit = currentHit;
    }

    private void setDownLayoutVisibility ( ) {
        if ( isDownLayoutVisible )
            downLayout.setVisibility ( View.VISIBLE );
        else
            downLayout.setVisibility ( View.GONE );
    }

    @Override
    public void click ( ) {
        isDownLayoutVisible = !isDownLayoutVisible;
        setDownLayoutVisibility ( );
    }

    public void downLoad ( ) {
        final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
        if ( ContextCompat.checkSelfPermission ( getActivity ( ) ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED ) {
            if ( ActivityCompat.shouldShowRequestPermissionRationale (
                    getActivity ( ) ,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE ) ) {
                Toast.makeText ( getActivity ( ) ,
                        R.string.callRequestPermissionsAgain ,
                        Toast.LENGTH_LONG ).show ( );
            }
            ActivityCompat.requestPermissions ( getActivity ( ) ,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} ,
                    REQUEST_WRITE_EXTERNAL_STORAGE );
        } else {
            savePictureToStorage ( );
        }
    }

    private File getPublicAlbumStorageDir ( String albumName ) {
        File file = new File ( Environment.getExternalStoragePublicDirectory (
                Environment.DIRECTORY_PICTURES ) , albumName );
        file.mkdirs ( );
        return file;
    }


    public void savePictureToStorage ( ) {
        compositeDisposable.add ( getSingleSavedFile ( getPublicAlbumStorageDir ( getString ( R.string.app_name ) ) ,
                currentHit.getId ( ) + ".jpg" )
                .observeOn ( AndroidSchedulers.mainThread ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        result -> showMessage ( "" , getString ( R.string.imageSaved ) ) ,
                        throwable -> showMessage ( "" , getString ( R.string.imageNotSaved ) )
                ) );
    }

    private View getCurrentView ( ViewPager viewPager ) {
        try {
            final int currentItem = viewPager.getCurrentItem ( );
            for (int i = 0; i < viewPager.getChildCount ( ); i++) {
                final View child = viewPager.getChildAt ( i );
                final ViewPager.LayoutParams layoutParams = ( ViewPager.LayoutParams ) child.getLayoutParams ( );
                Field f = layoutParams.getClass ( ).getDeclaredField ( "position" ); //NoSuchFieldException
                f.setAccessible ( true );
                int position = ( Integer ) f.get ( layoutParams ); //IllegalAccessException
                if ( !layoutParams.isDecor && currentItem == position ) {
                    return child;
                }
            }
        } catch (NoSuchFieldException e) {
            Log.e ( TAG , e.toString ( ) );
        } catch (IllegalArgumentException e) {
            Log.e ( TAG , e.toString ( ) );
        } catch (IllegalAccessException e) {
            Log.e ( TAG , e.toString ( ) );
        }
        return null;
    }

    private ImageView getImageViewFromCurrentPage ( ) {
        View container = getCurrentView ( detailViewPager );
        return container.findViewById ( R.id.detailImageView );
    }

    private Bitmap getBitMapFromCurrentPage ( ) {
        return (( BitmapDrawable ) getImageViewFromCurrentPage ( ).getDrawable ( )).getBitmap ( );
    }

    private Single<File> getSingleSavedFile ( File dir , String fileName ) {

        return Single.create ( emitter -> {
            try {
                File file = new File ( dir , fileName );
                FileOutputStream fileOutputStream = new FileOutputStream ( file );
                getBitMapFromCurrentPage ( ).compress ( Bitmap.CompressFormat.JPEG , 100 ,
                        fileOutputStream );
                emitter.onSuccess ( file );
            } catch (FileNotFoundException e) {
                emitter.onError ( e );
            }
        } );
    }

    private void setWallPaper ( ) {
        compositeDisposable.add ( getSingleSavedFile ( getActivity ( ).getExternalCacheDir ( ) ,
                getString ( R.string.fileForWallPaper ) )
                .observeOn ( AndroidSchedulers.mainThread ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        result -> {
                            onWallPaperScreenCall.callWallPaper ( );
                        } ,
                        throwable -> showMessage ( "" , getString ( R.string.imageNotSaved ) )
                ) );
    }


    private void share ( ) {
        compositeDisposable.add ( getSingleSavedFile ( getActivity ( ).getExternalCacheDir ( ) ,
                getString ( R.string.fileForShare ) )
                .observeOn ( AndroidSchedulers.mainThread ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        result -> {
                            Intent shareIntent = new Intent ( Intent.ACTION_SEND );
                            shareIntent.setType ( "image/*" );
                            shareIntent.putExtra ( Intent.EXTRA_STREAM , Uri.fromFile ( result ) );
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder ( );
                            StrictMode.setVmPolicy ( builder.build ( ) );
                            startActivity ( Intent.createChooser ( shareIntent , getString ( R.string.share ) ) );
                        } ,
                        throwable -> showMessage ( "" , getString ( R.string.imageNotSaved ) )
                ) );
    }

    private static class DetailPagerAdapter extends PagerAdapter {

        private List<Hit>           hits;
        private OnClickDetailScreen onClickDetailScreen;
        private OnChangeImageView   onPrimaryItem;

        private DetailPagerAdapter ( OnChangeImageView onPrimaryItem , OnClickDetailScreen onClickDetailScreen ) {
            this.onClickDetailScreen = onClickDetailScreen;
            this.onPrimaryItem = onPrimaryItem;
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
            detailImageView.setOnClickListener ( view1 -> onClickDetailScreen.click ( ) );

            FrameLayout detailFrameLayout = itemView.findViewById ( R.id.detailFrameLayout );


            container.addView ( itemView );

            Picasso.get ( )
                    .load ( hits.get ( position ).getLargeImageURL ( ) )
                    .placeholder ( R.drawable.placeholder )
                    .tag ( "detailImageView" )
                    .into ( detailImageView , new Callback ( ) {
                        @Override
                        public void onSuccess ( ) {
                            detailProgressBar.setVisibility ( View.GONE );
                            detailFrameLayout.getViewTreeObserver ().addOnPreDrawListener ( new ViewTreeObserver.OnPreDrawListener ( ) {
                                @Override
                                public boolean onPreDraw ( ) {
                                    detailImageView.setLayoutParams ( getLayoutParamsInContainer ( detailFrameLayout,detailImageView ));
                                    detailFrameLayout.getViewTreeObserver ().removeOnPreDrawListener ( this );
                                    return true;
                                }
                            } );
                        }

                        @Override
                        public void onError ( Exception e ) {
                            detailProgressBar.setVisibility ( View.GONE );
                        }
                    } );
            return itemView;
        }

        @Override
        public void destroyItem ( @NonNull ViewGroup container , int position ,
                                  @NonNull Object object ) {
            container.removeView ( ( View ) object );
        }
    }

    @Override
    public void onDestroy ( ) {
        super.onDestroy ( );
        Object tag = "detailImageView";
        Picasso.get().cancelTag(tag);
        if ( !compositeDisposable.isDisposed ( ) ) {
            compositeDisposable.dispose ( );
        }
    }
}

