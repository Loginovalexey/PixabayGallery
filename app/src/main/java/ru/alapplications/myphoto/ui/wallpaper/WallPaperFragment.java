package ru.alapplications.myphoto.ui.wallpaper;

import android.app.WallpaperManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.ui.OnMessage;

import static ru.alapplications.myphoto.ui.detail.viewmodel.FileHandler.FILE_FOR_WALL_PAPER;


/**
 * Класс для действий по установке обоев
 */

public class WallPaperFragment extends Fragment {

    CropImageView cropImageView;
    FrameLayout   cropFrame;
    //Флаг установки пропорций обрезки
    private boolean isSetScreenRatio = false;
    private boolean isRotated        = false;

    public static WallPaperFragment newInstance ( ) {
        return new WallPaperFragment ( );
    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        return inflater.inflate ( R.layout.fragment_wall_paper , container , false );
    }

    @Override
    public void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );
        initCropImage ( );
        initRotateTool ( view );
        initRatioTool ( view );
        initCropTool ( view );
        initWallPaperTool ( view );
        startMessage ( );
    }

    private void startMessage ( ) {
        (( OnMessage ) getActivity ( )).showMessage (
                getString ( R.string.settingWallpaper ) ,
                getString ( R.string.cropImage ) ,
                null );
    }

    private void initCropImage ( ) {
        cropFrame = getActivity ( ).findViewById ( R.id.cropFrame );
        cropImageView = getActivity ( ).findViewById ( R.id.cropImageView );
        //Загрузка изображения из сохраненного файла
        Uri uri = Uri.parse ( "file://" + getActivity ( ).getExternalCacheDir ( ) + "/" +
                FILE_FOR_WALL_PAPER );
        cropImageView.setImageUriAsync ( uri );
        cropImageView.setOnSetImageUriCompleteListener ( ( view , uri1 , error ) -> {
            //Определение границ изображения для корректного отображения фона
            cropImageView.setBackground (
                    getResources ( )
                            .getDrawable ( R.drawable.pyramidbitmap ) );
            cropImageView.setLayoutParams (
                    LayoutParamsSetter.setPlacementLayoutParameters (
                            cropFrame , cropImageView , false ) );

        } );
//        //При обрезке установить обновленное(обрезанное) изображение
        cropImageView.setOnCropImageCompleteListener ( ( view1 , result ) -> {
                    isRotated = false;
                    cropImageView.setImageBitmap ( result.getBitmap ( ) );
                    cropImageView.setLayoutParams (
                            LayoutParamsSetter.setPlacementLayoutParameters (
                                    cropFrame , cropImageView , false ));
                }
        );
        //Установка пропорций обрезки
        setRatio ( );
    }


    //Установка действий при нажатии на кнопку вращения
    private void initRotateTool ( @NonNull View view ) {
        view.findViewById ( R.id.rotate ).setOnClickListener ( viewRotate -> {
            cropImageView.rotateImage ( 90 );
            isRotated = !isRotated;
            cropImageView.setLayoutParams (
                    LayoutParamsSetter.setPlacementLayoutParameters (
                            cropFrame , cropImageView , isRotated ) );
        } );
    }

    //Установка действий при нажатии на кнопку пропорций
    private void initRatioTool ( @NonNull View view ) {
        view.findViewById ( R.id.setRatio ).setOnClickListener ( viewRatio -> {
            isSetScreenRatio = !isSetScreenRatio;
            setRatio ( );
        } );
    }

    //Установка действий при нажатии на кнопку обрезки
    private void initCropTool ( @NonNull View view ) {
        view.findViewById ( R.id.сrop ).setOnClickListener ( viewCrop ->
                cropImageView.getCroppedImageAsync ( ) );
    }

    //Установка действий при нажатии на кнопку установки обоев
    private void initWallPaperTool ( @NonNull View view ) {
        view.findViewById ( R.id.setWallPaper ).setOnClickListener ( viewWall -> setWallPaper ( ) );
    }


    private void setRatio ( ) {
        if ( isSetScreenRatio ) {
            //Установка пропорций в соответствии с размерами экрана
            Display display = getActivity ( ).getWindowManager ( ).getDefaultDisplay ( );
            DisplayMetrics metrics = new DisplayMetrics ( );
            display.getMetrics ( metrics );
            cropImageView.setAspectRatio ( metrics.widthPixels , metrics.heightPixels );
        } else
            cropImageView.clearAspectRatio ( );
    }

    //Установка обоев
    public void setWallPaper ( ) {
        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance ( getActivity ( ).getApplicationContext ( ) );
        try {
            wallpaperManager.setBitmap ( cropImageView.getCroppedImage ( ) );
            (( OnMessage ) getActivity ( )).showMessage (
                    "" , getActivity ( ).getResources ( ).getString ( R.string.wallpaperSet ) , null );
        } catch (IOException e) {
            (( OnMessage ) getActivity ( )).showMessage (
                    "" , getActivity ( ).getResources ( ).getString ( R.string.wallpaperNotSet ) , null );
        }
    }


}