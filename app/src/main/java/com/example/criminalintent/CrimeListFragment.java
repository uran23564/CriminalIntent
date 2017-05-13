package com.example.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by merz_konstantin on 5/13/17.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter; // jedes RecycleView braucht einen Adapter fuer das Erstellen von ViewHolders und die Arbeit
    // mit der Modellschicht

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView=(RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // RecyclerView braucht einen LayoutManager,
        // der die Liste auf dem Screen erzeugt und sie managed.
        // Wir erzeugen hier einen LinearLayoutManager und uebergeben diesen an den RecyclerView.
        updateUI();
        return view;
    }

    private void updateUI(){
        CrimeLab crimeLab=CrimeLab.get(getActivity()); // Singleton crimeLab wird erzeugt bzw. abgefragt
        List <Crime> crimes=crimeLab.getCrimes(); // die Liste der erzeugten Crimes wird kopiert

        mAdapter=new CrimeAdapter(crimes); // Verbindung zur Modellschicht
        mCrimeRecyclerView.setAdapter(mAdapter);
    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{ // ViewHolder als innere Klasse
        // Jeder ViewHolder, haelt genau einen View. Dieser wird nun mit Daten aus der Modellschicht gefuettert
        // CrimeHolder ist durch diese Definition als Empfaenger von Klicks gesetzt -- also onClickListener implementieren
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){ // Initialisierung eines ViewHolders
            super(inflater.inflate(R.layout.list_item_crime,parent,false)); // ViewHolder inflated ein Objekt der Liste
            itemView.setOnClickListener(this);

            mTitleTextView=(TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView=(TextView) itemView.findViewById(R.id.crime_date);
        }

        public void bind(Crime crime){ // fuettert die Widgets mit Daten aus der Modellschicht
            mCrime=crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }

        @Override
        public void onClick(View view){
            Toast.makeText(getActivity(),mCrime.getTitle()+ " clicked!",Toast.LENGTH_SHORT).show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> { // Adaper als innere Klasse
        // Adapter sitzt zwischen RecyclerView und den Daten, die der RecyclerView darstellen soll.
        // Der Adapter ist fuer die Erstellung der ViewHolder und das Verknuepfen von Daten der Modellschicht mit dem ViewHolder verantwortlich.
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes=crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType){ // Adapter erstellt die ViewHolder
            // Adapter von RecycleView erstellt gerade so viele ViewHolders, wie auf den Bildschirm passen (z. B. Elf). Beim Scrollen
            // werden die Views der Viewholder ersetzt
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater,parent); // erstellt einen neuen ViewHolder, insb. hier einen CrimeHolder
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position){ // fuettert den ViewHolder mit Daten aus der Modellschicht
            Crime crime=mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount(){
            return mCrimes.size();
        }
    }
}
