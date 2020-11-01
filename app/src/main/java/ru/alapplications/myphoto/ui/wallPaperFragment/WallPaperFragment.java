package ru.alapplications.myphoto.ui.wallPaperFragment;

import android.app.WallpaperManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.ui.OnMessage;


public class WallPaperFragment extends Fragment {

    CropImageView cropImageView;
    private OnMessage onMessage;
    private boolean   ratio;

    public static WallPaperFragment newInstance ( ) {
        WallPaperFragment fragment = new WallPaperFragment ( );
        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        ratio = true;
    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {

        return inflater.inflate ( R.layout.fragment_wall_paper , container , false );
    }

    @Override
    public void onAttach ( @NonNull Context context ) {
        super.onAttach ( context );
        onMessage = ( OnMessage ) context;

    }

    @Override
    public void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );
        cropImageView = getActivity ( ).findViewById ( R.id.cropImageView );
        Uri uri = Uri.parse ( "file://" + getActivity ( ).getExternalCacheDir ( ) + "/" + getString ( R.string.fileForWallPaper ) );
        cropImageView.setImageUriAsync ( uri );
        cropImageView.setOnCropImageCompleteListener ( ( view1 , result )
                -> cropImageView.setImageBitmap ( result.getBitmap ( ) ) );


        view.findViewById ( R.id.toCropIageView ).setOnClickListener ( view12 -> cropImageView.getCroppedImageAsync ( ) );

        view.findViewById ( R.id.rotateImageView ).setOnClickListener ( view13 -> cropImageView.rotateImage ( 90 ) );

        view.findViewById ( R.id.setWallPaperImageView ).setOnClickListener ( view13 -> {
            setWallPaper ( );
        } );

        view.findViewById ( R.id.ratioImageView ).setOnClickListener ( view1 -> {
            ratio = !ratio;
            setRatio ( );
        } );


    }

    private void setRatio ( ) {
        if ( ratio ) {
            Display display = getActivity ( ).getWindowManager ( ).getDefaultDisplay ( );
            DisplayMetrics metrics = new DisplayMetrics ( );
            display.getMetrics ( metrics );
            cropImageView.setAspectRatio ( metrics.widthPixels , metrics.heightPixels );
        } else {
            cropImageView.clearAspectRatio ( );
        }
    }

    public void setWallPaper ( ) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance ( getActivity ( ).getApplicationContext ( ) );
        try {
            wallpaperManager.setBitmap ( cropImageView.getCroppedImage ( ) );
            onMessage.simpleMessage ( "" , getActivity ( ).getResources ( ).getString ( R.string.wallpaperSet ) );
        } catch (IOException e) {
            onMessage.simpleMessage ( "" , getActivity ( ).getResources ( ).getString ( R.string.wallpaperNotSet ) );
        }
    }


}