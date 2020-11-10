package ru.alapplications.myphoto.model.entities;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "hits")
public class Hit {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("pageURL")
    @Expose
    private String  pageURL;
    @SerializedName("previewURL")
    @Expose
    private String  previewURL;
    @SerializedName("largeImageURL")
    @Expose
    private String  largeImageURL;

    private Boolean isLoaded = false;


    public Integer getId ( ) {
        return id;
    }

    public void setId ( Integer id ) {
        this.id = id;
    }

    public String getPageURL ( ) {
        return pageURL;
    }

    public void setPageURL ( String pageURL ) {
        this.pageURL = pageURL;
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

    public Integer getIndex ( ) {
        return index;
    }

    public void setIndex ( Integer index ) {
        this.index = index;
    }

    public Boolean getIsLoaded(){ return isLoaded;}

    public void setIsLoaded(Boolean isLoaded){this.isLoaded = isLoaded;}

}
