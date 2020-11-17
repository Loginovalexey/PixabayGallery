package ru.alapplications.myphoto.ui.gallery.viewmodel;

import java.util.List;

import ru.alapplications.myphoto.model.entities.Hit;

//Интерфейс для обработки результатов загрузки кэша
interface OnCacheLoadListener {
    /**
     * @param isExist - существует ли кэш
     * @param hits - набор данных
     * @param totalHits - сведения о наборе данных
     */
    void getResult( boolean isExist, List<Hit> hits, Integer totalHits);
}
