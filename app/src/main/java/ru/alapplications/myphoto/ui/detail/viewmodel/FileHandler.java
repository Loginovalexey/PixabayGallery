package ru.alapplications.myphoto.ui.detail.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.app.App;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Класс для работы с файлами.
 */

public class FileHandler {

    public final static  String FILE_FOR_WALL_PAPER = "fileForWallPaper.jpg";
    private final static String FILE_FOR_SHARE      = "fileForShare.jpg";
    private final static String SHARE_INTENT        = "Share";

    private final CompositeDisposable compositeDisposable;

    @Inject
    App app;

    FileHandler ( ) {
        App.getAppComponent ( ).inject ( this );
        compositeDisposable = new CompositeDisposable ( );
    }

    /**
     * @return наличие или отсутствие прав на запись файлов
     */
    protected boolean isWritePermission ( ) {
        return ContextCompat.checkSelfPermission (
                app.getApplicationContext ( ) ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Создание директории для сохранения изображений
     *
     * @param directoryName - название директории
     * @return - директория
     */
    private File createPublicApplicationDirectory ( String directoryName ) {
        File file = new File ( Environment.getExternalStoragePublicDirectory (
                Environment.DIRECTORY_PICTURES ) , directoryName );
        file.mkdirs ( );
        return file;
    }

    /**
     * Сохранение изображения
     *
     * @param bitMap           - изображение в формате Bitmap
     * @param fileName         - название файла
     * @param onSaveFileResult - колбэк для обработки результатов
     */
    public void download ( Bitmap bitMap ,
                           String fileName ,
                           OnSaveFileResult onSaveFileResult ) {
        compositeDisposable.add (
                savePictureSingle (
                        createPublicApplicationDirectory ( app.getResources ( )
                                .getString ( R.string.appName ) ) ,
                        fileName ,
                        bitMap ,
                        Bitmap.CompressFormat.PNG )
                        .observeOn ( AndroidSchedulers.mainThread ( ) )
                        .subscribeOn ( Schedulers.io ( ) )
                        .subscribe (
                                result -> onSaveFileResult.onOk ( ) ,
                                throwable -> onSaveFileResult.onError ( )
                        ) );
    }

    /**
     * Сохранение изображения с заданными параметрами
     *
     * @param dir            - директория
     * @param fileName       - название файла
     * @param bitMap         - изображение в формате Bitmap
     * @param compressFormat - формат файла
     */

    private Single<File> savePictureSingle ( File dir ,
                                             String fileName ,
                                             Bitmap bitMap ,
                                             Bitmap.CompressFormat compressFormat ) {

        return Single.create ( emitter -> {
            try {
                File file = new File ( dir , fileName );
                FileOutputStream fileOutputStream = new FileOutputStream ( file );
                bitMap.compress ( compressFormat ,
                        100 ,
                        fileOutputStream );
                emitter.onSuccess ( file );
            } catch (FileNotFoundException e) {
                emitter.onError ( e );
            }
        } );
    }

    /**
     * Сохранение файла для перехода на страницу редактирования обоев
     *
     * @param bitmap           - изображение в формате Bitmap
     * @param onSaveFileResult - колбэк для обработки результата
     */
    protected void setWallPaper ( Bitmap bitmap , OnSaveFileResult onSaveFileResult ) {

        compositeDisposable.add (
                savePictureSingle (
                        app.getExternalCacheDir ( ) ,
                        FILE_FOR_WALL_PAPER ,
                        bitmap ,
                        Bitmap.CompressFormat.PNG )
                        .observeOn ( AndroidSchedulers.mainThread ( ) )
                        .subscribeOn ( Schedulers.io ( ) )
                        .subscribe (
                                result -> onSaveFileResult.onOk ( ) ,
                                throwable -> onSaveFileResult.onError ( )
                        ) );
    }

    /**
     * Метод предоставляет возможность поделиться изображением
     */
    public void share ( Bitmap bitmap , OnSaveFileResult onSaveFileResult ) {
        compositeDisposable.add ( savePictureSingle (
                app.getExternalCacheDir ( ) ,
                FILE_FOR_SHARE ,
                bitmap ,
                Bitmap.CompressFormat.JPEG )
                .observeOn ( AndroidSchedulers.mainThread ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        this::callShareActivity ,
                        throwable -> onSaveFileResult.onError ( )
                ) );
    }

    private void callShareActivity ( File result ) {
        Intent shareIntent = new Intent ( Intent.ACTION_SEND );
        shareIntent.setType ( "image/*" );
        shareIntent.putExtra ( Intent.EXTRA_STREAM , Uri.fromFile ( result ) );
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder ( );
        StrictMode.setVmPolicy ( builder.build ( ) );
        app.startActivity ( Intent.createChooser ( shareIntent , SHARE_INTENT )
                .addFlags ( FLAG_ACTIVITY_NEW_TASK ) );
    }

    //Отмена выполняемых методов
    protected void onCleared ( ) {
        if ( !compositeDisposable.isDisposed ( ) ) {
            compositeDisposable.dispose ( );
        }
    }
}
