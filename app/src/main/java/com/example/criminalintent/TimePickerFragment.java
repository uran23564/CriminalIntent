package com.example.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

public class TimePickerFragment extends DialogFragment{
    private static final String ARG_TIME="time";
    private static final String EXTRA_TIME="com.example.criminalintent.time";
    
    private TimePicker mTimePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);
        // um die integer aus dem datum zu bekommen, muessen wir den umweg ueber ein calendar-objekt gehen.
        Date date=(Date) getArguments.getSerializable(ARG_DATE);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        
        mTimePicker=(DatePicker) v.findViewById(R.id.dialog_date);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);
        
        return new AlertDialog.Builder(getActivity()).setView(v).
                                                    setTitle(R.string.date_picker_title).
                                                    setPositiveButton(android.R.String.ok,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog,int which){
                                // datum zusammenbasteln
                                Date date=new GregorianCalendar(null,null,null,mDatePicker.getHour(),mDatePicker.getMinute(),null).getTime();
                                
                                sendResult(Activity.RESULT_OK,date); // wird der knopf gedrueckt, basteln wir uns ein datum aus dem zeug, was der user eingegeben hat, zusammen und schicken es anschliessend an CrimeFragment per intent zurueck
                            }
                                                    }).create();
        // zweites Argument von setPositiveButton ist ein Objekt, das DialogInterface.OnClickListener implementiert        
    }
    
    
    public static TimePickerFragment newInstance(Date time){ // angepasster konstruktor
        Bundle args=new Bundle();
        args.putSerializable(ARG_TIME,time);
        TimePickerFragment fragment=new TimePickerFragment();
        fragment.setArguments(time);
        return fragment;
    }
    
    private void sendResult(int resultCode, Date time){ // gibt ausgewaehltes datum zurueck an CrimeFragment, sodass CrimeFragment dieses im Modell setzen kann
        if(getTargetFragment()==null){
            return;
        }
        
        Intent intent=new Intent();
        intent.putExtra(EXTRA_TIME,time);
        getTargetFragment.onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}