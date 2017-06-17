package org.pk.potholedetection.utilities;

import org.pk.potholedetection.MainApplication;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;



/**
 * Custom toast class. Can be used if we want to have a customized toast througtout the application.
 * 
 */
public class CustomToast {
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;

    /**
     * Method to show the toast message
     * 
     * @param activity
     *            the activity context
     * @param messageResId
     *            the string resource id
     * @param length
     *            the toast length
     */
    public static void show( final Activity activity, final int messageResId, final int length ) {
        try {
            activity.runOnUiThread( new Runnable( ) {
                @Override
                public void run() {
                    try {
                        Toast.makeText( activity, messageResId, length ).show( );
                    } catch ( Exception e ) {
                        Log.e( MainApplication.LOG_TAG, "CustomToast: show: run: Exception->" + e.getMessage( ) );
                        e.printStackTrace( );
                    }
                }
            } );
        } catch ( Exception e ) {
            Log.e( MainApplication.LOG_TAG, "CustomToast: show: Exception->" + e.getMessage( ) );
            e.printStackTrace( );
        }
    }

    /**
     * Method to show the toast message
     * 
     * @param activity
     *            the activity context
     * @param message
     *            the string
     * @param length
     *            the toast length
     */
    public static void show( final Activity activity, final String message, final int length ) {
        try {
            activity.runOnUiThread( new Runnable( ) {
                @Override
                public void run() {
                    try {
                        Toast.makeText( activity, message, length ).show( );
                    } catch ( Exception e ) {
                        Log.e( MainApplication.LOG_TAG, "CustomToast: show: run: Exception->" + e.getMessage( ) );
                        e.printStackTrace( );
                    }
                }
            } );
        } catch ( Exception e ) {
            Log.e( MainApplication.LOG_TAG, "CustomToast: show: Exception->" + e.getMessage( ) );
            e.printStackTrace( );
        }
    }
}
