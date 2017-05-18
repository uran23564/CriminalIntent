package com.example.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by merz_konstantin on 5/17/17.
 */
 
 // Erzeugt die eigentliche Datenbank

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE_NAME="crimeBase.db";

    public CrimeBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // importiere nur CrimeTable-Klasse
        // Tabelle mit dem Namen und allen Spalten in CrimeTable wird erzeugt
        db.execSQL("create table" + CrimeDbSchema.CrimeTable.NAME
                    + "(" + "_id integer primary key  autoincrement"
                    + CrimeTable.Cols.UUID + ", "
                    + CrimeTable.Cols.TITLE + ", "
                    + CrimeTable.Cols.DATE + ", "
                    + CrimeTable.Cols.SOLVED + ", "
                    + CrimeTable.Cols.SERIOUS +")"); 
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // TODO
    }
}
