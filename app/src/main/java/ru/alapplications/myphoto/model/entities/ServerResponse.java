
package ru.alapplications.myphoto.model.entities;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Класс для получения ответов сервера
 */

public class ServerResponse {

    //Максимальное количество рисунков для загрузки
    @Expose
    private       Integer   totalHits;
    //Список с данными об изображениях
    @Expose
    private final List<Hit> hits = null;

    public Integer getTotalHits ( ) {
        return totalHits;
    }

    public List<Hit> getHits ( ) {
        return hits;
    }

}
