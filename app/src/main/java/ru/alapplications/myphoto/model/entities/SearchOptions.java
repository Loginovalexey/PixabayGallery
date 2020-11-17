package ru.alapplications.myphoto.model.entities;


/**
 * Класс для хранения данных о параметрах поиска
 */
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

    /**
     * @param query - предмет поиска
     * @param imageTypeChoice - флаг учета типа изображения
     * @param imageTypeIndex - выбор типа изображения
     * @param orientationChoice - флаг учета ориентации изображения
     * @param orientationIndex - выбор ориентации
     * @param categoryChoice - флаг учета категории изображения
     * @param categoryIndex - выбор категории
     * @param colorsChoice - флаг учета цветов
     * @param colorsChecks - выбранные цвета
     * @param editorChoice - флаг учета выбора редакции
     * @param safeSearchChoice - флаг учета ограничений по возрасту
     * @param orderChoice  - флаг учета сортировки
     * @param orderIndex - сортировка
     */
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
        setOptions ( "" ,
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

    public boolean getImageTypeChoice ( ) {
        return imageTypeChoice;
    }

    public int getImageTypeIndex ( ) {
        return imageTypeIndex;
    }

    public boolean getOrientationChoice ( ) {
        return orientationChoice;
    }

    public int getOrientationIndex ( ) {
        return orientationIndex;
    }

    public boolean getCategoryChoice ( ) {
        return categoryChoice;
    }

    public int getCategoryIndex ( ) {
        return categoryIndex;
    }

    public boolean getColorsChoice ( ) {
        return colorsChoice;
    }

    public boolean[] getColorsChecks ( ) {
        return colorsChecks;
    }

    public boolean getEditorChoice ( ) {
        return editorChoice;
    }

    public boolean getSafeSearchChoice ( ) {
        return safeSearchChoice;
    }

    public boolean getOrderChoice ( ) {
        return orderChoice;
    }

    public int getOrderIndex ( ) {
        return orderIndex;
    }

}
