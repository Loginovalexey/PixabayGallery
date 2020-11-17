package ru.alapplications.myphoto.ui.gallery.viewmodel;

/**
 * Статусы загрузки
 */
public enum LoadingState {
    //Ожидание
    WAITING,
    //Успех
    OK,
    //Ошибка при загрузке первой страницы
    FIRST_LOAD_ERROR,
    //Ошибка при загрузке очередной страницы
    LOAD_ERROR,
    //Нет данных
    EMPTY_DATA
}
