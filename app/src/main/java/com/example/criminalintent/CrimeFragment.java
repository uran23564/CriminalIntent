package com.example.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * Created by merz_konstantin on 5/7/17.
 */

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID="crime_id";
    // private static final String ARG_CRIME="crime";
    public static final String EXTRA_UUID="crime_id_back"; // zum zuruecksenden
    private static final String DIALOG_DATE="DialogDate"; // tag fuer den alertdialog, damit dieser identifiziert und aufgerufen werden kann
    private static final String DIALOG_TIME="DialogTime"; // tag fuer den alertdialog, damit dieser identifiziert und aufgerufen werden kann
    private static final String DIALOG_PHOTO="DialogPhoto";
    private static int REQUEST_DATE=0; // zum identifizieren des kind-fragments, von dem wir eine antwort erwarten -- hier DatePickerFragment
    private static int REQUEST_TIME=1; // zum identifizieren des kind-fragments --hier TimePickerFragment
    private static int REQUEST_CONTACT=2; // wir erwarten eine antwort von der kontakt-app
    private static int REQUEST_PHOTO=3; // wir erwarten ein foto, welches an einem vorgegebenem pfad gespeichert werden soll
    private static int REQUEST_ZOOMED_PHOTO=4;

    private Crime mCrime; // eine Untat aus einer Liste von Untaten (die in CrimeListActivity bzw. CrimeListFragment gemanaged wird), wird in diesem Fragment bearbeitet
    // private UUID[] maybeChangedIds; // moeglicherweise veraenderte Crimes
    // private int counterChangedIds; // Zaehler der moeglicherweise veraenderten Crimes

    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private CheckBox mSeriousCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    
    
    public static CrimeFragment newInstance(UUID crimeId){ // methode wird von einer activity aufgerufen, die dieses fragment mit zusaetzlichen bundles erzeugen moechte -- hier soll dem erzeugten Fragment eine crimeId uebergeben werden
    // public static CrimeFragment newInstance(Crime crime){
    // argumente/bundles muessen dem Fragment NACH seiner Erzeugung aber BEVOR es zu einer activity zugefuegt wird, uebergeben wrden
        Bundle args=new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId); // speichert die vom Caller (CrimeActivity) uebergebene crimeId ab
        // args.putSerializable(ARG_CRIME,crime);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args); // uebergibt dem neu erzeugten Fragment die erzeugten Argumente
        return fragment;
    }
    
    
    // MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){ // erzeugt das menu
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){ // handlet, was passiert, wenn ein bestimmtes item im menu gedrueckt wurde
        // wenn was gedrueckt wurde, wird true zurueckgeben, damit wir wissen, dass was gedrueckt wurde
        switch(item.getItemId()){
            case R.id.delete_crime: CrimeLab.get(getActivity()).deleteCrime(mCrime);
            getActivity().finish();
            return true;
            
            
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // CrimeFragment wird durch newInstance erzeugt, d.h. ARG_CRIME_ID ist wohldefiniert
        UUID crimeId=(UUID) getArguments().getSerializable(ARG_CRIME_ID); // liest die crimeId aus dem Argument, mit dem das Fragment erzeugt wurde
        mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);
        // mCrime=(Crime) getArguments().getSerializable(ARG_CRIME);
        
        // teilt dem FragmentManager mit, dass wir ein Menu haben, d.h. CrimeListFragment muss Menu-Callbacks vom OS empfangen koennen
        setHasOptionsMenu(true);

        mPhotoFile=CrimeLab.get(getActivity()).getPhotoFile(mCrime); // Pfad zum Foto des Crimes
    }
    
    // wird ein Crime geaendert, so soll die SQL-Datenbank aktualisiert werden
    @Override
    public void onPause(){
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    // Layout wird nicht von onCreate erzeugt, sondern von dieser Methode, die das aufgeblasene View an die activity gibt, wenn die activity diese methode aufruft
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_crime,container,false);
        // wir werden den view im code der activity schreiben, weshalb dritter parameter false ist

        PackageManager packageManager=getActivity().getPackageManager();

        final Intent pickContact=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); // intent fuer kontaktabfrage
        final Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // intent fuers ausloesen der kamera

        // widgets konfigurieren
        mTitleField=(EditText) v.findViewById(R.id.crime_title); // findViewById muss vom view des fragments aufgerufen werden
        mDateButton=(Button) v.findViewById(R.id.crime_date);
        mTimeButton=(Button) v.findViewById(R.id.crime_time);
        mSolvedCheckBox=(CheckBox) v.findViewById(R.id.crime_solved);
        mSeriousCheckBox=(CheckBox) v.findViewById(R.id.crime_serious);
        mReportButton=(Button) v.findViewById(R.id.crime_report);
        mSuspectButton=(Button) v.findViewById(R.id.crime_suspect);
        mCallButton=(Button) v.findViewById(R.id.crime_call);
        // mCallButton.setEnabled(false); // wird erst entsperrt, wenn wir wirklich eine telefonnummer bekommen koennen
        mPhotoButton=(ImageButton) v.findViewById(R.id.crime_camera);
        mPhotoView=(ImageView) v.findViewById(R.id.crime_photo);



        // Titel setzen
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                // TODO
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // TODO
            }
        });


        // Datum setzen
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager=getFragmentManager();
                DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getLongDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE); // TargetFragment ist das Fragment, das Daten von einem anderen Fragment bekommen soll, wenn es zerstoert wird. wir haben somit eine Verbindung zwischen CrimeFragment un DatePickerFragment
                dialog.show(manager,DIALOG_DATE);
            }
        });

              
        // Zeit setzen
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager=getFragmentManager();
                TimePickerFragment dialog=TimePickerFragment.newInstance(mCrime.getLongDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME); // TargetFragment ist das Fragment, das Daten von einem anderen Fragment bekommen soll, wenn es zerstoert wird. wir haben somit eine Verbindung zwischen CrimeFragment un DatePickerFragment
                dialog.show(manager,DIALOG_TIME);
            }
        });
        
        
        // Fall geloest setzen
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                //collectChanges(mCrime.getId());
            }
        });

        // schwere untat setzen
        mSeriousCheckBox.setChecked(mCrime.isPoliceRequired());
        mSeriousCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                mCrime.setPoliceRequired(isChecked);
            }
        });

        // Fall abschicken
        // hier braucht der implicit intent nur eine action
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); // Typ kann nicht im konstruktor spezifiziert werden, deshalb wird er separat gesetzt
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport()); // bericht wird uebergeben
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject)); // titel des berichts -- standardtitel hier
                intent=Intent.createChooser(intent,getString(R.string.send_report)); // zeige immer eine liste der apps an, die die aufgabe uebernehmen kann. zeige zusaetzlich einen titel
                ShareCompat.IntentBuilder intent2=(ShareCompat.IntentBuilder.from(getActivity()));
                intent2.setSubject(getString(R.string.crime_report_subject));
                intent2.setText(getString(R.string.send_report));
                startActivity(intent);
            }
        });

        // durchsuche kontakte -- verwende dazu obigen pickContact-Intent
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUEST_CONTACT); // resultat wird ein pointer (genauer gesagt ein contactUri-Objekt) auf den geklickten namen sein. den werden wir in onActivityResult auslesen
            }
        });

        // rufe verdaechtigen an
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mCrime.getSuspectPhoneNumber()));
                startActivity(intent);
            }
        });

        if(mCrime.getSuspect()!=null){
            mSuspectButton.setText(mCrime.getSuspect());
        }


        // checke, ob es eine app gibt, die bilder aufnehmen kann
        boolean canTakePhoto=mPhotoFile != null && captureImage.resolveActivity(packageManager)!= null;
        mPhotoButton.setEnabled(canTakePhoto);
        //
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri= FileProvider.getUriForFile(getActivity(),"com.example.criminalintent.fileprovider",mPhotoFile); // uebersetzt pfad in uri fuer kamera-app
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri); // speichere das aufgenommene bild in oben spezifizierten pfad
                // nun erlauben wir allen apps, die bilder aufnehmen koennen, diese auch bei uns zu speichern
                List <ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY); // gibt alle apps, die bilder aufnehmen koennen
                for(ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION); // erlaubt diesen apps zu speichern
                }
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });
        updatePhotoView(); // lade foto


        // lade reingezoomte version des fotos
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getFragmentManager();
                ZoomedPhotoFragment dialog=ZoomedPhotoFragment.newInstance(mPhotoFile);
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_ZOOMED_PHOTO); // TargetFragment ist das Fragment, das Daten von einem anderen Fragment bekommen soll, wenn es zerstoert wird. wir haben somit eine Verbindung zwischen CrimeFragment un DatePickerFragment
                dialog.show(manager,DIALOG_PHOTO);
            }
        });


        // checke, ob wir eine app haben, die kontakte auslesen kann (wenn es keine gibt, und der user klicken koennte, wuerde die app crashen)
        if(packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)==null){ // MATCH_DEFAULT_ONLY restringiert die suche auf activities mit CATEGORY_DEFAULT-flag (d.h., es muss activities geben,
            // die die aufgabe tatsaechlich freiwillig uebernehmen wollen; wenn die suche erfolgreich ist, bekommen wir eine instanz von ResolveInfo, die uns alles ueber die gefundene activity sagt
            mSuspectButton.setEnabled(false);
        }


        return v;
    }

