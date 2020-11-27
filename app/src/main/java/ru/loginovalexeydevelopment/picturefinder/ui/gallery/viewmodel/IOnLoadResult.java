package ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel;


/**
 * Интерфейс для вызова колбэка для обработки статусов загрузки
 */
interface IOnLoadResult {
    void callOnLoadResult( LoadingState result);
}
