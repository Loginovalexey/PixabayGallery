package ru.alapplications.myphoto.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.model.entities.TotalInfo;

/**
 * Класс базы данных, используется для кэширования
 */

@Database(entities = {Hit.class, TotalInfo.class}, version = 1, exportSchema = false)
public abstract class CacheDatabase extends RoomDatabase {
    public abstract CacheDao getCacheDao();
}