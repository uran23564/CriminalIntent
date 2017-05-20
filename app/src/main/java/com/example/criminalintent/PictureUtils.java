package com.example.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by merz_konstantin on 5/20/17.
 */

public class PictureUtils {
    // skaliert das erzeugte bitmap aus der originaldatei ausgehend von der groesse des PhotoViews
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
        // Dimensionen des gespeicherten Bilds auslesen
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        // Algorythmus zum Runterskalieren
        int inSampleSize=1; // wie gross ist jedes "sample" fuer jeden pixel (samplesize=s hat einen horizontalen pixel fuer s horizontale pixel der originaldatei
        if(srcHeight>destHeight || srcWidth>destWidth){
            float heightScale=srcHeight/destHeight;
            float widthScale=srcWidth/destWidth;

            inSampleSize=Math.round(heightScale>widthScale ? heightScale:widthScale);
        }

        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        // einlesen des bilds und erstellen des finalen bitmap-objekts
        return BitmapFactory.decodeFile(path,options);
    }

    // erzeugt skaliertes bitmap fuer eine bestimmte groesse der activity
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size); // checked, wie gross der screen ist (brutale abschaetzung)
        return getScaledBitmap(path, size.x,size.y);
    }
}
