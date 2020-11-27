package ru.loginovalexeydevelopment.picturefinder.model.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import kotlin.jvm.Transient;


/**
 * Класс для хранения данных об изображении.
 * В базе данных в таблице hits хранится список объектов для кэширования
 */
@Entity(tableName = "hits")
public class Hit {
    //Автоматически присваемые индексы для упорядоченного хранения данных в соответствии с
    //очередностью загрузки
    @PrimaryKey(autoGenerate = true)
    @Expose
    private Integer index;
    //Уникальный идентификатор изображения
    @Expose
    private Integer id;
    //Ссылка для скачивания миниатюры изображения
    @Expose
    private String  previewURL;
    //Ссылка для скачивания полноразмерного изображения
    @Expose
    private String  largeImageURL;
    //Служебное поле-флаг для определения отрасовано изображение или нет
    @Transient
    @Ignore
    private Boolean isLoaded = false;


    public Integer getIndex ( ) {
        return index;
    }

    public void setIndex ( Integer index ) {
        this.index = index;
    }

    public Integer getId ( ) {
        return id;
    }

    public void setId ( Integer id ) {
        this.id = id;
    }

    public String getPreviewURL ( ) {
        return previewURL;
    }

    public void setPreviewURL ( String previewURL ) {
        this.previewURL = previewURL;
    }

    public String getLargeImageURL ( ) {
        return largeImageURL;
    }

    public void setLargeImageURL ( String largeImageURL ) {
        this.largeImageURL = largeImageURL;
    }

    public Boolean isLoaded (){ return isLoaded;}

    public void setIsLoaded(Boolean isLoaded){this.isLoaded = isLoaded;}

}
