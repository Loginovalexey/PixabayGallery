package ru.alapplications.myphoto.db;

import android.util.Pair;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.model.entities.TotalInfo;


/**
 * Класс определяет операции с базой данных для кэширования
 */
@Dao
public abstract class CacheDao {

     //Выбор всех существующих записей об изображениях
    @Query("SELECT * FROM hits")
    public abstract List<Hit> getHits ( );


    //Получение дополнительных данных
    @Query("SELECT * FROM totalInfo WHERE `index` =1")
    public abstract TotalInfo getTotalInfo ( );

     //Удаление списка изображений
    @Query("DELETE FROM hits")
    public abstract void deleteAllHits ( );

    //Удаление дополнительной информации
    @Query("DELETE FROM totalInfo")
    public abstract void deleteTotalInfo ( );

    //Запись в базу данных переданного списка изображений
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> saveCache ( List<Hit> hits );

    //Запись в базу данных дополнительной информации
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void setTotalHits ( TotalInfo totalHits );

    /**
     * Запись данных в кэш
     * @param hits - список данных об изображениях
     * @param totalHits - дополнительная информация, определяется классом TotalInfo
     */
    @Transaction
    public List<Long> saveCache ( List<Hit> hits , TotalInfo totalHits ) {
        clearCache ();
        setTotalHits ( totalHits );
        return saveCache ( hits );
    }

    /**
     * Чтение кэша
     * @return первый параметр - список записей, второй - дополнительная информация
     */
    @Transaction
    public Pair loadCache ( ) {
        return new Pair ( getHits ( ) , getTotalInfo ( ) );
    }

    /**
     * Удаление кэша. Вызывается при установке новых условий поиска
     */
    @Transaction
    public Boolean clearCache ( ) {
        deleteAllHits ( );
        deleteTotalInfo ( );
        return true;
    }

}
