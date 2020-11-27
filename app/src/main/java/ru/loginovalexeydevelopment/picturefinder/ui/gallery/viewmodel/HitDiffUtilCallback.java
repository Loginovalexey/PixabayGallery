package ru.loginovalexeydevelopment.picturefinder.ui.gallery.viewmodel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import ru.loginovalexeydevelopment.picturefinder.model.entities.Hit;

/**
 * Класс для оптимизации прокрутки списка.
 * Элементы сравниваются по id
 */

public class HitDiffUtilCallback extends DiffUtil.ItemCallback<Hit> {

    @Override
    public boolean areItemsTheSame ( @NonNull Hit oldItem , @NonNull Hit newItem ) {
        return oldItem.getId ( ).equals ( newItem.getId ( ));
    }

    @Override
    public boolean areContentsTheSame ( @NonNull Hit oldItem , @NonNull Hit newItem ) {
        return oldItem.getId ().equals ( newItem.getId ( ));
    }

}
