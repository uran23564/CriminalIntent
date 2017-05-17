package com.example.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by merz_konstantin on 5/13/17.
 */

public class CrimeLab { // Singleton-Klasse
    public static CrimeLab sCrimeLab;
    private List<Crime> mCrimes; // Crime-"array"

    public static CrimeLab get(Context context){ // stellt sicher, dass es maximal eine instanz von crimelab gibt
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return  sCrimeLab;
    }

    private CrimeLab(Context context){ // privater konstruktor
        mCrimes=new ArrayList<>();
        // fuer starter erstellen wir selber neue crimes -- werden wir bald nicht mehr brauchen
        for(int i=0;i<100;i++){
            Crime crime=new Crime();
            crime.setTitle("Crime #" +(i+1));
            crime.setSolved(i%2==0); // jede zweite untat ist geloest
            crime.setPoliceRequired(i%3==0); // jede dritte untat ist schwerwiegend
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes(){ return mCrimes;} // gibt Crime-Array zurueck

    public Crime getCrime(UUID id){ // gibt gesuchtes crime anhand der id zurueck
        for (Crime crime:mCrimes){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
    
    public void addCrime(Crime c){
        mCrimes.add(c);
    }

}
