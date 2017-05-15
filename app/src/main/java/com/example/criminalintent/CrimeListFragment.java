package com.example.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
        CrimeLab crimeLab=CrimeLab.get(getActivity()); // Singleton crimeLab samt Liste der Crimes wird erzeugt bzw. abgefragt
        List <Crime> crimes=crimeLab.getCrimes(); // die Liste der erzeugten Crimes wird kopiert

        mAdapter=new CrimeAdapter(crimes); // Verbindung von RecycleView zur Modellschicht
        mCrimeRecyclerView.setAdapter(mAdapter); // Zuweiseung des Adapters
    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{ // ViewHolder als innere Klasse
        // Jeder ViewHolder, haelt genau einen View. Dieser wird nun mit Daten aus der Modellschicht gefuettert
        // CrimeHolder ist durch diese Definition als Empfaenger von Klicks gesetzt -- also onClickListener implementieren
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){ // Initialisierung eines ViewHolders und seiner Widgets
            super(inflater.inflate(R.layout.list_item_crime, parent, false)); // ViewHolder inflated ein Objekt der Liste
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView=(ImageView) itemView.findViewById(R.id.crime_solved); // Handschellen bedeuten, dass der Fall geloest ist
        }

        public void bind(Crime crime){ // fuettert die Widgets mit Daten aus der Modellschicht
            mCrime=crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE:View.GONE);
        }

        @Override
        public void onClick(View view){
            // Toast.makeText(getActivity(),mCrime.getTitle()+ " clicked!",Toast.LENGTH_SHORT).show();
            Intent intent=CrimeActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent); // lade CrimeActivity mit zugehoeriger Id der Untat
        }

    }

    private class SeriousCrimeHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{ // ViewHolder fuer schlimme Untaten
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mSeriousTextView;
        private Button mCallPolice;
        private ImageView mSolvedImageView;

        private Crime mCrime;

        public SeriousCrimeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_serious_crime,parent,false));
            itemView.setOnClickListener(this);

            mTitleTextView=(TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView=(TextView) itemView.findViewById(R.id.crime_date);
            mSeriousTextView=(TextView) itemView.findViewById(R.id.crime_serious);
            mCallPolice=(Button) itemView.findViewById(R.id.call_police);
            mSolvedImageView=(ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind (Crime crime){
            mCrime=crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSeriousTextView.setText(String.valueOf(mCrime.isPoliceRequired()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE:View.GONE);
            mCallPolice.setVisibility(crime.isSolved() ? View.GONE:View.VISIBLE); // Verstecke Knopf, wenn der Fall geloest ist
        }

        @Override
        public void onClick(View view){
            Toast.makeText(getActivity(),mCrime.getTitle()+ " clicked!",Toast.LENGTH_SHORT).show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> { // Adaper als innere Klasse
        // Adapter managed ganz allgemein ViewHolder-Objekte -- nicht nur CrimeHold-Objekte
        // Adapter sitzt zwischen RecyclerView und den Daten, die der RecyclerView darstellen soll.
        // Der Adapter ist fuer die Erstellung der ViewHolder und das Verknuepfen von Daten der Modellschicht mit dem ViewHolder verantwortlich.
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){ // Konstruktor
            mCrimes=crimes; // Zuweisung der Crime-Liste aus dem Singleton
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){ // Adapter erstellt die ViewHolder
            // Adapter von RecycleView erstellt gerade so viele ViewHolders, wie auf den Bildschirm passen (z. B. Elf). Beim Scrollen
            // werden die Views der Viewholder ersetzt
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if(viewType==0) {
                return new CrimeHolder(layoutInflater, parent); // erstellt einen neuen CrimeHolder
            }
            if(viewType==1){
                return new SeriousCrimeHolder(layoutInflater,parent);
            }
            return null;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){ // fuettert den ViewHolder mit Daten aus der Modellschicht
            if(holder.getItemViewType()==0) {
                CrimeHolder crimeHolder=(CrimeHolder)holder;
                crimeHolder.bind(mCrimes.get(position)); // bindet den Crime an der Stelle "position" im array an einen ViewHolder
            }
            if(holder.getItemViewType()==1){
                SeriousCrimeHolder seriousCrimeHolder=(SeriousCrimeHolder)holder;
                seriousCrimeHolder.bind(mCrimes.get(position));
            }
        }

        @Override
        public int getItemCount(){
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position){ // ueberlade getItemViewType, je nachdem, ob das Verbrechen schwerwiegend ist
            if(!mCrimes.get(position).isPoliceRequired()){
                return 0; // bei normalen Untaten Null zurueckgeben
            }
            else{ return 1;} // bei schweren Untaten Eins zurueckgeben
        }
    }
}
