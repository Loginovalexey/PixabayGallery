package ru.alapplications.myphoto.ui.detail.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

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
import ru.alapplications.myphoto.app.App;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.ui.OnMessage;
import ru.alapplications.myphoto.ui.OnWallPaperScreenCall;
import ru.alapplications.myphoto.ui.gallery.viewmodel.GalleryViewModel;
import ru.alapplications.myphoto.ui.gallery.viewmodel.LoadingState;



public class DetailFragment extends Fragment implements
        OnClickDetailScreen,
        OnChangeImageView,
        OnLoadingResult {
    private HackyProblematicPager detailViewPager;
    private DetailPagerAdapter    detailPagerAdapter;
    private LinearLayout          downLayout;
    private boolean               isDownLayoutVisible;
    private boolean               isDownLayoutEnabled;
    private OnMessage             onMessage;
    private OnWallPaperScreenCall onWallPaperScreenCall;
    private CompositeDisposable   compositeDisposable;
    private Hit                   currentHit;
    private String           TAG;
    private GalleryViewModel viewModel;
    private boolean          isClickabale;

    private final static String FILE_FOR_SAVE = "fileForSave.jpg";
    private final static String FILE_FOR_SHARE      = "fileForShare.jpg";
    private final static String SHARE_INTENT        = "Share";
    public final static  String FILE_FOR_WALL_PAPER = "fileForWallPaper.jpg";


    public static DetailFragment getInstance ( ) {
        return new DetailFragment ( );
    }

    @Override
    public void onAttach ( @NonNull Context context ) {
        super.onAttach ( context );
        onMessage = ( OnMessage ) context;
        onWallPaperScreenCall = ( OnWallPaperScreenCall ) context;
        TAG = getString ( R.string.appName ) + " DetailFragment: ";
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        isClickabale = false;
        detailPagerAdapter = new DetailPagerAdapter ( this , this , this );
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
        viewModel = ViewModelProviders.of ( getActivity ( ) ).get ( GalleryViewModel.class );
        viewModel.onViewCreated ( );
        viewModel.pagedListLiveData.observe ( getViewLifecycleOwner ( ) , hits -> {
            detailPagerAdapter.setHits ( hits );
            Log.d ( App.TAG , "submitList ( hits )" );
            if ( detailViewPager.getAdapter ( ) == null ) {
                detailViewPager.setAdapter ( detailPagerAdapter );
                viewModel.getCurrentIndex ( ).observe ( getViewLifecycleOwner ( ) , index -> {
                    detailViewPager.setCurrentItem ( index );
                } );
            }

        } );

        downLayout = getActivity ( ).findViewById ( R.id.detailDownLayout );
        setDownLayoutVisibility ( );
        ImageView downloadImageView = getActivity ( ).findViewById ( R.id.downloadImageView );
        ImageView setWallPaperImageView = getActivity ( ).findViewById ( R.id.setWallPaperImageView );
        ImageView shareImageView = getActivity ( ).findViewById ( R.id.shareImageView );
        detailViewPager = getActivity ( ).findViewById ( R.id.detailViewPager );
        isClickabale = false;
        downloadImageView.setOnClickListener ( view1 -> downLoad ( ) );
        setWallPaperImageView.setOnClickListener ( view1 -> setWallPaper ( ) );
        shareImageView.setOnClickListener ( view1 -> share ( ) );

    }


    public void updateView ( List<Hit> hits , Integer currentIndex ) {
        //detailPagerAdapter.setHits ( hits );
        if ( detailViewPager.getAdapter ( ) == null ) {
            detailViewPager.setAdapter ( detailPagerAdapter );
        }
        detailViewPager.setCurrentItem ( currentIndex );
    }

    public boolean showMessage ( String title , String message ) {
        onMessage.showMessage ( title , message , null );
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
        if (currentHit.getIsLoaded ()) {
            final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
            if ( ContextCompat.checkSelfPermission ( getActivity ( ) ,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE )
                    != PackageManager.PERMISSION_GRANTED ) {
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
            } else {
                savePictureToStorage ( );
            }
        }
    }

    private File getPublicAlbumStorageDir ( String albumName ) {
        File file = new File ( Environment.getExternalStoragePublicDirectory (
                Environment.DIRECTORY_PICTURES ) , albumName );
        file.mkdirs ( );
        return file;
    }


    public void savePictureToStorage ( ) {
        compositeDisposable.add ( getSingleSavedFile ( getPublicAlbumStorageDir ( getString ( R.string.appName ) ) ,
                currentHit.getId ( ) + ".jpg" )
                .observeOn ( AndroidSchedulers.mainThread ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        result -> showMessage ( "" , getString ( R.string.imageSaved ) ) ,
                        throwable -> showMessage ( "" , getString ( R.string.failedUpload ) )
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
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
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
        if (currentHit.getIsLoaded ()) {
            compositeDisposable.add ( getSingleSavedFile ( getActivity ( ).getExternalCacheDir ( ) ,
                    FILE_FOR_WALL_PAPER)
                    .observeOn ( AndroidSchedulers.mainThread ( ) )
                    .subscribeOn ( Schedulers.io ( ) )
                    .subscribe (
                            result -> {
                                onWallPaperScreenCall.callWallPaperScreen ( );
                                viewModel.setCurrentIndex ( detailViewPager.getCurrentItem ( ) );
                            } ,
                            throwable -> showMessage ( "" , getString ( R.string.wallpaperNotSet ) )
                    ) );
        }
    }


    private void share ( ) {
        if (currentHit.getIsLoaded ()) {
            compositeDisposable.add ( getSingleSavedFile ( getActivity ( ).getExternalCacheDir ( ) ,
                    FILE_FOR_SHARE)
                    .observeOn ( AndroidSchedulers.mainThread ( ) )
                    .subscribeOn ( Schedulers.io ( ) )
                    .subscribe (
                            result -> {
                                Intent shareIntent = new Intent ( Intent.ACTION_SEND );
                                shareIntent.setType ( "image/*" );
                                shareIntent.putExtra ( Intent.EXTRA_STREAM , Uri.fromFile ( result ) );
                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder ( );
                                StrictMode.setVmPolicy ( builder.build ( ) );
                                startActivity ( Intent.createChooser ( shareIntent , SHARE_INTENT ));
                            } ,
                            throwable -> showMessage ( "" , getString ( R.string.failedShare ) )
                    ) );
        }
    }



    private boolean isTopFragment ( ) {
        return (getActivity ( ).getSupportFragmentManager ( ).getFragments ( ).get (
                getActivity ( ).getSupportFragmentManager ( ).getFragments ( ).size ( ) - 1
        ) == this);
    }

    @Override
    public void onDestroy ( ) {
        super.onDestroy ( );
        if ( isTopFragment ( ) ) {
            Log.d ( App.TAG , "DetailFragment onDestroy" );
            Object tag = "detailImageView";
            Picasso.get ( ).cancelTag ( tag );
            viewModel.setCurrentIndex ( detailViewPager.getCurrentItem ( ) );
            if ( !compositeDisposable.isDisposed ( ) ) {
                compositeDisposable.dispose ( );
            }
        }
    }

    @Override
    public void setLoadingResult ( LoadingState loadState ) {
        if ( loadState == LoadingState.OK )
            isClickabale = true;
        else
            isClickabale = false;
    }
}

