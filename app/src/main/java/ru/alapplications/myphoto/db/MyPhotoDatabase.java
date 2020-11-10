package ru.alapplications.myphoto.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.alapplications.myphoto.model.entities.Hit;
import ru.alapplications.myphoto.model.entities.TotalInfo;

@Database(entities = {Hit.class, TotalInfo.class}, version = 1, exportSchema = false)
public abstract class MyPhotoDatabase extends RoomDatabase {
    public abstract MyPhotoDao getMyPhotoDao();
}