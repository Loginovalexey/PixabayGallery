package ru.alapplications.myphoto.ui.gallery.viewmodel;

import ru.alapplications.myphoto.model.entities.ServerResponse;

/**
 * Интерфейс для вызова колбэка после загрузки с веб-сервера
 */

interface OnServerLoadListener {
    void getResult ( ServerResponse serverResponse );
}
