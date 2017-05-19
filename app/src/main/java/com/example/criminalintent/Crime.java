package com.example.criminalintent;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by merz_konstantin on 5/7/17.
 */

public class Crime {
    private UUID mId; // identifiziert ein Crime-Objekt
    private String mTitle; // Titel der Untat
    private Date mDate; // Wann ist die Untat geschehen?
    private boolean mSolved; // Wurde das Verbrechen geloest?
    private boolean mRequiresPolice; // ernsthaftes Verbrechen?
    private String mSuspect; // Verdaechtiger
    private Uri mSuspectPhoneNumber; // Telefonnummer des Verdaechtigen

    public Crime(){
        // mId=UUID.randomUUID(); // zufaellige, aber eindeutige identifikationsnummer (universally unique ID)
        // mDate=new Date(); // Datum des Geschehens wird auf "heute" gesetzt
        this(UUID.randomUUID());
        mRequiresPolice=false; // normalerweise ist die Untat nicht so schlimm
    }
    
    public Crime(UUID id){
        mId=id;
        mDate=new Date();
        mRequiresPolice=false; // normalerweise ist die Untat nicht so schlimm
    }

    // getter
    public UUID getId(){ return mId;}
    public String getTitle(){ return mTitle;}
    
    // public Date getDate(){
    public String getDate(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(mDate);
        // return new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).getTime();
        // android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        // return dateFormat.format("dd.MM.yyyy",calendar.getTime());
        // wuerde das auch mit SimpleDateFormat gehen?
        SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(calendar.getTime());
    }
    
    public String getTime(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(mDate);
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        return sdf.format(calendar.getTime());
    }
    
    public Date getLongDate(){
        return mDate;
    }
    
    public boolean isSolved(){ return mSolved;}
    public boolean isPoliceRequired(){ return mRequiresPolice;}
    public String getSuspect(){ return mSuspect;}
    public Uri getSuspectPhoneNumber(){ return mSuspectPhoneNumber; }

    
    // setter (UUID wird nie veraendert!)
    public void setTitle(String title){mTitle=title;}
    
    public void setDate(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Calendar tCalendar=Calendar.getInstance(); // uebernehme alte Zeit
        tCalendar.setTime(mDate);
        // mDate=date;
        mDate=new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),tCalendar.get(Calendar.HOUR_OF_DAY),tCalendar.get(Calendar.MINUTE)).getTime();
    }
    
    public void setTime(Date time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(time);
        Calendar dCalendar=Calendar.getInstance(); // uebernehme altes datum
        dCalendar.setTime(time);
        mDate=new GregorianCalendar(dCalendar.get(Calendar.YEAR),dCalendar.get(Calendar.MONTH),dCalendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)).getTime();
    }
    
    public void setLongDate(Date date){mDate=date;}
    public void setSolved(boolean solved){mSolved=solved;}
    public void setPoliceRequired(boolean policeRequired){ mRequiresPolice=policeRequired;}
    public void setSuspect(String suspect){mSuspect=suspect;}
    // public void setSuspectPhoneNumber(Uri number){mSuspectPhoneNumber=number;}
}
