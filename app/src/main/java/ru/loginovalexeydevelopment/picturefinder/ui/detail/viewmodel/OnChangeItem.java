package ru.loginovalexeydevelopment.picturefinder.ui.detail.viewmodel;

import ru.loginovalexeydevelopment.picturefinder.model.entities.Hit;

/**
 * Колбэк вызывается при перелистывании страницы
 */
public interface OnChangeItem {

    /**
     * @param currentHit Текущее отображаемое изображение
     */
    void setCurrentHit ( Hit currentHit );
}
