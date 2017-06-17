package org.pk.potholedetection.utilities;

import org.pk.potholedetection.MainApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;



/**
 * This class is used to display custom progress dialog.
 * 
 */
public class CustomProgressDialog {
    private static ProgressDialog _progressDialog;

    /**
     * Method to show progress dialog
     * 
     * @param activity
     *            the activity context
     * @param messageResId
     *            the string resource id
     */
    public static void show( final Activity activity, final int messageResId ) {
        try {
            activity.runOnUiThread( new Runnable( ) {
                @Override
                public void run() {
                    try {
                        _progressDialog = new ProgressDialog( activity );
                        _progressDialog.setMessage( activity.getString( messageResId ) );
                        _progressDialog.setIndeterminate( false );
                        _progressDialog.setCancelable( true );
                        _progressDialog.setCanceledOnTouchOutside( false );
                        _progressDialog.show( );
                    } catch ( Exception e ) {
                        Log.e( MainApplication.LOG_TAG, "CustomProgressDialog: show: run: Exception->" + e.getMessage( ) );
                        e.printStackTrace( );
                    }
                }
            } );
        } catch ( Exception e ) {
            Log.e( MainApplication.LOG_TAG, "CustomProgressDialog: show: Exception->" + e.getMessage( ) );
            e.printStackTrace( );
        }
    }

    /**
     * Method to show progress dialog
     * 
     * @param activity
     *            the activity context
     * @param message
     *            the string message
     */
    public static void show( final Activity activity, final String message ) {
        try {
            activity.runOnUiThread( new Runnable( ) {
                @Override
                public void run() {
                    try {
                        _progressDialog = new ProgressDialog( activity );
                        _progressDialog.setMessage( message );
                        _progressDialog.setIndeterminate( false );
                        _progressDialog.setCancelable( true );
                        _progressDialog.setCanceledOnTouchOutside( false );
                        _progressDialog.show( );
                    } catch ( Exception e ) {
                        Log.e( MainApplication.LOG_TAG, "CustomProgressDialog: show: run: Exception->" + e.getMessage( ) );
                        e.printStackTrace( );
                    }
                }
            } );
        } catch ( Exception e ) {
            Log.e( MainApplication.LOG_TAG, "CustomProgressDialog: show: Exception->" + e.getMessage( ) );
            e.printStackTrace( );
        }
    }

    /**
     * Method to dismiss previously showing progress dialog
     * 
     * @param activity
     *            the activity context
     */
    public static void dismiss( Activity activity ) {
        try {
            activity.runOnUiThread( new Runnable( ) {
                @Override
                public void run() {
                    try {
                        if ( _progressDialog != null && _progressDialog.isShowing( ) ) {
                            _progressDialog.dismiss( );
                        }
                    } catch ( Exception e ) {
                        Log.e( MainApplication.LOG_TAG, "CustomProgressDialog: dismiss: run: Exception->" + e.getMessage( ) );
                        e.printStackTrace( );
                    }
                }
            } );
        } catch ( Exception e ) {
            Log.e( MainApplication.LOG_TAG, "CustomProgressDialog: dismiss: Exception->" + e.getMessage( ) );
            e.printStackTrace( );
        }
    }
}
