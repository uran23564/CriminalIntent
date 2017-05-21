package com.example.criminalintent.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.criminalintent.Crime;
import com.example.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by merz_konstantin on 5/18/17.
 */
 
public class CrimeCursorWrapper extends CursorWrapper{
    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }
    
    // importiere CrimeTable
    public Crime getCrime(){
        String uuidString=getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title=getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date=getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved=getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        int isSerious=getInt(getColumnIndex(CrimeTable.Cols.SERIOUS));
        String suspect=getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
        long suspectId=getLong(getColumnIndex(CrimeTable.Cols.SUSPECTID));
        String suspectPhone=getString(getColumnIndex(CrimeTable.Cols.SUSPECTPHONE));
        
        Crime crime=new Crime(UUID.fromString(uuidString));
        crime.setTitle(title); // importiere java.util.date, NICHT java.sql.date
        crime.setLongDate(new Date(date));
        crime.setSolved(isSolved!=0);
        crime.setPoliceRequired(isSerious!=0);
        crime.setSuspect(suspect);
        crime.setSuspectId(suspectId);
        crime.setSuspectPhoneNumber(suspectPhone);
        
        return crime;
    }
    
    
}