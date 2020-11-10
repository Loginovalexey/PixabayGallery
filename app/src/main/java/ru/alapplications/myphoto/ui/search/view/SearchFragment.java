package ru.alapplications.myphoto.ui.search.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;
import ru.alapplications.myphoto.R;
import ru.alapplications.myphoto.model.entities.SearchOptions;
import ru.alapplications.myphoto.ui.search.viewmodel.SearchViewModel;


public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;

    private Unbinder unbinder;

    @BindView(R.id.searchEditText)
    EditText searchEditText;

    @BindView(R.id.imageTypeСheckBox)
    CheckBox imageTypeCheckBox;

    @BindView(R.id.imageTypeSpinner)
    Spinner imageTypeSpinner;

    @BindView(R.id.orientationCheckBox)
    CheckBox orientationCheckBox;

    @BindView(R.id.orientationRadioGroup)
    RadioGroup orientationRadioGroup;

    @BindView(R.id.categoryCheckBox)
    CheckBox categoryCheckBox;

    @BindView(R.id.categorySpinner)
    Spinner categorySpinner;

    @BindView(R.id.colorsCheckBox)
    CheckBox colorsCheckBox;

    @BindView(R.id.colorsChipGroup)
    ChipGroup colorsChipGroup;

    @BindView(R.id.editorsChoiceCheckBox)
    CheckBox editorsChoiceCheckBox;

    @BindView(R.id.safeSearchCheckBox)
    CheckBox safeSearchCheckBox;

    @BindView(R.id.orderСheckBox)
    CheckBox orderCheckBox;

    @BindView(R.id.orderRadioGroup)
    RadioGroup orderRadioGroup;

    @BindView(R.id.searchButton)
    Button searchButton;


    public static SearchFragment getInstance ( ) {
        return new SearchFragment ( );
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        viewModel = ViewModelProviders.of ( getActivity ( ) ).get ( SearchViewModel.class );
    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        View view = inflater.inflate ( R.layout.fragment_search_options , container , false );
        unbinder = ButterKnife.bind ( this , view );
        return view;
    }


    @Override
    public void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );
        ButterKnife.bind ( getActivity ( ) );

        searchEditText.setOnEditorActionListener ( ( textView , i , keyEvent ) -> {
            if ( i == EditorInfo.IME_ACTION_SEARCH ) {
                startSearch ( );
                return true;
            }
            return false;
        } );

        imageTypeCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) -> {
            setGroupVisibility ( isChecked , imageTypeSpinner );
        } );


        for (int i = 0; i < getResources ( ).getStringArray ( R.array.orientationText ).length; i++) {
            RadioButton radioButton = new RadioButton ( getContext ( ) );
            radioButton.setText ( getResources ( ).getStringArray ( R.array.orientationText )[i] );
            orientationRadioGroup.addView ( radioButton );
        }

        orientationCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) ->
                setGroupVisibility ( isChecked , orientationRadioGroup ) );

        categoryCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) ->
                setGroupVisibility ( isChecked , categorySpinner ) );

        for (int i = 0; i < getResources ( ).getStringArray ( R.array.colorText ).length; i++) {
            Chip chip = new Chip ( getContext ( ) );
            chip.setText ( getResources ( ).getStringArray ( R.array.colorText )[i] );
            String backGroundColor =
                    getResources ( ).getStringArray ( R.array.colorRgb )[i];
            chip.setChipBackgroundColor ( ColorStateList.valueOf ( Color.parseColor ( backGroundColor ) ) );
            if ( backGroundColor.equals ( "#000000" ) )
                chip.setTextColor ( Color.parseColor ( "#FFFFFF" ) );
            chip.setCheckable ( true );
            colorsChipGroup.addView ( chip );
        }

        colorsCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) -> {
            setGroupVisibility ( isChecked , colorsChipGroup );
        } );

        for (int i = 0; i < getResources ( ).getStringArray ( R.array.order ).length; i++) {
            RadioButton radioButton = new RadioButton ( getContext ( ) );
            radioButton.setText ( getResources ( ).getStringArray ( R.array.orderText )[i] );
            orderRadioGroup.addView ( radioButton );
        }

        orderCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) ->
                setGroupVisibility ( isChecked , orderRadioGroup ) );

        searchButton.setOnClickListener ( view1 -> startSearch ( ) );

        viewModel.searchOptionsState.observe ( getViewLifecycleOwner ( ) , searchOptions -> {
            initQuery ( searchOptions.getQuery ( ) );
            initImageTypeIndex ( searchOptions.getImageTypeIndex ( ) );
            initImageTypeChoice ( searchOptions.getImageTypeChoice ( ) );
            initOrientationIndex ( searchOptions.getOrientationIndex ( ) );
            initOrientationChoice ( searchOptions.getOrientationChoice ( ) );
            initCategoryIndex ( searchOptions.getCategoryIndex ( ) );
            initCategoryChoice ( searchOptions.getCategoryChoice ( ) );
            initColorsChecks ( searchOptions.getColorsChecks ( ) );
            initColorsChoice ( searchOptions.getColorsChoice ( ) );
            intEditorChoice ( searchOptions.getEditorChoice ( ) );
            initSafeSearchChoice ( searchOptions.getSafeSearchChoice ( ) );
            initOrderIndex ( searchOptions.getOrderIndex ( ) );
            initOrderChoice ( searchOptions.getOrderChoice ( ) );
        } );
    }

    private void startSearch ( ) {
        SearchOptions searchOptions = new SearchOptions (
                getQuery ( ) ,
                getImageTypeChoice ( ) ,
                getImageTypeIndex ( ) ,
                getOrientationChoice ( ) ,
                getOrientationIndex ( ) ,
                getCategoryChoice ( ) ,
                getCategoryIndex ( ) ,
                getColorsChoice ( ) ,
                getColorsChecks ( ) ,
                getEditorChoice ( ) ,
                getSafeSearchChoice ( ) ,
                getOrderChoice ( ),
                getOrderIndex ( ) );
        viewModel.actionSearch ( searchOptions );
        back ( );
    }

    public void initQuery ( String query ) {
        searchEditText.setText ( query );
    }

    public void initImageTypeIndex ( Integer imageTypeIndex ) {
        imageTypeSpinner.setSelection ( imageTypeIndex );
    }


    public void initImageTypeChoice ( boolean imageTypeChoice ) {
        imageTypeCheckBox.setChecked ( imageTypeChoice );
        setGroupVisibility ( imageTypeChoice , imageTypeSpinner );

    }

    public void initOrientationIndex ( Integer orientationIndex ) {
        (( RadioButton ) orientationRadioGroup.getChildAt ( orientationIndex )).setChecked ( true );
    }

    public void initOrientationChoice ( boolean orientationChoice ) {
        orientationCheckBox.setChecked ( orientationChoice );
        setGroupVisibility ( orientationChoice , orientationRadioGroup );
    }

    public void initCategoryIndex ( Integer categoryIndex ) {
        categorySpinner.setSelection ( categoryIndex );
    }

    public void initCategoryChoice ( boolean categoryChoice ) {
        categoryCheckBox.setChecked ( categoryChoice );
        setGroupVisibility ( categoryChoice , categorySpinner );
    }

    private void setGroupVisibility ( Boolean isChecked , View viewGroup ) {
        if ( isChecked ) {
            viewGroup.setVisibility ( View.VISIBLE );
        } else {
            viewGroup.setVisibility ( View.GONE );
        }
    }


    public String getQuery ( ) {
        return searchEditText.getText ( ).toString ( );
    }

    public int getImageTypeIndex ( ) {
        return imageTypeSpinner.getSelectedItemPosition ( );
    }

    public boolean getImageTypeChoice ( ) {
        return imageTypeCheckBox.isChecked ( );
    }

    public int getOrientationIndex ( ) {
        return orientationRadioGroup.indexOfChild (
                getActivity ( ).findViewById (
                        orientationRadioGroup.getCheckedRadioButtonId ( ) )
        );
    }

    public boolean getOrientationChoice ( ) {
        return orientationCheckBox.isChecked ( );
    }

    public int getCategoryIndex ( ) {
        return categorySpinner.getSelectedItemPosition ( );
    }

    public boolean getCategoryChoice ( ) {
        return categoryCheckBox.isChecked ( );
    }

    public void initColorsChecks ( boolean[] colorsChecks ) {
        for (int i = 0; i < colorsChecks.length; i++) {
            if ( colorsChecks[i] ) (( Chip ) colorsChipGroup.getChildAt ( i )).setChecked ( true );
        }
    }

    public boolean[] getColorsChecks ( ) {
        boolean[] result = new boolean[colorsChipGroup.getChildCount ( )];
        for (int i = 0; i < result.length; i++)
            result[i] = (( Chip ) colorsChipGroup.getChildAt ( i )).isChecked ( );
        return result;
    }

    public boolean getColorsChoice ( ) {
        return colorsCheckBox.isChecked ( );
    }

    public void initColorsChoice ( boolean colorsChoice ) {
        colorsCheckBox.setChecked ( colorsChoice );
        setGroupVisibility ( colorsChoice , colorsChipGroup );
    }


    public void intEditorChoice ( boolean editorChoice ) {
        editorsChoiceCheckBox.setChecked ( editorChoice );
    }

    public boolean getEditorChoice ( ) {
        return editorsChoiceCheckBox.isChecked ( );
    }

    public boolean getSafeSearchChoice ( ) {
        return safeSearchCheckBox.isChecked ( );
    }

    public void initSafeSearchChoice ( boolean safeSearchChoice ) {
        safeSearchCheckBox.setChecked ( safeSearchChoice );
    }

    public Integer getOrderIndex ( ) {
        return orderRadioGroup.indexOfChild (
                getActivity ( ).findViewById (
                        orderRadioGroup.getCheckedRadioButtonId ( ) )
        );
    }

    public void initOrderIndex ( Integer orderIndex ) {
        (( RadioButton ) orderRadioGroup.getChildAt ( orderIndex )).setChecked ( true );
    }

    public boolean getOrderChoice ( ) {
        return orderCheckBox.isChecked ( );
    }

    public void initOrderChoice ( boolean orderChoice ) {
        orderCheckBox.setChecked ( orderChoice );
        setGroupVisibility ( orderChoice , orderRadioGroup );
    }

    @Override
    public void onDestroyView ( ) {
        super.onDestroyView ( );
        unbinder.unbind ( );
    }

    public void back ( ) {
        getActivity ( ).onBackPressed ( );
    }

}
