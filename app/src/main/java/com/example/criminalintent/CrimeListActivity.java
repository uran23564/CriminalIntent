package com.example.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by merz_konstantin on 5/13/17.
 */


public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{ // implementiert Callbacks, sodass das Fragment Methoden aufrufen kann
    // Callbacks fuer CrimeListFragment: damit das Crime im detail_container angezeigt wird
    // Callbacks fuer CrimeFragment: damit die Liste der Crimes aktualisiert wird, wenn ein Crime geaendert wird

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    // laedt ein CrimeFragment (entweder wie ueblich im neuen fenster, oder im detail_container)
    @Override
    public void onCrimeSelected(Crime crime){
        if (findViewById(R.id.detail_fragment_container)==null){ // Telefon
            Intent intent = CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(intent);
        }
        else{ // Tablet: lade CrimeFragment im detail_fragment_container
            Fragment newDetail=CrimeFragment.newInstance(crime.getId());

            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,newDetail).commit();
        }
    }

    // aktualisiert die liste, wenn ein crime geaendert wurde
    @Override
    public void onCrimeUpdated(Crime crime){
        CrimeListFragment listFragment=(CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeSwiped(Crime crime){
        CrimeLab.get(getApplicationContext()).deleteCrime(crime);
    }

}