/*    private void collectChanges(UUID id){
        maybeChangedIds[counterChangedIds]=id;
        counterChangedIds++;
        sendResult(Activity.RESULT_OK,maybeChangedIds);
    }


    private void sendResult(int resultCode, UUID[] id){ // gibt letzte angesehene UUID zurueck
        Intent intent=new Intent();
        // UUID[] maybeChangedIds=new UUID[10];
        // maybeChangedIds[0]=UUID.randomUUID();
        //maybeChangedIds.add(id);
        // intent.putExtra(EXTRA_UUID,maybeChangedIds);
        intent.putExtra(EXTRA_UUID,id);
        getActivity().setResult(resultCode,intent);
    }*/

    // ueberschreibt die onActivityResult-Methode, um mit der Antwort von DatePickerFragment umgehen zu koennen
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_DATE){
            Date date=(Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
            // sendResult(Activity.RESULT_OK,mCrime.getId());
            // collectChanges(mCrime.getId());
        }

        if(requestCode==REQUEST_TIME){
            Date time=(Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTime(time);
            updateTime();
            // sendResult(Activity.RESULT_OK,mCrime.getId());
            // collectChanges(mCrime.getId());
        }

        else if(requestCode==REQUEST_CONTACT && data!=null){
            Uri contactUri=data.getData(); // zeigt auf den geklickten kontakt
            String suspectId=null;
            String suspectName=null;
            String suspectNumber=null;
            // welche Teile des kontakts wollen wir auslesen?
            // String[] queryFields=new String[]{ ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
            // fuehre anfrage aus; daten von einem ContactProvider (wie kontakten) bekommen wir mittels eines wrappers, dem ContactResolver
            // Instanzen von ContactProvidern wrappen Datenbanken
            // Cursor c=getActivity().getContentResolver().query(contactUri,queryFields,ContactsContract.Contacts.HAS_PHONE_NUMBER +"!=0",null,null); // zeigt auf die zeile, die wir von data bekommen haben
            Cursor c=getActivity().getContentResolver().query(contactUri,null,null,null,null); // zeigt auf die zeile, die wir von data bekommen haben

            try{
                // pruefen, ob wir was zurueckbekommen haben
                if(c.getCount()==0){
                    return;
                }
                // lese die erste spalte aus -- dies ist der name des kontakts
                c.moveToFirst();
                suspectName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                suspectId=c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                boolean hasPhone=Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0; // checke, ob kontakt eine telefonnummer hat
                if(hasPhone){
                    Cursor cp=getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                                        null,
                                                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                                                                        new String[] {suspectId}, null);
                    try {
                        if (cp.getCount() == 0) {
                            return;
                        }
                        cp.moveToFirst();
                        suspectNumber=cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    finally {
                        cp.close();
                    }
                }
                else{
                    mCallButton.setEnabled(false);
                }
                mCrime.setSuspect(suspectName);
                mSuspectButton.setText(suspectName);
                mCrime.setSuspectPhoneNumber(suspectNumber);
            }
            finally{
                c.close(); // aufraeumen nicht vergessen!
            }


        }

        else if(requestCode==REQUEST_PHOTO){
            Uri uri= FileProvider.getUriForFile(getActivity(),"com.example.criminalintent.fileprovider",mPhotoFile);
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION); // nun duerfen die apps keine fotos mehr speichern
            updatePhotoView();
        }

        else if(requestCode==REQUEST_ZOOMED_PHOTO){
            removePhoto();
            updatePhotoView();
        }
    }


    // Datum anzeigen lassen
    private void updateDate(){
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        // mDateButton.setText("Date: " + dateFormat.format("dd.MM.yyyy",mCrime.getDate()));
        mDateButton.setText("Date: " + mCrime.getDate());
        // mDateButton.setText(mCrime.getDate().toString());
    }

    // Zeit anzeigen lassen
    private void updateTime(){
        // android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        // mTimeButton.setText("Date: " + dateFormat.format("HH:mm",mCrime.getDate()));
        mTimeButton.setText("Time: " + mCrime.getTime());
        // mTimeButton.setText(mCrime.getDate().toString());
    }


    // Stellt den String fuer den CrimeReport zusammen
    private String getCrimeReport(){
        String solvedString=null;
        if(mCrime.isSolved()){
            solvedString=getString(R.string.crime_report_solved);
        }
        else{
            solvedString=getString(R.string.crime_report_unsolved);
        }

        String seriousString=null;
        if(mCrime.isPoliceRequired()){
            seriousString=getString(R.string.crime_report_serious);
        }
        else{
            seriousString=getString(R.string.crime_report_not_serious);
        }

        String dateFormat="EEE, MMM dd";
        String dateString=android.text.format.DateFormat.format(dateFormat,mCrime.getLongDate()).toString();

        String suspect=mCrime.getSuspect();
        if(suspect==null){
            suspect=getString(R.string.crime_report_no_suspect);
        }
        else{
            suspect=getString(R.string.crime_report_suspect);
        }

        String report=getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,seriousString,suspect);

        return report;
    }


    // laedt das skalierte bitmap
    private void updatePhotoView(){
        if (mPhotoFile==null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }
        else{
            Bitmap bitmap=PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void removePhoto(){
        mPhotoFile.delete();
    }
}
