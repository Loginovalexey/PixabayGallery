package ru.loginovalexeydevelopment.picturefinder.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.loginovalexeydevelopment.picturefinder.model.entities.Hit;
import ru.loginovalexeydevelopment.picturefinder.model.entities.TotalInfo;

/**
 * Класс базы данных, используется для кэширования
 */

@Database(entities = {Hit.class, TotalInfo.class}, version = 1, exportSchema = false)
public abstract class CacheDatabase extends RoomDatabase {
    public abstract CacheDao getCacheDao();
}