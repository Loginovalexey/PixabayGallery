package ru.alapplications.myphoto.ui.gallery.viewmodel;

import androidx.paging.DataSource;

import org.jetbrains.annotations.NotNull;

import ru.alapplications.myphoto.model.entities.Hit;

/**
 * Фабрика для создания класса HitsDataSource - источника данных
 */

public class HitsDataSourceFactory extends DataSource.Factory<Integer, Hit> {
    final IOnLoadResult onLoadResult;

    /**
     * @param onLoadResult В конструктор создаваемого объекта передается колбэк
     *                     для получения состояний загрузки
     */
    HitsDataSourceFactory(IOnLoadResult onLoadResult){
        this.onLoadResult = onLoadResult;
    }

    @NotNull
    @Override
    public DataSource create ( ) {
        return new HitsDataSource ( onLoadResult );
    }
}