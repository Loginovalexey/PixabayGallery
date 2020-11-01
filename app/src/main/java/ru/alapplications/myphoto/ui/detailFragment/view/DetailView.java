package ru.alapplications.myphoto.ui.detailFragment.view;

import java.util.List;

import ru.alapplications.myphoto.model.entities.Hit;

public interface DetailView {
    void updateView ( List<Hit> hits , Integer currentIndex );
    boolean showMessage ( String title , String message );
}
