package ru.alapplications.myphoto.ui.gallery.viewmodel;

import java.util.List;

import ru.alapplications.myphoto.model.entities.Hit;

interface OnLoadCacheListener {
    void getResult( boolean isExist, List<Hit> hits, Integer totalHits);
}
