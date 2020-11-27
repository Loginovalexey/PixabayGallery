package ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.loginovalexeydevelopment.picturefinder.R;
import ru.loginovalexeydevelopment.picturefinder.app.App;
import ru.loginovalexeydevelopment.picturefinder.model.Model;
import ru.loginovalexeydevelopment.picturefinder.model.entities.ServerResponse;
import ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel.retrofit.ApiBuilder;

/**
 * Класс, отвечающий за получение данных с веб-сервера
 */

public class ServerHandler {

    @Inject
    Model model;

    @Inject
    App app;

    public ServerHandler ( ) {
        App.getAppComponent ( ).inject ( this );
    }


    //Формирование нового запроса к серверу
    public void request ( int position ,
                          int pageSize ,
                          OnServerLoadListener onServerLoadListener ) {
        int nextPage = position / pageSize + 1;
        paramsRequest ( nextPage , pageSize )
                .observeOn ( Schedulers.io ( ) )
                .subscribeOn ( Schedulers.io ( ) )
                .subscribe (
                        onServerLoadListener::getResult ,
                        throwable -> onServerLoadListener.getResult ( null ) );
    }

    /**
     * @param nextPageNumber - номер очередной загружаемой страницы с веб-сервера
     * @param pageSize       - размер страницы
     */
    public Single<ServerResponse> paramsRequest ( int nextPageNumber , int pageSize ) {
        //Отправка параметризованного запроса на веб-сервер и получение новой страницы
        return new ApiBuilder ( )
                .serverRequest (
                        model.getSearchOptions ( ).getQuery ( ) ,
                        Locale.getDefault ( ).getLanguage ( ) ,
                        getImageTypeValue ( ) ,
                        getOrientationValue ( ) ,
                        getCategoryValue ( ) ,
                        getColorValues ( ) ,
                        model.getSearchOptions ( ).getEditorChoice ( ) ,
                        model.getSearchOptions ( ).getSafeSearchChoice ( ) ,
                        getOrderValue ( ) ,
                        nextPageNumber ,
                        pageSize );
    }

    //Получение строковых данных о типе рисунков для формирования запроса
    public String getImageTypeValue ( ) {
        if ( model.getSearchOptions ( ).getImageTypeChoice ( ) )
            return app.getResources ( ).getStringArray ( R.array.imageType )[
                    model.getSearchOptions ( ).getImageTypeIndex ( )
                    ];
        else return "";
    }

    //Получение строковых данных об ориентацмм для формирования запроса
    public String getOrientationValue ( ) {
        if ( model.getSearchOptions ( ).getOrientationChoice ( ) ) {
            return app.getResources ( ).getStringArray ( R.array.orientation )[
                    model.getSearchOptions ( ).getOrientationIndex ( )];

        } else return "";
    }

    //Получение строковых данных о категории для формирования запроса
    public String getCategoryValue ( ) {
        if ( model.getSearchOptions ( ).getCategoryChoice ( ) ) {
            return app.getResources ( )
                    .getStringArray ( R.array.category )[
                            model.getSearchOptions ( ).getCategoryIndex ( )];
        } else return "";
    }

    //Получение строковых данных о выбранных цветах для формирования запроса
    public String getColorValues ( ) {
        StringBuilder result = new StringBuilder ( );
        if ( model.getSearchOptions ( ).getColorsChoice ( ) ) {
            for (int i = 0; i < model.getSearchOptions ( ).getColorsChecks ( ).length; i++) {
                if ( model.getSearchOptions ( ).getColorsChecks ( )[i] ) {
                    if ( result.toString ( ).equals ( "" ) ) {
                        result = new StringBuilder ( app.getResources ( ).getStringArray ( R.array.color )[i] );
                    } else {
                        result.append ( "," ).append ( app.getResources ( )
                                .getStringArray ( R.array.color )[i] );
                    }
                }
            }
        }
        return result.toString ( );
    }

    //Получение строковых данных о выборе сортировки для формирования запроса
    public String getOrderValue ( ) {
        if ( model.getSearchOptions ( ).getOrderChoice ( ) ) {
            return app.getResources ( )
                    .getStringArray ( R.array.order )[model.getSearchOptions ( ).getOrderIndex ( )];

        } else return "";
    }
}
