package ru.alapplications.myphoto.ui.detail.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Класс пэйджера с поддержкой масштабирования
 */
public class ZoomablelePager extends ViewPager {

    public ZoomablelePager ( Context context ) {
        super ( context );
    }

    public ZoomablelePager ( Context context , AttributeSet attrs ) {
        super ( context , attrs );
    }

    @Override
    public boolean onInterceptTouchEvent ( MotionEvent ev ) {
        try {
            return super.onInterceptTouchEvent ( ev );
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
