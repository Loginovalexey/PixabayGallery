package ru.alapplications.myphoto.ui.gallery.viewmodel;

import androidx.paging.DataSource;

import java.util.List;

import ru.alapplications.myphoto.model.entities.Hit;

public class HitsDataSourceFactory extends DataSource.Factory<Integer, Hit> {
    IOnLoadResult onLoadResult;

    HitsDataSourceFactory(IOnLoadResult onLoadResult){
        this.onLoadResult = onLoadResult;
    }

    @Override
    public DataSource create ( ) {
        return new HitsDataSource ( onLoadResult );
    }
}