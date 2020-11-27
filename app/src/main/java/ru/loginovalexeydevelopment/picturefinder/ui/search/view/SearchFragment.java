package ru.loginovalexeydevelopment.picturefinder.ui.search.view;

import android.annotation.SuppressLint;
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
import ru.loginovalexeydevelopment.picturefinder.R;
import ru.loginovalexeydevelopment.picturefinder.model.entities.SearchOptions;
import ru.loginovalexeydevelopment.picturefinder.ui.search.viewmodel.SearchViewModel;


/**
 * Фрагмент для работы с параметрами поиска
 */
public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;

    private Unbinder unbinder;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.searchEditText)
    EditText searchEditText;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.imageTypeСheckBox)
    CheckBox imageTypeCheckBox;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.imageTypeSpinner)
    Spinner imageTypeSpinner;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.orientationCheckBox)
    CheckBox orientationCheckBox;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.orientationRadioGroup)
    RadioGroup orientationRadioGroup;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.categoryCheckBox)
    CheckBox categoryCheckBox;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.categorySpinner)
    Spinner categorySpinner;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.colorsCheckBox)
    CheckBox colorsCheckBox;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.colorsChipGroup)
    ChipGroup colorsChipGroup;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.editorsChoiceCheckBox)
    CheckBox editorsChoiceCheckBox;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.orderСheckBox)
    CheckBox orderCheckBox;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.orderRadioGroup)
    RadioGroup orderRadioGroup;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.searchButton)
    Button searchButton;


    public static SearchFragment getInstance ( ) {
        return new SearchFragment ( );
    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        return inflater.inflate ( R.layout.fragment_search_options , container , false );
    }


    @Override
    public void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );

        //Инициализация визуальных компонентов
        initButterKnife ( view );
        initSearchEditText ( );
        initImageCheckBox ( );
        initOrientationGroup ( );
        initCategoryCheckBox ( );
        initColorsGroup ( );
        initOrderGroup ( );
        searchButton.setOnClickListener ( view1 -> startSearch ( ) );

        initViewModel ( );
        observe ( );
    }

    private void observe ( ) {
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
            initOrderIndex ( searchOptions.getOrderIndex ( ) );
            initOrderChoice ( searchOptions.getOrderChoice ( ) );
        } );
    }

    //Действия при нажатии на кнопку "Поиск"
    private void startSearch ( ) {
        //Создание параметров поиска согласно выбранным элементам
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
                getOrderChoice ( ) ,
                getOrderIndex ( ) );
        //Передача новых параметров во ViewModel
        viewModel.actionSearch ( searchOptions );
        //Возвращение на предыдущий экран
        back ( );
    }

    private void initOrderGroup ( ) {
        for (int i = 0; i < getResources ( ).getStringArray ( R.array.order ).length; i++) {
            RadioButton radioButton = new RadioButton ( getContext ( ) );
            radioButton.setText ( getResources ( ).getStringArray ( R.array.orderText )[i] );
            orderRadioGroup.addView ( radioButton );
        }

        orderCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) ->
                setGroupVisibility ( isChecked , orderRadioGroup ) );
    }

    private void initColorsGroup ( ) {
        initColorsChips ( );

        colorsCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) -> setGroupVisibility ( isChecked , colorsChipGroup ) );
    }

    private void initColorsChips ( ) {
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
    }

    private void initCategoryCheckBox ( ) {
        categoryCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) ->
                setGroupVisibility ( isChecked , categorySpinner ) );
    }

    private void initOrientationGroup ( ) {
        for (int i = 0; i < getResources ( ).getStringArray ( R.array.orientationText ).length; i++) {
            RadioButton radioButton = new RadioButton ( getContext ( ) );
            radioButton.setText ( getResources ( ).getStringArray ( R.array.orientationText )[i] );
            orientationRadioGroup.addView ( radioButton );
        }

        orientationCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) ->
                setGroupVisibility ( isChecked , orientationRadioGroup ) );
    }

    private void initImageCheckBox ( ) {
        imageTypeCheckBox.setOnCheckedChangeListener ( ( compoundButton , isChecked ) ->
                setGroupVisibility ( isChecked , imageTypeSpinner ) );
    }

    private void initSearchEditText ( ) {
        searchEditText.setOnEditorActionListener ( ( textView , action , keyEvent ) -> {
            if ( action == EditorInfo.IME_ACTION_SEARCH ) {
                startSearch ( );
                return true;
            }
            return false;
        } );
    }

    private void initButterKnife ( @NonNull View view ) {
        unbinder = ButterKnife.bind ( this , view );
        ButterKnife.bind ( getActivity ( ) );
    }

    private void initViewModel ( ) {
        viewModel = ViewModelProviders.of ( getActivity ( ) ).get ( SearchViewModel.class );
        viewModel.onViewCreated ( );
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
        if ( isChecked )
            viewGroup.setVisibility ( View.VISIBLE );
        else
            viewGroup.setVisibility ( View.GONE );
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

    private boolean getSafeSearchChoice ( ) {
        return true;
    }


    public void intEditorChoice ( boolean editorChoice ) {
        editorsChoiceCheckBox.setChecked ( editorChoice );
    }

    public boolean getEditorChoice ( ) {
        return editorsChoiceCheckBox.isChecked ( );
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
