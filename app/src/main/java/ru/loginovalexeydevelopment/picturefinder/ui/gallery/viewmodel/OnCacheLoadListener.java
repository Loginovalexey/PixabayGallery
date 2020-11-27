package ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel;

import java.util.List;

import ru.loginovalexeydevelopment.picturefinder.model.entities.Hit;

//Интерфейс для обработки результатов загрузки кэша
interface OnCacheLoadListener {
    /**
     * @param isExist - существует ли кэш
     * @param hits - набор данных
     * @param totalHits - сведения о наборе данных
     */
    void getResult( boolean isExist, List<Hit> hits, Integer totalHits);
}
