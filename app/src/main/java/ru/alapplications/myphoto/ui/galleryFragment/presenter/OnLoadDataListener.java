package ru.alapplications.myphoto.ui.galleryFragment.presenter;

import ru.alapplications.myphoto.model.entities.ServerResponse;

interface OnLoadDataListener {
    void getLoadResult ( ServerResponse serverResponse );
}
