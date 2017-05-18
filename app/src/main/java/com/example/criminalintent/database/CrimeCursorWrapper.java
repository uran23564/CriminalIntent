package com.example.criminalintent.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.criminalintent.Crime;

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
        String uuidString=getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title=getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date=getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved=getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
        int isSerious=getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SERIOUS));
        
        Crime crime=new Crime(UUID.fromString(uuidString));
        crime.setTitle(title); // importiere java.util.date, NICHT java.sql.date
        crime.setLongDate(new Date(date));
        crime.setSolved(isSolved!=0);
        crime.setPoliceRequired(isSerious!=0);
        
        return crime;
    }
    
    
}