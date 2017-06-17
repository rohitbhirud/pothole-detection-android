package org.pk.potholedetection.logger;

import org.pk.potholedetection.MainApplication;

import android.util.Log;

public class LogUtils {
	private static boolean _writeToFile = true;
	private static final String LOG_TAG = MainApplication.LOG_TAG;
	private static Logger _logger;

	public static void info(String category, String message) {
		Log.i(LOG_TAG, message);
		if (_writeToFile) {
			if (_logger == null) {
				_logger = new Logger();
			}
			// _logger.addEntry(category, message);
		}
	}

	public static void debug(String category, String message) {
		Log.d(LOG_TAG, message);
		if (_writeToFile) {
			if (_logger == null) {
				_logger = new Logger();
			}
			// _logger.addEntry(category, message);
		}
	}

	public static void error(String category, String message) {
		Log.e(LOG_TAG, message);
		if (_writeToFile) {
			if (_logger == null) {
				_logger = new Logger();
			}
			// _logger.addEntry(category, message);
		}
	}

}
