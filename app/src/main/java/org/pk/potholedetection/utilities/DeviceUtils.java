package org.pk.potholedetection.utilities;

import org.pk.potholedetection.MainApplication;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;



/**
 * Utility class to interact with device interface APIs
 * 
 */
public class DeviceUtils {
	/**
	 * Method to get the database directory path
	 * 
	 * @param dbName
	 *            the database name
	 * @return the database path for input database name
	 */
	public static String getDatabaseDirectory(String dbName) {
		return MainApplication.appContext.getDatabasePath(dbName).getAbsolutePath();
	}

	/**
	 * Method to check if SD card is present
	 * 
	 * @return true if SD card is present, false otherwise
	 */
	public static boolean isSdCardPresent() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_SHARED.equals(state)) {
			return true;
		}
		return false;
	}

	public static void hideSoftKeyboard(View focusedView) {
		InputMethodManager imm = (InputMethodManager) MainApplication.appContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
	}
}
