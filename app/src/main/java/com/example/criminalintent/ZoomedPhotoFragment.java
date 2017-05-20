package com.example.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by merz_konstantin on 5/20/17.
 */

public class ZoomedPhotoFragment extends DialogFragment{
    private static final String ARG_PHOTO_FILE="photoFile";

    private File mFile;
    private ImageView mPhotoView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_zoomed_photo,null);

        mPhotoView=(ImageView) v.findViewById(R.id.zoomed_photo);
        mFile=(File) getArguments().getSerializable(ARG_PHOTO_FILE);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(v).setNegativeButton(R.string.delete_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK);
                // onDestroy();
            }
        });



        if(mFile==null || !mFile.exists()){
            return builder.create();
        }
        else{
            Bitmap bitmap =PictureUtils.getScaledBitmap(mFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
            return builder.create();
        }
    }


    public static ZoomedPhotoFragment newInstance(File file) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_FILE,file);
        ZoomedPhotoFragment fragment = new ZoomedPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // wird nur aufgerufen, falls foto geloescht werden soll
    private void sendResult(int resultCode){
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,null);
    }
}
