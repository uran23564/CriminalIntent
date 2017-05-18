package com.example.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminalintent.database.CrimeBaseHelper;
import com.example.criminalintent.database.CrimeCursorWrapper;
import com.example.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by merz_konstantin on 5/13/17.
 */

public class CrimeLab { // Singleton-Klasse
    public static CrimeLab sCrimeLab;
    // private List<Crime> mCrimes; // Crime-"array"; Ab jetzt nur noch mit SQL!
    
    private Context mContext;
    private SQLiteDatabase mDatabase; // hier werden die Crimes fuer immer gespeichert sein

    public static CrimeLab get(Context context){ // stellt sicher, dass es maximal eine instanz von crimelab gibt
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return  sCrimeLab;
    }

    private CrimeLab(Context context){ // privater konstruktor -- initialisiert auch die datenbank bzw. greift auf sie zu bzw. updatet sie, falls sie eine aeltere versionsnummer traegt (all diese arbeiten werden von CrimeBaseHelper und seinen Methoden uebernommen
        mContext=context.getApplicationContext(); // rufe getApplicationContext() auf, da activities kommen und gehen -- der ApplicationContext bleibt jedoch so lange bestehen, bis die ganze app aus dem speicher geloescht wird. dies ist gerade das verhalten, das wir von unserem Singleton wuenschen
        mDatabase=new CrimeBaseHelper(mContext).getWritableDatabase();
        // mCrimes=new ArrayList<>(); Ab jetzt nur noch SQL

        // fuer starter erstellen wir selber neue crimes -- werden wir bald nicht mehr brauchen
/*        for(int i=0;i<100;i++){
            Crime crime=new Crime();
            crime.setTitle("Crime #" +(i+1));
            crime.setSolved(i%2==0); // jede zweite untat ist geloest
            crime.setPoliceRequired(i%3==0); // jede dritte untat ist schwerwiegend
            mCrimes.add(crime);
        }*/
    }

    public List<Crime> getCrimes(){
        // return mCrimes; // gibt Crime-Array zurueck // ab jetzt nur noch SQL!
        List<Crime> crimes=new ArrayList<>();
        CrimeCursorWrapper cursor=queryCrimes(null,null);
        
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){ // cursor zeigen auf eine reihe in der datenbank, deshalb while
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close(); // aufraeumen nicht vergessen!
        }
        
        return crimes;
    } 

    public Crime getCrime(UUID id){ // gibt gesuchtes crime anhand der id zurueck
//         for (Crime crime:mCrimes){
//             if(crime.getId().equals(id)){
//                 return crime;
//             }
//         }
        // ab jetzt nur noch SQL!
        CrimeCursorWrapper cursor=queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[] { id.toString() }); // sucht in der datenbank nach der zeile mit entsprechender id; cursor zeigt dann darauf
        
        try{
            if(cursor.getCount()==0){ // vll gabs den crime nicht
                return null;
            }
            cursor.moveToFirst(); // zeige auf den crime und gib ihn anschliessend zurueck
            return cursor.getCrime();
        }
        finally {
            cursor.close(); // aufraeumen nicht vergessen!
        }
    }
    
    
    // importiere CrimeTable
    // Uebersetze Crimes in ContentValues
    private static ContentValues getContentValues(Crime crime){
        ContentValues values=new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeTable.Cols.DATE,crime.getLongDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SERIOUS,crime.isPoliceRequired() ? 1 : 0);
        
        return values;
    }
    
    public void updateCrime(Crime crime){ // aktualisiert die liste der Crimes
        String uuidString=crime.getId().toString();
        ContentValues values = getContentValues(crime);
        
        mDatabase.update(CrimeTable.NAME,values, CrimeTable.Cols.UUID+ " = ?",new String[] { uuidString });
        // letztes argument spezifiziert, welche Reihe aktualisiert werden soll
        // "=?" stellt sicher, dass kein SQL-Code, der im string sein koennte, sondern nur der reine String uebergeben wird
    }
    
    // Crimes aus Datenbank auslesen (a la SQL)
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(CrimeTable.NAME, // Name der Tabelle, die abgefragt werden soll
                                        null, // welche Spalten sollen abgefragt werden? null bedeutet alle
                                        whereClause,
                                        whereArgs,
                                        null, // groupBy
                                        null, // having
                                        null); // orderBy
        return new CrimeCursorWrapper(cursor);
    }
    
    public void addCrime(Crime c){
        // mCrimes.add(c); ab jetzt nur noch SQL
        ContentValues values=getContentValues(c);
        mDatabase.insert(CrimeTable.NAME,null,values); // erstes Argument: Name der Datenbank, wo wirs reinstecken wollen. Drittes Argument sind die zu uebergebenden Werte
        // zweites Argument "nullCollumnHack": man kann hier z.b. eine uuid eingeben, die values aber leer lassen --> erzeugt neue zeile mit leerem inhalt
    }
    
    public void deleteCrime(Crime c){
        // mCrimes.remove(c); ab jetzt nur noch SQL
        ContentValues values=getContentValues(c);
        mDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID + " = ?", new String[] { c.getId().toString() });
    }

}
