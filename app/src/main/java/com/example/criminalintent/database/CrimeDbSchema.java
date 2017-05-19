package com.example.criminalintent.database;

/**
 * Created by merz_konstantin on 5/17/17.
 */
 
 // Definition des Schemas der Tabelle mit Hilfe von Java-Code
 // Definiert Name der Tabelle und ihre Spalten

public class CrimeDbSchema {
    public static final class CrimeTable{ // CrimeTable-Klasse dient nur zur Definition von Strings, um die Tabelle zu organisieren. Als Erstes definieren wir den Namen der Tabelle
        public static final String NAME="crimes";

        public static final class Cols{ // hier werden die Strings der Spalten gespeichert
            public static final String UUID="uuid";
            public static final String TITLE="title";
            public static final String DATE="date";
            public static final String SOLVED="solved";
            public static final String SERIOUS="serious";
            public static final String SUSPECT="suspect";
            // public static final String PHONENUMBER="phonenumber";
        }
    }
}
