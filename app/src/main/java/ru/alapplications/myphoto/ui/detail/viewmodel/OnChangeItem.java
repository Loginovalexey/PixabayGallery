package ru.alapplications.myphoto.ui.detail.viewmodel;

import ru.alapplications.myphoto.model.entities.Hit;

/**
 * Колбэк вызывается при перелистывании страницы
 */
public interface OnChangeItem {

    /**
     * @param currentHit Текущее отображаемое изображение
     */
    void setCurrentHit ( Hit currentHit );
}
