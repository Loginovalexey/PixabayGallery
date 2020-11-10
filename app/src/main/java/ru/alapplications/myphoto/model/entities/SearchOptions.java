package ru.alapplications.myphoto.model.entities;

public class SearchOptions {
    private String    query;
    private boolean   imageTypeChoice;
    private int       imageTypeIndex;
    private boolean   orientationChoice;
    private int       orientationIndex;
    private boolean   categoryChoice;
    private int       categoryIndex;
    private boolean   colorsChoice;
    private boolean[] colorsChecks;
    private boolean   editorChoice;
    private boolean   safeSearchChoice;
    private boolean   orderChoice;
    private int       orderIndex;

    public SearchOptions ( String query ,
                           boolean imageTypeChoice ,
                           int imageTypeIndex ,
                           boolean orientationChoice ,
                           int orientationIndex ,
                           boolean categoryChoice ,
                           int categoryIndex ,
                           boolean colorsChoice ,
                           boolean[] colorsChecks ,
                           boolean editorChoice ,
                           boolean safeSearchChoice ,
                           boolean orderChoice ,
                           int orderIndex ) {
        setOptions (
                query ,
                imageTypeChoice ,
                imageTypeIndex ,
                orientationChoice ,
                orientationIndex ,
                categoryChoice ,
                categoryIndex ,
                colorsChoice ,
                colorsChecks ,
                editorChoice ,
                safeSearchChoice ,
                orderChoice ,
                orderIndex
        );
    }

    public void setOptions ( String query ,
                             boolean imageTypeChoice ,
                             int imageTypeIndex ,
                             boolean orientationChoice ,
                             int orientationIndex ,
                             boolean categoryChoice ,
                             int categoryIndex ,
                             boolean colorsChoice ,
                             boolean[] colorsChecks ,
                             boolean editorChoice ,
                             boolean safeSearchChoice ,
                             boolean orderChoice ,
                             int orderIndex ) {
        this.query = query;
        this.imageTypeChoice = imageTypeChoice;
        this.imageTypeIndex = imageTypeIndex;
        this.orientationChoice = orientationChoice;
        this.orientationIndex = orientationIndex;
        this.categoryChoice = categoryChoice;
        this.categoryIndex = categoryIndex;
        this.colorsChoice = colorsChoice;
        this.colorsChecks = colorsChecks;
        this.editorChoice = editorChoice;
        this.safeSearchChoice = safeSearchChoice;
        this.orderChoice = orderChoice;
        this.orderIndex = orderIndex;
    }

    public SearchOptions ( ) {
        setOptions ( "Поиск" ,
                false ,
                0 ,
                false ,
                0 ,
                false ,
                0 ,
                false ,
                new boolean[]{} ,
                false ,
                false ,
                false ,
                0 );
    }

    public String getQuery ( ) {
        return query;
    }

    public void setQuery ( String query ) {
        this.query = query;
    }

    public boolean getImageTypeChoice ( ) {
        return imageTypeChoice;
    }

    public void setImageTypeChoice ( boolean imageTypeChoice ) {
        this.imageTypeChoice = imageTypeChoice;
    }

    public int getImageTypeIndex ( ) {
        return imageTypeIndex;
    }

    public void setImageTypeIndex ( int imageTypeIndex ) {
        this.imageTypeIndex = imageTypeIndex;
    }

    public boolean getOrientationChoice ( ) {
        return orientationChoice;
    }

    public void setOrientationChoice ( boolean orientationChoice ) {
        this.orientationChoice = orientationChoice;
    }

    public int getOrientationIndex ( ) {
        return orientationIndex;
    }

    public void setOrientationIndex ( int orientationIndex ) {
        this.orientationIndex = orientationIndex;
    }

    public boolean getCategoryChoice ( ) {
        return categoryChoice;
    }

    public void setCategoryChoice ( boolean categoryChoice ) {
        this.categoryChoice = categoryChoice;
    }

    public int getCategoryIndex ( ) {
        return categoryIndex;
    }

    public void setCategoryIndex ( int categoryIndex ) {
        this.categoryIndex = categoryIndex;
    }

    public boolean getColorsChoice ( ) {
        return colorsChoice;
    }

    public void setColorsChoice ( boolean colorsChoice ) {
        this.colorsChoice = colorsChoice;
    }

    public boolean[] getColorsChecks ( ) {
        return colorsChecks;
    }

    public void setColorsChecks ( boolean[] colorsChecks ) {
        this.colorsChecks = colorsChecks;
    }

    public boolean getEditorChoice ( ) {
        return editorChoice;
    }

    public void setEditorChoice ( boolean editorChoice ) {
        this.editorChoice = editorChoice;
    }

    public boolean getSafeSearchChoice ( ) {
        return safeSearchChoice;
    }

    public void setSafeSearchChoice ( boolean safeSearchChoice ) {
        this.safeSearchChoice = safeSearchChoice;
    }

    public boolean getOrderChoice ( ) {
        return orderChoice;
    }

    public void setOrderChoice ( boolean orderChoice ) {
        this.orderChoice = orderChoice;
    }

    public int getOrderIndex ( ) {
        return orderIndex;
    }

    public void setOrderIndex ( int orderIndex ) {
        this.orderIndex = orderIndex;
    }

}
