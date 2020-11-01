package ru.alapplications.myphoto.ui.galleryFragment.view;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

public interface GalleryView
{
    void detailHitCall ( );
    void sendMessage ( String string );
    void setAdapter ( HitAdapter hitAdapter );
    Fragment getFragment ( );
    void hideRepeatButton();
    void showRepeatButton();
}

