package ru.alapplications.myphoto.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Arrays;

import javax.inject.Inject;

import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.app.App;

public class SearchOptions {
    private static String    query;
    private static boolean   imageTypeChoice;
    private static int       imageTypeIndex;
    private static boolean   orientationChoice;
    private static int       orientationIndex;
    private static boolean   categoryChoice;
    private static int       categoryIndex;
    private static boolean   colorsChoice;
    private static boolean[] colorsChecks;
    private static boolean   editorChoice;
    private static boolean   safeSearchChoice;
    private static boolean   orderChoice;
    private static int       orderIndex;

    private final String SEARCH_OPTIONS_SHARED_PREFERENCES_FILE_NAME = "searchOptions.pref";

    @Inject
    App app;

    public SearchOptions ( ) {
        App.getAppComponent ( ).inject ( this );
        loadFromPref ( );
    }

    public void setQuery ( String text ) {
        query = text;
    }

    public String getQuery ( ) {
        return query;
    }

    public void setImageTypeChoice ( boolean imageTypeChoice ) {
        SearchOptions.imageTypeChoice = imageTypeChoice;
    }

    public boolean getImageTypeChoice ( ) {
        return imageTypeChoice;
    }


    public void setImageTypeIndex ( Integer imageTypeIndex ) {
        SearchOptions.imageTypeIndex = imageTypeIndex;
    }

    public Integer getImageTypeIndex ( ) {
        return imageTypeIndex;
    }

    public String getImageTypeValue ( ) {
        if ( imageTypeChoice ) {
            return app.getResources ( )
                    .getStringArray ( R.array.imageType )[imageTypeIndex];
        } else return "";
    }

    public void setOrientationChoice ( boolean orientationChoice ) {
        SearchOptions.orientationChoice = orientationChoice;
    }

    public boolean getOrientationChoice ( ) {
        return orientationChoice;
    }


    public void setOrientationIndex ( Integer orientationIndex ) {
        SearchOptions.orientationIndex = orientationIndex;

    }

    public Integer getOrientationIndex ( ) {
        return orientationIndex;
    }

    public String getOrientationValue ( ) {
        if ( orientationChoice ) {
            return app.getResources ( ).getStringArray ( R.array.orientation )[orientationIndex];

        } else return "";
    }

    public void setCategoryChoice ( boolean categoryChoice ) {
        SearchOptions.categoryChoice = categoryChoice;
    }

    public boolean getCategoryChoice ( ) {
        return categoryChoice;
    }

    public void setCategoryIndex ( Integer categoryIndex ) {
        SearchOptions.categoryIndex = categoryIndex;
    }

    public Integer getCategoryIndex ( ) {
        return categoryIndex;
    }

    public String getCategoryValue ( ) {
        if ( categoryChoice ) {
            return app.getResources ( )
                    .getStringArray ( R.array.category )[categoryIndex];

        } else return "";
    }

    public void setColorsChoice ( boolean colorsChoice ) {
        SearchOptions.colorsChoice = colorsChoice;
    }

    public boolean getColorsChoice ( ) {
        return colorsChoice;
    }


    public void setColorsChecks ( boolean[] colorsChecks ) {
        SearchOptions.colorsChecks = colorsChecks;
    }

    public boolean[] getColorsChecks ( ) {
        return colorsChecks;
    }

    public String getColorValues ( ) {
        String result = "";
        if ( colorsChoice ) {
            for (int i = 0; i < colorsChecks.length; i++) {
                if ( colorsChecks[i] ) {
                    if ( result == "" ) {
                        result = app.getResources ( ).getStringArray ( R.array.color )[i];
                    } else {
                        result = result + "," + app.getResources ( ).getStringArray ( R.array.color )[i];
                    }
                }
            }
        }
        return result;
    }

    public void setEditorChoice ( boolean editorChoice ) {
        SearchOptions.editorChoice = editorChoice;
    }

    public boolean getEditorChoice ( ) {
        return editorChoice;
    }

    public void setSafeSearchChoice ( boolean safeSearchChoice ) {
        SearchOptions.safeSearchChoice = safeSearchChoice;
    }

    public boolean getSafeSearchChoice ( ) {
        return safeSearchChoice;
    }

    public void setOrderChoice ( boolean orderChoice ) {
        SearchOptions.orderChoice = orderChoice;
    }

    public boolean getOrderChoice ( ) {
        return orderChoice;
    }


    public void setOrderIndex ( Integer orderIndex ) {
        SearchOptions.orderIndex = orderIndex;
        Log.d ( App.TAG , orderIndex.toString ( ) );
    }

    public Integer getOrderIndex ( ) {
        return orderIndex;
    }

    public String getOrderValue ( ) {
        if ( orderChoice ) {
            return app.getResources ( )
                    .getStringArray ( R.array.order )[orderIndex];

        } else return "";
    }

    private void loadFromPref ( ) {

        SharedPreferences sharedPreferences = app.getSharedPreferences ( SEARCH_OPTIONS_SHARED_PREFERENCES_FILE_NAME , Context.MODE_PRIVATE );

        query = sharedPreferences.getString ( "query" , "" );
        imageTypeChoice = sharedPreferences.getBoolean ( "imageTypeChoice" , false );
        imageTypeIndex = sharedPreferences.getInt ( "imageTypeIndex" , 0 );
        orientationChoice = sharedPreferences.getBoolean ( "orientationChoice" , false );
        orientationIndex = sharedPreferences.getInt ( "orientationIndex" , 0 );
        categoryChoice = sharedPreferences.getBoolean ( "categoryChoice" , false );
        categoryIndex = sharedPreferences.getInt ( "categoryIndex" , 0 );
        colorsChoice = sharedPreferences.getBoolean ( "colorsChoice" , false );
        colorsChecks = new boolean[app.getResources ( ).getStringArray (
                R.array.color ).length];
        String checksString = sharedPreferences.getString ( "colorsChecks" , "" );
        if ( !checksString.equals ( "" ) ) {
            String[] parts = checksString.split ( "," );
            for (int i = 0; i < parts.length; i++)
                colorsChecks[i] = Boolean.parseBoolean ( parts[i].trim ( ) );
        }
        editorChoice = sharedPreferences.getBoolean ( "editorChoice" , false );
        safeSearchChoice = sharedPreferences.getBoolean ( "safeSearchChoice" , false );
        orderChoice = sharedPreferences.getBoolean ( "orderChoice" , false );
        orderIndex = sharedPreferences.getInt ( "orderIndex" , 0 );
    }

    public void saveToPref ( ) {

        SharedPreferences sharedPreferences = app.getSharedPreferences ( SEARCH_OPTIONS_SHARED_PREFERENCES_FILE_NAME , Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = sharedPreferences.edit ( );
        editor.putString ( "query" , query );
        editor.putBoolean ( "imageTypeChoice" , imageTypeChoice );
        editor.putInt ( "imageTypeIndex" , imageTypeIndex );
        editor.putBoolean ( "orientationChoice" , orientationChoice );
        editor.putInt ( "orientationIndex" , orientationIndex );
        editor.putBoolean ( "categoryChoice" , categoryChoice );
        editor.putInt ( "categoryIndex" , categoryIndex );
        editor.putBoolean ( "colorsChoice" , colorsChoice );
        editor.putString ( "colorsChecks" , Arrays.toString ( colorsChecks ) );
        editor.putBoolean ( "editorChoice" , editorChoice );
        editor.putBoolean ( "safeSearchChoice" , safeSearchChoice );
        editor.putBoolean ( "orderChoice" , orderChoice );
        editor.putInt ( "orderIndex" , orderIndex );
        editor.apply ( );
    }



}
