package ru.alapplications.myphoto.ui.galleryFragment.presenter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import ru.alapplications.myphoto.model.entities.Hit;

public class HitDiffUtilCallback extends DiffUtil.ItemCallback<Hit> {

    @Override
    public boolean areItemsTheSame ( @NonNull Hit oldItem , @NonNull Hit newItem ) {
        return oldItem.getId ( ) == newItem.getId ( );
    }

    @Override
    public boolean areContentsTheSame ( @NonNull Hit oldItem , @NonNull Hit newItem ) {
        return oldItem.getId ( ) == newItem.getId ( );
    }

}
