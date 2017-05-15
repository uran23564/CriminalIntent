package com.example.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;


public class CrimePagerActivity extends AppCompatActivity{
    private static final String EXTRA_CRIME_ID="com.example.criminalintent.crime_id";

    private ViewPager mViewPager;
    private Button mFirstCrime;
    private Button mLastCrime;
    
    private List<Crime> mCrimes;
    
    public static Intent newIntent(Context packageContext,UUID crimeId){
        Intent intent=new Intent(packageContext,CrimePagerActivity.class);
        intent.putExtras(EXTRA_CRIME_ID,crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_crime_pager);
        
        UUID crimeId=(UUID) getIntent.getSerializable(EXTRA_CRIME_ID);
        
        // initialisiere Widgets
        mViewPager=(ViewPager) findViewById(R.id.crime_view_pager); 
        mFirstCrime=(Button) findViewById(R.id.first_crime_button);
        mLastCrime=(Button) findViewById(R.id.last_crime_button);
        
        mCrimes=CrimeLab.get(this).getCrimes(); // rufe liste der crimes ab
        
        FragmentManager fragmentManager=getSupportFragmentManager();
        
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager){ // FragmentStatePagerAdapter vermittelt zwischen den Views des ViewPagers und der Modellschicht
        // der Adapter managed genauer gesagt die Fragments der entsprechenden Crimes
            @Override
            public Fragment getItem(int position){
                Crime crime=mCrimes.get(position);
                mFirstButton.setEnabled(position==0 ? false:true); // knopf deaktivieren, wenn wir an erster stelle sind
                mLastButton.setEnabled(position==mCrimes.size()-1 false:true); // knopf deaktivieren, wenn wir an letzter stelle sind
                return CrimeFragment.newInstance(crime.getId());
            }
            
            @Override
            public int getCount(){
                return mCrimes.size();
            }
        });
        
        for (int i=0;i<mCrimes.size();i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
        

        
        mFirstButton.setOnClickListener(new View.OnClickListener(){ // zur ersten Untat springen
            @Override
            public void onClick(View v){
                mViewPager.setCurrentItem(0);
            }
        });
        
        mLastButton.setOnClickListener(new View.OnClickListener(){ // zur ersten Untat springen
            @Override
            public void onClick(View v){
                mViewPager.setCurrentItem(mCrimes.size()-1);
            }
        });
    }
}