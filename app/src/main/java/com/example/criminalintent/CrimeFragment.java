package com.example.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * Created by merz_konstantin on 5/7/17.
 */

public class CrimeFragment extends Fragment {
    private Crime mCrime; // erstmal nur eine Untat

    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // mCrime=new Crime();
        UUID crimeId=(UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    // Layout wird nicht von onCreate erzeugt, sondern von dieser Methode, die das aufgeblasene View an die activity gibt, wenn die activity diese methode aufruft
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_crime,container,false);
        // wir werden den view im code der activity schreiben, weshalb dritter parameter false ist

        // widgets konfigurieren
        mTitleField=(EditText) v.findViewById(R.id.crime_title); // findViewById muss vom view des fragments aufgerufen werden
        mDateButton=(Button) v.findViewById(R.id.crime_date);
        mSolvedCheckBox=(CheckBox) v.findViewById(R.id.crime_solved);


        // Titel setzen
        mTitleField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                // TODO
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // TODO
            }
        });

        // Datum setzen
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false); // spaeter erlauben wir es ein datum einzugeben

        // Fall geloest setzen
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });



        return v;
    }
}
