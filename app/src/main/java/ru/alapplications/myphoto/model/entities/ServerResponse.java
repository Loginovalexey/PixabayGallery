
package ru.alapplications.myphoto.model.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "serverResponse")
public class ServerResponse {
    @PrimaryKey(autoGenerate = false)
    private Integer index = 1;
    @SerializedName("total")
    @Expose
    private Integer   total;
    @SerializedName("totalHits")
    @Expose
    private Integer   totalHits;
    @SerializedName("hits")
    @Expose
    @Ignore
    private List<Hit> hits = null;

    public Integer getTotal ( ) {
        return total;
    }

    public void setTotal ( Integer total ) {
        this.total = total;
    }

    public Integer getTotalHits ( ) {
        return totalHits;
    }

    public void setTotalHits ( Integer totalHits ) {
        this.totalHits = totalHits;
    }

    public List<Hit> getHits ( ) {
        return hits;
    }

}
