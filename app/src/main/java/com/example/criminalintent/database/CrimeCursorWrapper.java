package com.example.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by merz_konstantin on 5/18/17.
 */
 
 public Class CrimeCursorWrapper extends CursorWrapper{
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
        
        Crime crime=new Crime(UUID.fromString(uuidString));
        crime.setTitle(title); // importiere java.util.date, NICHT java.sql.date
        crime.setLongDate(date);
        crime.setSolved(isSolved);
        crime.setPoliceRequired(isSerious);
        
        return crime;
    }
    
    
}