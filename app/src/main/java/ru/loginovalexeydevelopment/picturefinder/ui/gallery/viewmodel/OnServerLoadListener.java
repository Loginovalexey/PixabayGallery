package ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel;

import ru.loginovalexeydevelopment.picturefinder.model.entities.ServerResponse;

/**
 * Интерфейс для вызова колбэка после загрузки с веб-сервера
 */

interface OnServerLoadListener {
    void getResult ( ServerResponse serverResponse );
}
