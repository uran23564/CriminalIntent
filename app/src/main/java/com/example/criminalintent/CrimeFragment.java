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
    private static final String ARG_CRIME_ID="crime_id";
    // private static final String ARG_CRIME="crime";

    private Crime mCrime; // eine Untat aus einer Liste von Untaten (die in CrimeListActivity bzw. CrimeListFragment gemanaged wird), wird in diesem Fragment bearbeitet

    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    
    public static CrimeFragment newInstance(UUID crimeId){ // methode wird von einer activity aufgerufen, die dieses fragment mit zusaetzlichen bundles erzeugen moechte -- hier soll dem erzeugten Fragment eine crimeId uebergeben werden
    // public static CrimeFragment newInstance(Crime crime){
    // argumente/bundles muessen dem Fragment NACH seiner Erzeugung aber BEVOR es zu einer activity zugefuegt wird, uebergeben wrden
        Bundle args=new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId); // speichert die vom Caller (CrimeActivity) uebergebene crimeId ab
        // args.putSerializable(ARG_CRIME,crime);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args); // uebergibt dem neu erzeugten Fragment die erzeugten Argumente
        return fragment;
    }
    

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // CrimeFragment wird durch newInstance erzeugt, d.h. ARG_CRIME_ID ist wohldefiniert
        UUID crimeId=(UUID) getArguments().getSerializable(ARG_CRIME_ID); // liest die crimeId aus dem Argument, mit dem das Fragment erzeugt wurde
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
        mTitleField.setText(mCrime.getTitle());
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
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }
}
