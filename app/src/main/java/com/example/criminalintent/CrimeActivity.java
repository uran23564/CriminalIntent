package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_ID="com.example.criminalintent.crime_id"; // keine andere klasse wird darauf zugreifen -- dient nur zum speichern der crimeId, die vom Caller uebergeben wird
    // private static final String EXTRA_CRIME="com.example.criminalintent.crime";

    public static Intent newIntent(Context packageContext, UUID crimeId){
    // public static Intent newIntent(Context packageContext,Crime crime){
        Intent intent = new Intent(packageContext,CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        // intent.putExtra(EXTRA_CRIME,crime);
        return intent;
    }
    
    @Override
    protected Fragment createFragment(){
        // return new CrimeFragment();
        UUID crimeId=(UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID); // liest die vom caller uebergebene Id aus und schreibt sie in eine lokale Variable
        // Crime crime=(Crime) getIntent().getSerializableExtra(EXTRA_CRIME);
        return CrimeFragment.newInstance(crimeId); // erzeugt ein Fragment mit Argumenten (hier der crimeId) und bekommt es zurueck
        // return CrimeFragment.newInstance(crime);
    }
}
