package org.pk.potholedetection.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.pk.potholedetection.utilities.DateTimeUtils;
import org.pk.potholedetection.utilities.DeviceUtils;

import android.os.Environment;



public class Logger {
	private static String LOG_TIMESTAMP_FORMAT = "yyyy-MM-dd hh:mm:ss";

	private static final String BASE_FOLDER_ON_SD_CARD = "PotholeDetectionLogs";
	private static final String FILE_NAME_PREFIX = "Log_";
	private static final String FILE_EXTENSION = ".txt";
	private static String FILE_NAME_FORMAT = "yyyy-MM-dd";

	public synchronized void addEntry(String category, String message) {
		/**
		 * Check if SD card is present if not, simply return.
		 */
		if (!DeviceUtils.isSdCardPresent()) {
			return;
		}

		String timeStamp = DateTimeUtils.getFormattedTime(LOG_TIMESTAMP_FORMAT);
		String logEntry = "[" + timeStamp + "] [" + category + "] " + message;
		_writeToFile(logEntry);
	}

	/**
	 * Method to write log entry to file.
	 * 
	 * @param logEntry
	 *            the log entry to be written to file.
	 */
	private void _writeToFile(String logEntry) {
		if (!_isFileExists()) {
			boolean created = _createNewFile();
			if (!created) {
				return;
			}
		}

		String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		String fileName = FILE_NAME_PREFIX + DateTimeUtils.getFormattedTime(FILE_NAME_FORMAT) + FILE_EXTENSION;
		String filePath = sdCardPath + File.separator + BASE_FOLDER_ON_SD_CARD + File.separator + fileName;
		File file = new File(filePath);
		FileWriter fos = null;
		try {
			fos = new FileWriter(file, true);
			fos.append(logEntry + "\n");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to check if the log file exists.
	 * 
	 * @return true if the log file exists, false otherwise.
	 */
	private boolean _isFileExists() {
		String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		String fileName = FILE_NAME_PREFIX + DateTimeUtils.getFormattedTime(FILE_NAME_FORMAT) + FILE_EXTENSION;
		String filePath = sdCardPath + File.separator + BASE_FOLDER_ON_SD_CARD + File.separator + fileName;
		File file = new File(filePath);
		if (file != null && file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * Method to create new log file
	 * 
	 * @return true upon successful creation, false otherwise.
	 */
	private boolean _createNewFile() {
		String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		String fileName = FILE_NAME_PREFIX + DateTimeUtils.getFormattedTime(FILE_NAME_FORMAT) + FILE_EXTENSION;
		String folderPath = sdCardPath + File.separator + BASE_FOLDER_ON_SD_CARD + File.separator;
		String filePath = folderPath + fileName;

		File folder = new File(folderPath);
		if (folder != null || !folder.exists()) {
			folder.mkdirs();
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			try {
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
