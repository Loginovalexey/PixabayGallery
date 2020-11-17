package ru.alapplications.myphoto.ui.gallery.viewmodel;

import ru.alapplications.myphoto.ui.gallery.viewmodel.LoadingState;


/**
 * Интерфейс для вызова колбэка для обработки статусов загрузки
 */
interface IOnLoadResult {
    void callOnLoadResult( LoadingState result);
}
