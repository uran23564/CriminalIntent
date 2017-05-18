package com.example.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.view.View.GONE;

/**
 * Created by merz_konstantin on 5/13/17.
 */

public class CrimeListFragment extends Fragment {
    private static final int REQUEST_CODE_CRIME=0;
    private static final int NOT_SERIOUS_CRIME=0;
    private static final int SERIOUS_CRIME=1;
    private static final int EMPTY_VIEW=2;
    private static final String SAVED_SUBTITLE_VISIBLE="subtitle"; // wenn geraet rotiert wird, sollen subtitles (un)sichtbar bleiben

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter; // jedes RecycleView braucht einen Adapter fuer das Erstellen von ViewHolders und die Arbeit
    // mit der Modellschicht

    private TextView mEmptyText;
    private Button mAddCrimeButton;

    private boolean mSubtitleVisible=false; // ist der Subtitle sichtbar (je nachdem, ob der entsprechende knopf gedrueckt wurde)?

    // private UUID lastId; // letzte UUID, die man angeschaut hat
    // private UUID[] maybeChangedIds;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // teilt dem FragmentManager mit, dass wir ein Menu haben, d.h. CrimeListFragment muss Menu-Callbacks vom OS empfangen koennen
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView=(RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // RecyclerView braucht einen LayoutManager,
        // der die Liste auf dem Screen erzeugt und sie managed.
        // Wir erzeugen hier einen LinearLayoutManager und uebergeben diesen an den RecyclerView.

        mEmptyText=(TextView) view.findViewById(R.id.empty_text);
        mAddCrimeButton=(Button) view.findViewById(R.id.add_crime);
        mAddCrimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Crime crime=new Crime();
                CrimeLab.get(getActivity()).addCrime(crime); // erzeugt neues Crime in der Singleton-Liste der vorhandenen Crimes

                Intent intent=CrimePagerActivity.newIntent(getActivity(),crime.getId()); // startet die CrimePagerActivity und zeigt das neu erstellte Crime sofort an, damit man es editieren kann.
                startActivity(intent);
            }
        });
        
        if(savedInstanceState!=null){
            mSubtitleVisible=savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        
        updateUI();
        return view;
    }
    
    @Override
    public void onResume(){ // wenn etwas in CrimeActivity bzw. CrimeFragment geaendert wird, soll der RecyclerView aktualisiert werden
        super.onResume();
        updateUI();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){ // erzeugt das menu
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        
        MenuItem subtitleItem=menu.findItem(R.id.show_subtitle);
        // jedes mal, wenn auf den show_subtitle-Knopf im Menu gedrueckt wird, soll das Menu neu erstellt werden (dies passiert in der onOptionsItemSelected-Methode) und der entsprechende text geladen werden
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){ // handlet, was passiert, wenn ein bestimmtes item im menu gedrueckt wurde
        // wenn was gedrueckt wurde, wird true zurueckgeben, damit wir wissen, dass was gedrueckt wurde
        switch(item.getItemId()){
            case R.id.new_crime: Crime crime=new Crime();
            CrimeLab.get(getActivity()).addCrime(crime); // erzeugt neues Crime in der Singleton-Liste der vorhandenen Crimes
            
            Intent intent=CrimePagerActivity.newIntent(getActivity(),crime.getId()); // startet die CrimePagerActivity und zeigt das neu erstellte Crime sofort an, damit man es editieren kann.
            startActivity(intent);
            return true;
            
            case R.id.show_subtitle: 
            mSubtitleVisible=!mSubtitleVisible; // wenn show_subtitle gedrueckt wurde, soll der boolean geupdatet und das menu neu geladen werden. dabei wird der text geupdatet (in der onCreateOptionsMenu-Methode)
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
            
            default: return super.onOptionsItemSelected(item);
        }
    }
    
    private void updateSubtitle(){
        CrimeLab crimeLab=CrimeLab.get(getActivity());
        // String subtitle=getString(R.string.subtitle_format,crimeLab.getCrimes().size());
        String subtitle=getResources().getQuantityString(R.plurals.subtitle_plural,crimeLab.getCrimes().size(),crimeLab.getCrimes().size());
        
        if(!mSubtitleVisible){ // wenn der subtitle nicht sichtbar ist, soll er das auch sein^^
            subtitle=null;
        }
        AppCompatActivity activity=(AppCompatActivity) getActivity(); // activity, die CrimeListFragment hostet wird gecastet
        activity.getSupportActionBar().setSubtitle(subtitle); // subtitle im menu wird aktualisiert
    }

    private void updateUI(){
        // ViewHolder des RecyclerView sollen aktualisiert werden
        CrimeLab crimeLab=CrimeLab.get(getActivity()); // Singleton crimeLab samt Liste der Crimes wird erzeugt bzw. abgefragt
        List <Crime> crimes=crimeLab.getCrimes(); // die Liste der erzeugten Crimes wird kopiert

        if(mAdapter==null){
            mAdapter=new CrimeAdapter(crimes); // Verbindung von RecycleView zur Modellschicht
            mCrimeRecyclerView.setAdapter(mAdapter); // Zuweiseung des Adapters
        }
        else{
            setCrimes(crimes);
            mAdapter.notifyDataSetChanged(); // ganze liste wird aktualisiert, obwohl hoechstens ein Crime geaendert wurde -> ineffizient bei CrimeActivity -- beim pageViewer jedoch das einzig sinnvolle, statt die ganze liste durchzuchecken
        }
        /*else{ // gibt es bereits einen adapter, so benachrichtige ihn, dass sich ein Crime geaendert haben koennte
            // mAdapter.notifyItemChanged(clickedLayoutPosition); // funktioniert nicht mehr, wenn wir von Crime zu Crime wischen koennen
            if(maybeChangedIds.length!=0) {
                for (int i = 0; i < crimes.size(); i++) { // checke, ob letztes gesehene Crime geaendert wurde
                    for (int j = 0; j < maybeChangedIds.length; j++) {
                        // if(crimes.get(i).getId().equals(lastId)){
                        if (crimes.get(i).getId().equals(maybeChangedIds[j])) {
                            mAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }
            else{ // nuetzlich, wenn wir CrimeActivity starten, da wir nur das zuletzt angesehene Crime aktualisieren muessen
                for (int i=0;i<crimes.size();i++){ // checke, ob letztes gesehene Crime geaendert wurde
                    if(crimes.get(i).getId().equals(lastId)){
                        mAdapter.notifyItemChanged(i);
                        break;
                        }
                    }
                }
            }*/

        updateSubtitle(); // wenn wir von CrimeFragment hierher zurueckkehren, soll auch der Subtitle aktualisiert werden (wenn z.B. ein Crime erzeugt oder vernichtet wurde)

        if(crimes.size()==0){
            mCrimeRecyclerView.setVisibility(GONE);
            mAddCrimeButton.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.VISIBLE);
        }
        else {
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.GONE);
            mAddCrimeButton.setVisibility(View.GONE);
        }
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
            mDateTextView.setText(mCrime.getLongDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE: GONE);
        }

        @Override
        public void onClick(View view){
            // Toast.makeText(getActivity(),mCrime.getTitle()+ " clicked!",Toast.LENGTH_SHORT).show();
            // Intent intent=CrimeActivity.newIntent(getActivity(),mCrime.getId());
            // Intent intent=CrimeActivity.newIntent(getActivity(),mCrime);
            Intent intent=CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivityForResult(intent,REQUEST_CODE_CRIME); // lade CrimeActivity mit zugehoeriger Id der Untat
            // startActivity(intent); // lade CrimeActivity mit zugehoeriger Id der Untat
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
            mDateTextView.setText(mCrime.getLongDate().toString());
            mSeriousTextView.setText(String.valueOf(mCrime.isPoliceRequired()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE: GONE);
            mCallPolice.setVisibility(crime.isSolved() ? GONE:View.VISIBLE); // Verstecke Knopf, wenn der Fall geloest ist
        }

        @Override
        public void onClick(View view){
            // Toast.makeText(getActivity(),mCrime.getTitle()+ " clicked!",Toast.LENGTH_SHORT).show();
            // Intent intent=CrimeActivity.newIntent(getActivity(),mCrime.getId());
            // Intent intent=CrimeActivity.newIntent(getActivity(),mCrime);
            Intent intent=CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivityForResult(intent,REQUEST_CODE_CRIME); // lade CrimeActivity mit zugehoeriger Id der Untat
            // startActivity(intent); // lade CrimeActivity mit zugehoeriger Id der Untat
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
            if(viewType==NOT_SERIOUS_CRIME) {
                return new CrimeHolder(layoutInflater, parent); // erstellt einen neuen CrimeHolder
            }
            if(viewType==SERIOUS_CRIME){
                return new SeriousCrimeHolder(layoutInflater,parent); // erstellt einen SeriousCrimeHolder
            }
            return null;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){ // fuettert den ViewHolder mit Daten aus der Modellschicht
        // clickedLayoutPosition=position;
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
        
        // aktualisiert Crime-Liste, wenn layout neu erzeugt wird
        public void setCrimes(List<Crime> crimes){
            mCrimes=crimes;
        }

        @Override
        public int getItemViewType(int position){ // ueberlade getItemViewType, je nachdem, ob das Verbrechen schwerwiegend ist
            if(!mCrimes.get(position).isPoliceRequired()){
                return NOT_SERIOUS_CRIME; // bei normalen Untaten Null zurueckgeben
            }
            if(mCrimes.get(position).isPoliceRequired()){
                return SERIOUS_CRIME; // bei schweren Untaten Eins zurueckgeben
            }
            if(mCrimes.size()==0){
                return EMPTY_VIEW; // wir haben keine crimes --> zeige ein anderes view an
            }
            else{ return 0;}
        }
    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_CODE_CRIME){
            // lastId=(UUID) data.getSerializableExtra(CrimeFragment.EXTRA_UUID);
            maybeChangedIds=(UUID[]) data.getSerializableExtra(CrimeFragment.EXTRA_UUID);
        }
    }*/
    
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }
    
}
