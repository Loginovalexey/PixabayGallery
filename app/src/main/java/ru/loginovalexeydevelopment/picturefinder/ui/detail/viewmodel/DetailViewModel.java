package ru.loginovalexeydevelopment.picturefinder.ui.detail.viewmodel;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.loginovalexeydevelopment.picturefinder.R;
import ru.loginovalexeydevelopment.picturefinder.app.App;
import ru.loginovalexeydevelopment.picturefinder.model.Model;
import ru.loginovalexeydevelopment.picturefinder.model.entities.Hit;


@SuppressWarnings("ALL")
public class DetailViewModel extends ViewModel {

    public final     MutableLiveData<List<Hit>>            hits;
    public final     MutableLiveData<Integer>              currentIndex;
    public final     MutableLiveData<Boolean>              requestPermission;
    public final     MutableLiveData<Boolean>              wallPaperScreenCall;
    public final     MutableLiveData<Pair<String, String>> message;
    private final    FileHandler                           fileHandler;
    private volatile boolean                               isRecording = false;

    @Inject
    App app;

    @Inject
    Model model;


    @SuppressLint("CheckResult")
    public DetailViewModel ( ) {

        App.getAppComponent ( ).inject ( this );
        hits = new MutableLiveData<> ( );
        currentIndex = new MutableLiveData<> ( );
        requestPermission = new MutableLiveData<> ( false );
        message = new MutableLiveData<> ( );
        wallPaperScreenCall = new MutableLiveData<> ( );
        fileHandler = new FileHandler ( );
    }

    /**
     * Установка начального состояния
     */
    public void onViewCreated ( ) {
        initState ( );
    }

    //Передача списка изображений и актуального индекса
    private void initState ( ) {
        hits.setValue ( model.getLoadedHits ( ) );
        currentIndex.setValue ( model.getCurrentIndex ( ) );
    }

    /**
     * Скачивание изображения
     */
    public void downLoad (
            int currentIndex ,
            Bitmap bitmap ) {
        setCurrentIndex ( currentIndex );
        if ( fileHandler.isWritePermission ( ) )
            savePicture ( bitmap );
        else
            requestPermission.setValue ( true );
    }


    public void savePicture ( Bitmap bitmap ) {
        if ( !isRecording ) {
            isRecording = true;
            fileHandler.download (
                    bitmap ,
                    model.getCurrentHitId ( ) + ".jpg" ,
                    new OnSaveFileResult ( ) {
                        @Override
                        public void onOk ( ) {
                            message.setValue ( new Pair (
                                    "" ,
                                    app.getString ( R.string.imageSaved ) ) );
                            message.setValue ( null );
                            isRecording = false;
                        }

                        @SuppressWarnings("rawtypes")
                        @Override
                        public void onError ( ) {
                            message.setValue ( new Pair (
                                    app.getString ( R.string.error ) ,
                                    app.getString ( R.string.failedUpload ) ) );
                            message.setValue ( null );
                            isRecording = false;
                        }
                    }
            );
        }
    }

    /**
     * Установить обои
     */

    public void setWallPaper ( int currentIndex , Bitmap bitmap ) {
        if ( !isRecording ) {
            isRecording = true;
            model.setCurrentIndex ( currentIndex );

            fileHandler.setWallPaper (
                    bitmap ,
                    new OnSaveFileResult ( ) {
                        @Override
                        public void onOk ( ) {
                            wallPaperScreenCall.setValue ( true );
                            wallPaperScreenCall.setValue ( null );
                            isRecording = false;
                        }

                        @Override
                        public void onError ( ) {
                            message.setValue ( new Pair<> (
                                    app.getString ( R.string.error ) ,
                                    app.getString ( R.string.wallpaperNotSet ) ) );
                            message.setValue ( null );
                            isRecording = false;
                        }
                    } );
        }
    }

    /**
     * Поделиться изображением
     */
    public void share ( int currentIndex , Bitmap bitmap ) {

        model.setCurrentIndex ( currentIndex );

        fileHandler.share ( bitmap ,
                new OnSaveFileResult ( ) {
                    @Override
                    public void onOk ( ) {
                    }

                    @Override
                    public void onError ( ) {
                        message.setValue ( new Pair<> (
                                app.getString ( R.string.error ) ,
                                app.getString ( R.string.failedShare ) ) );
                        message.setValue ( null );
                    }
                } );
    }


    /**
     * Сохранение индекса текущего изображения
     */
    public void setCurrentIndex ( int position ) {
        model.setCurrentIndex ( position );
    }

    @Override
    protected void onCleared ( ) {
        super.onCleared ( );
        fileHandler.onCleared ( );
    }
}
