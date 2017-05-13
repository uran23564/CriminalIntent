package com.example.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by merz_konstantin on 5/13/17.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
