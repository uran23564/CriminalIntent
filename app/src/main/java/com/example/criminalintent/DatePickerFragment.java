package com.example.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

public class DatePickerFragment extends DialogFragment{
    private static final String ARG_DATE="date";
    private static final String EXTRA_DATE="com.example.criminalintent.date";
    
    private DatePicker mDatePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);
        // um die integer aus dem datum zu bekommen, muessen wir den umweg ueber ein calendar-objekt gehen.
        Date date=(Date) getArguments.getSerializable(ARG_DATE);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        
        mDatePicker=(DatePicker) v.findViewById(R.id.dialog_date);
        mDatePicker.init(year,month,day,null);
        
        return new AlertDialog.Builder(getActivity()).setView(v).
                                                    setTitle(R.string.date_picker_title).
                                                    setPositiveButton(android.R.String.ok,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog,int which){
                                // datum zusammenbasteln
                                Date date=new GregorianCalendar(mDatePicker.getYear(),mDatePicker.getMonth(),mDatePicker.getDayOfMonth()).getTime();
                                
                                sendResult(Activity.RESULT_OK,date); // wird der knopf gedrueckt, basteln wir uns ein datum aus dem zeug, was der user eingegeben hat, zusammen und schicken es anschliessend an CrimeFragment per intent zurueck
                            }
                                                    }).create();
        // zweites Argument von setPositiveButton ist ein Objekt, das DialogInterface.OnClickListener implementiert        
    }
    
    
    public static DatePickerFragment newInstance(Date date){ // angepasster konstruktor
        Bundle args=new Bundle();
        args.putSerializable(ARG_DATE,date);
        DatePickerFragment fragment=new DatePickerFragment();
        fragment.setArguments(date);
        return fragment;
    }
    
    private void sendResult(int resultCode, Date date){ // gibt ausgewaehltes datum zurueck an CrimeFragment, sodass CrimeFragment dieses im Modell setzen kann
        if(getTargetFragment()==null){
            return;
        }
        
        Intent intent=new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment.onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}