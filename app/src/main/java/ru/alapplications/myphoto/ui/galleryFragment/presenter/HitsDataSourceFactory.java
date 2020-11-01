package ru.alapplications.myphoto.ui.galleryFragment.presenter;

import androidx.paging.DataSource;

import java.util.List;

import ru.alapplications.myphoto.model.entities.Hit;

public class HitsDataSourceFactory extends DataSource.Factory<Integer, Hit> {

    @Override
    public DataSource create ( ) {
        return new HitsDataSource ( );
    }
}