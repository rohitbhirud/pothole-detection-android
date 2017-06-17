package org.pk.potholedetection.webservices;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import org.pk.potholedetection.datamodels.CommunicationError;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;
import org.pk.potholedetection.utilities.ScalingUtilities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

/**
 * @author SHREE
 * 
 */
public abstract class FileUploader {

	protected abstract HashMap<String, String> getRequestParams();

	protected abstract String getUrl();

	protected abstract String getLocalFilePath();

	protected abstract void notifyProgress(int percentage);

	protected abstract void parseResponse(String response);

	protected abstract void notifyError(int errorCode);

	public void startUpload() {
		FileUploaderTask uploadFile = new FileUploaderTask();
		uploadFile.execute();
	}

	class FileUploaderTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			long bytesRead, bytesAvailable, totalBytes;
			int bufferSize;
			byte[] buffer;
			int maxBufferSize = 10 * 1024;

			String localFilePath = getLocalFilePath();

			String reducedFilePath = this.decodeFile( localFilePath, 300, 500 );

			LogUtils.debug(LogCategory.SERVICE, "FileUploader: localFilePath: " + localFilePath);
			LogUtils.debug(LogCategory.SERVICE, "FileUploader: reducedFilePath: " + reducedFilePath);

			File sourceFile = new File(reducedFilePath);

			if (sourceFile == null || !sourceFile.exists() || sourceFile.length() == 0) {
				LogUtils.error(LogCategory.SERVICE, "FileUploader: doInBackground: Source File not exist->" + localFilePath);
				return -1;
			} else {
				try {
					LogUtils.debug(LogCategory.SERVICE, "FileUploader: doInBackground: uploadig file");

					FileInputStream fileInputStream = new FileInputStream(sourceFile);
					URL url = new URL(getUrl());

					// Open a HTTP connection to the URL
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoInput(true); // Allow Inputs
					conn.setDoOutput(true); // Allow Outputs
					conn.setUseCaches(false); // Don't use a Cached Copy
					conn.setRequestMethod("POST");
					// conn.setChunkedStreamingMode(0);
					conn.setRequestProperty("ENCTYPE", "multipart/form-data");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
					conn.setRequestProperty("userfile", sourceFile.getName());
					// HashMap<String, String> requestParams =
					// getRequestParams();
					// if (requestParams != null && !requestParams.isEmpty()) {
					// // send multipart form data necessary after file data...
					// Set<String> keys = requestParams.keySet();
					// Object[] strKeys = keys.toArray();
					// for (Object key : strKeys) {
					// String value = requestParams.get((String) key);
					// conn.addRequestProperty((String) key, value);
					// }
					// }
					dos = new DataOutputStream(conn.getOutputStream());

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"userfile\";filename=\"" + sourceFile.getName() + "\"" + lineEnd);
					dos.writeBytes(lineEnd);

					totalBytes = bytesAvailable = fileInputStream.available();
					do {
						// LogUtils.debug(LogCategory.CAT_SERVICE,
						// "FileUploader: doInBackground: bytesAvailable->" +
						// bytesAvailable);
						bufferSize = (int) Math.min(bytesAvailable, maxBufferSize);
						buffer = new byte[bufferSize];
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
						dos.write(buffer, 0, bufferSize);
						bytesAvailable -= bytesRead;

						long uploadPercentage = (long) ((totalBytes - bytesAvailable) / (double) totalBytes * 100);
						LogUtils.debug(LogCategory.SERVICE, "FileUploader: doInBackground: notifying update");
						if (uploadPercentage == 100) {
							notifyProgress(99);
						} else {
							notifyProgress((int) uploadPercentage);
						}
						// }
					} while (bytesAvailable > 0);

					HashMap<String, String> requestParams = getRequestParams();
					if (requestParams != null && !requestParams.isEmpty()) {
						// send multipart form data necessary after file data...
						Set<String> keys = requestParams.keySet();
						Object[] strKeys = keys.toArray();
						for (Object key : strKeys) {
							String value = requestParams.get((String) key);
							dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
							dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
							dos.writeBytes(lineEnd);
							dos.writeBytes(value);
							dos.writeBytes(lineEnd);
						}
					}

					dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"filesize\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes("" + sourceFile.length());
					dos.writeBytes(lineEnd);

					/**
					 * Append last form-data header
					 */
					dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);

					// Responses from the server (code and message)
					int serverResponseCode = conn.getResponseCode();
					String serverResponseMessage = conn.getResponseMessage();
					byte[] buff = new byte[1024];
					int read = conn.getInputStream().read(buff);
					String responseMessage = new String(buff, 0, read);
					LogUtils.debug(LogCategory.SERVICE, "FileUpoader: doInBackground: HTTP Response->" + serverResponseMessage + " responseMessage->" + responseMessage);

					if (serverResponseCode == 200) {

						LogUtils.debug(LogCategory.SERVICE, "FileUpoader: doInBackground: File uploaded successfully");
					}

					// close the connection and streams //
					fileInputStream.close();
					dos.flush();
					dos.close();
					conn.disconnect();

					notifyProgress(100);
					parseResponse(responseMessage);
				} catch (Exception ex) {
					LogUtils.error(LogCategory.SERVICE, "FileUpoader: doInBackground: Exception->" + ex.getMessage());
					ex.printStackTrace();
					notifyError(CommunicationError.ERROR_COMMUNICATION);
					return -1;
				} // End else block
			}
			return 0;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			// EventNotifier notifier =
			// NotifierFactory.getInstance().getNotifier(NotifierFactory.UPLOAD_IMAGE_NOTIFIER);
			// notifier.eventNotify(EventTypes.UPDATE_IMAGE_FAILED, null);
		}

		private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
			String strMyImagePath = null;
			Bitmap scaledBitmap = null;

			try {
				// Part 1: Decode image
				Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

				if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
					// Part 2: Scale image
					scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
				} else {
					unscaledBitmap.recycle();
					return path;
				}

				// Store to tmp file

				String extr = Environment.getExternalStorageDirectory().toString();
				File mFolder = new File(extr + "/TMMFOLDER");
				if (!mFolder.exists()) {
					mFolder.mkdir();
				}

				String s = "tmp.png";

				File f = new File(mFolder.getAbsolutePath(), s);

				strMyImagePath = f.getAbsolutePath();
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(f);
					scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (Exception e) {

					e.printStackTrace();
				}

				scaledBitmap.recycle();
			} catch (Throwable e) {
			}

			if (strMyImagePath == null) {
				return path;
			}
			return strMyImagePath;

		}
	}
}
