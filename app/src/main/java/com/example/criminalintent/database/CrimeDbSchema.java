package com.example.criminalintent.database;

/**
 * Created by merz_konstantin on 5/17/17.
 */

public class CrimeDbSchema {
    public static final class CrimeTable{
        public static final String NAME="crimes";

        public static final class Cols{
            public static final String UUID="uuid";
            public static final String TITLE="title";
            public static final String DATE="date";
            public static final String SOLVED="solved";
            public static final String SERIOUS="serious";
        }
    }
}
