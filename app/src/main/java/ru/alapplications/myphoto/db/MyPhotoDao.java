package ru.alapplications.myphoto.db;

import android.util.Pair;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Single;
import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.ui.galleryFragment.presenter.TotalInfo;

@Dao
public abstract class MyPhotoDao {

    @Query("SELECT * FROM hits")
    public abstract Single<List<Hit>> getAllHits ( );

    @Query("SELECT * FROM hits")
    public abstract List<Hit> getHits ( );

    @Query("SELECT * FROM totalInfo WHERE `index` =1")
    public abstract TotalInfo getTotalInfo ( );

    @Query("DELETE FROM hits")
    public abstract void deleteAllHits ( );

    @Query("DELETE FROM totalInfo")
    public abstract void deleteTotalInfo ( );

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> cacheHits ( List<Hit> hits );

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long setTotalHits ( TotalInfo totalHits );

    @Transaction
    public List<Long> clearDbTableAndCacheHits ( List<Hit> hits , TotalInfo totalHits ) {
        deleteAllHits ( );
        setTotalHits ( totalHits );
        return cacheHits ( hits );
    }

    @Transaction
    public Pair<List<Hit>, TotalInfo> getTotalAndHits ( ) {
        return new Pair ( getHits ( ) , getTotalInfo ( ) );
    }

    @Transaction
    public Boolean clearTotalAndHits ( ) {
        deleteAllHits ( );
        deleteTotalInfo ( );
        return true;
    }

}
