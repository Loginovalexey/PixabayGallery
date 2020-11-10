package ru.alapplications.myphoto.ui.detail.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class HackyProblematicPager extends ViewPager {

    public HackyProblematicPager ( Context context ) {
        super ( context );
    }

    public HackyProblematicPager ( Context context , AttributeSet attrs ) {
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
