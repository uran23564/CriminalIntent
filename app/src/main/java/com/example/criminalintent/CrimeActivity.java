package com.example.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CrimeActivity extends SingleFragmentActivity {
    public static final String EXTRA_CRIME_ID="com.example.criminalintent.crime_id";

    public static Intent newIntent(Context packageContext,UUID crimeId){
        Intent intent = new Intent(packageContext,CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }
    
    @Override
    protected Fragment createFragment(){
        return new CrimeFragment();
    }
}
