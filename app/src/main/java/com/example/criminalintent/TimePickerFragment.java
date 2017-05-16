package com.example.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment{
    private static final String ARG_TIME="time";
    public static final String EXTRA_TIME="com.example.criminalintent.time";
    
    private TimePicker mTimePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);
        // um die integer aus dem datum zu bekommen, muessen wir den umweg ueber ein calendar-objekt gehen.
        Date time=(Date) getArguments().getSerializable(ARG_TIME);
        final Calendar calendar=Calendar.getInstance();
        calendar.setTime(time);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        
        mTimePicker=(TimePicker) v.findViewById(R.id.dialog_time_picker);
        // stelle datum des angeklickten crimes ein
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) { // Methoden stehen nur bei Marshmellow zur Verfuegung
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }
        else { // fuer API-Level kleiner 23
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }
        
        return new AlertDialog.Builder(getActivity()).setView(v).
                                                    setTitle(R.string.date_picker_title).
                                                    setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog,int which){
                                // datum zusammenbasteln
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                                    Date time = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), mTimePicker.getHour(), mTimePicker.getMinute(), 0).getTime();
                                    sendResult(Activity.RESULT_OK,time); // wird der knopf gedrueckt, basteln wir uns ein datum aus dem zeug, was der user eingegeben hat, zusammen und schicken es anschliessend an CrimeFragment per intent zurueck
                                }
                                else{
                                    Date time = new GregorianCalendar(0, 0, 0, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute(), 0).getTime();
                                    sendResult(Activity.RESULT_OK,time); // wird der knopf gedrueckt, basteln wir uns ein datum aus dem zeug, was der user eingegeben hat, zusammen und schicken es anschliessend an CrimeFragment per intent zurueck
                                }

                            }
                                                    }).create();
        // zweites Argument von setPositiveButton ist ein Objekt, das DialogInterface.OnClickListener implementiert        
    }
    
    
    public static TimePickerFragment newInstance(Date time){ // angepasster konstruktor
        Bundle args=new Bundle();
        args.putSerializable(ARG_TIME,time);
        TimePickerFragment fragment=new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    private void sendResult(int resultCode, Date time){ // gibt ausgewaehltes datum zurueck an CrimeFragment, sodass CrimeFragment dieses im Modell setzen kann
        if(getTargetFragment()==null){
            return;
        }
        
        Intent intent=new Intent();
        intent.putExtra(EXTRA_TIME,time);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}