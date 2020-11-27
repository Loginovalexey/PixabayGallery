package ru.loginovalexeydevelopment.picturefinder.model.entities;

import android.annotation.SuppressLint;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Класс с дополнительной информацией, используется для кэширования
 * Содержит данные о максимально возможном количестве изображений для загрузки, переданном сервером.
 * Также содержит дату создания кэша для проверки на актуальность
 */

@SuppressWarnings("ALL")
@Entity(tableName = "totalInfo")
public class TotalInfo {
    //Таблица состоит из одной записи
    @PrimaryKey()
    @Expose
    private Integer index = 1;

    //Максимально возможное количество изображений для загрузки
    @SuppressWarnings("FieldMayBeFinal")
    @Expose
    private final Integer totalHits;

    //Дата создания кэша
    @Expose
    private String date;

    public TotalInfo ( int totalHits ) {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );
        this.totalHits = totalHits;
        date = dateFormat.format ( new Date ( ) );
    }

    public String getDate ( ) {
        return date;
    }

    public void setDate ( String date ) {
        this.date = date;
    }

    public Integer getIndex ( ) {
        return index;
    }

    public void setIndex ( Integer index ) {
        this.index = index;
    }

    public Integer getTotalHits ( ) {
        return totalHits;
    }

}

