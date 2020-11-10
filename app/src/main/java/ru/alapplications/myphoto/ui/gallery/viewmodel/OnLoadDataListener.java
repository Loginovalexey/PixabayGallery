package ru.alapplications.myphoto.ui.gallery.viewmodel;

import ru.alapplications.myphoto.model.entities.ServerResponse;

interface OnLoadDataListener {
    void getLoadResult ( ServerResponse serverResponse );
}
