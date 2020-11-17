package ru.alapplications.myphoto.model;

import java.util.List;

import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.model.entities.SearchOptions;


/**
 * Класс модели. Используется для хранения, операций и обмена данными
 */

public class Model {
    /**
     * Массив с данными об изображениях. Размер определяется максимальным количеством доступных для
     * загрузки изображений. Содержит как инициализированные(загруженные) записи,
     * так и null-элементы(данные еще не загружены)
     */
    private volatile List<Hit>     hits;
    /**
     * Реальный размер массива - размер загруженных данных
     */
    private          int           loadedCount;
    /**
     * Индекс выбранного изображения
     */
    private          Integer       currentIndex;
    /**
     * Флаг необходимости сброса данных (используется при установки новых параметров поиска)
     */
    private          boolean       needReload;
    /**
     * Флаг необходимости сохранения кэша (при обновлении данных)
     */

    private boolean needSaveCache = false;

    /**
     * Параметры поиска
     */
    private          SearchOptions searchOptions;


    public Model ( ) {
        reset ();
    }

    public void reset ( ) {
        hits = null;
        currentIndex = 0;
        needReload = false;
        loadedCount = 0;
        needSaveCache = false;
    }

    public void setHits ( List<Hit> hits, int loadedCount ) {
        this.hits = hits;
        this.loadedCount = loadedCount;
    }

    public int getHitsSize ( ) {
        return hits.size ();
    }

    public List<Hit> getLoadedHits ( ) {
        if (hits!=null)
            return hits.subList ( 0 , loadedCount );
        else
            return null;
    }

    public boolean isNeedSaveCache ( ) {
        return needSaveCache;
    }

    public void setNeedSaveCache ( boolean needSaveCache ) {
        this.needSaveCache = needSaveCache;
    }

    public boolean isNeedReload ( ) {
        return needReload;
    }

    public void setCurrentIndex ( Integer index ) {
        currentIndex = index;
    }

    public String getCurrentHitId ( ) {
        return hits.get ( currentIndex ).getId ( ) + "";
    }

    public Integer getCurrentIndex ( ) {
        return currentIndex;
    }

    public SearchOptions getSearchOptions ( ) {
        return searchOptions;
    }

    public void setSearchOptions ( SearchOptions searchOptions ) {
        this.searchOptions = searchOptions;
        needReload = true;
    }
}