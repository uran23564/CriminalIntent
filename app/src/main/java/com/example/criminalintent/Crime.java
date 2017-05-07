package com.example.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by merz_konstantin on 5/7/17.
 */

public class Crime {
    private UUID mId; // identifiziert ein Crime-Objekt
    private String mTitle; // Titel der Untat
    private Date mDate; // Wann ist die Untat geschehen?
    private boolean mSolved; // Wurde das Verbrechen geloest?

    public Crime(){
        mId=UUID.randomUUID(); // zufaellige, aber eindeutige identifikationsnummer (universally unique ID)
        mDate=new Date(); // Datum des Geschehens wird auf "heute" gesetzt
    }

    // getter
    public UUID getId(){ return mId;}
    public String getTitle(){ return mTitle;}
    public Date getDate(){return mDate;}
    public boolean isSolved(){ return mSolved;}

    // setter (UUID wird nie veraendert!)
    public void setTitle(String title){mTitle=title;}
    public void setmDate(Date date){mDate=date;}
    public void setSolved(boolean solved){mSolved=solved;}
}
