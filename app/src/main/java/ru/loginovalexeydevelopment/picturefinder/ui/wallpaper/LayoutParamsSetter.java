package ru.loginovalexeydevelopment.picturefinder.ui.wallpaper;

import android.widget.FrameLayout;

import com.theartofdev.edmodo.cropper.CropImageView;

class LayoutParamsSetter {
    /**
     * Вычисление параметров размещения в зависимости от размеров контейнера, рисунка и поворота
     */
    public static FrameLayout.LayoutParams setPlacementLayoutParameters (
            FrameLayout container,
            CropImageView child ,
            boolean isRotated ) {

        double childRatio;

            FrameLayout.LayoutParams childLayoutParams =
                    ( FrameLayout.LayoutParams ) child.getLayoutParams ( );

            double containerRatio = ( double ) container.getMeasuredHeight ( ) /
                    ( double ) container.getMeasuredWidth ( );

            if ( !isRotated ) {

                childRatio = ( double ) child.getWholeImageRect ( ).height ( ) /
                        ( double ) child.getWholeImageRect ( ).width ( );

                if ( childRatio > containerRatio ) {
                    childLayoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
                    double ratioWidth = ( double ) container.getMeasuredHeight ( ) /
                            ( double ) child.getWholeImageRect ( ).height ( );
                    childLayoutParams.width = ( int ) (child.getWholeImageRect ( ).width ( ) *
                            ratioWidth);
                } else {
                    childLayoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
                    double ratioHeight = ( double ) container.getMeasuredWidth ( ) /
                            ( double ) child.getWholeImageRect ( ).width ( );
                    childLayoutParams.height = ( int ) (child.getWholeImageRect ( ).height ( ) *
                            ratioHeight);
                }
            } else {

                childRatio = ( double ) child.getWholeImageRect ( ).width ( ) /
                        ( double ) child.getWholeImageRect ( ).height ( );

                if ( childRatio > containerRatio ) {
                    childLayoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
                    double ratioWidth = ( double ) container.getMeasuredHeight ( ) /
                            ( double ) child.getWholeImageRect ( ).width ( );
                    childLayoutParams.width = ( int ) (child.getWholeImageRect ( ).height ( ) *
                            ratioWidth);
                } else {
                    childLayoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
                    double ratioHeight = ( double ) container.getMeasuredWidth ( ) /
                            ( double ) child.getWholeImageRect ( ).height ( );
                    childLayoutParams.height = ( int ) (child.getWholeImageRect ( ).width ( ) *
                            ratioHeight);
                }
            }
            return childLayoutParams;
        }
}
