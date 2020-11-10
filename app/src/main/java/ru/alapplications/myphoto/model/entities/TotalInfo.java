package ru.alapplications.myphoto.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.alapplications.myphoto.model.entities.Hit;


@Entity(tableName = "totalInfo")
public class TotalInfo {

    @PrimaryKey(autoGenerate = false)
    public Integer index = 1;

    public Integer totalHits;

    public String date;

    public String getDate ( ) {
        return date;
    }

    public void setDate ( String date ) {
        this.date = date;
    }


    public TotalInfo ( int totalHits ) {
        DateFormat dateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );
        this.totalHits = totalHits;
        date = dateFormat.format ( new Date ( ) );
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

    public void setTotalHits ( Integer totalHits ) {
        this.totalHits = totalHits;
    }

}

