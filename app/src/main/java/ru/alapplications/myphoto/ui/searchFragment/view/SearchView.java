package ru.alapplications.myphoto.ui.searchFragment.view;

public interface SearchView {
    void initQuery ( String query );
    void initImageTypeIndex ( Integer imageTypeIndex );
    void initImageTypeChoice ( boolean imageTypeChoice );
    void initOrientationIndex ( Integer orientationIndex );
    void initOrientationChoice ( boolean orientationChoice );
    void initCategoryIndex ( Integer categoryIndex );
    void initCategoryChoice ( boolean categoryChoice );
    void initColorsChecks ( boolean[] colorsChecks );
    void initColorsChoice ( boolean colorsChoice );
    void intEditorChoice ( boolean editorChoice );
    void initSafeSearchChoice ( boolean safeSearchChoice );
    void initOrderIndex ( Integer orderIndex );
    void initOrderChoice ( boolean orderChoice );

    String getQuery ( );

    int getImageTypeIndex ( );
    boolean getImageTypeChoice ( );
    int getOrientationIndex ( );
    boolean getOrientationChoice ( );
    int getCategoryIndex ( );
    boolean getCategoryChoice ( );
    boolean[] getColorsChecks ( );
    boolean getColorsChoice ( );
    boolean getEditorChoice ( );
    boolean getSafeSearchChoice ( );
    Integer getOrderIndex ( );
    boolean getOrderChoice ( );

    void back();
}
