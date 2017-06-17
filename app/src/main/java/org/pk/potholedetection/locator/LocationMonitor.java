package org.pk.potholedetection.locator;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import org.pk.potholedetection.MainApplication;
import org.pk.potholedetection.logger.LogUtils;


public class LocationMonitor {
	private static LocationMonitor _instance;
	private LocationManager _locationManager = null;
	private LocationListener _locationListener = null;
	private Location _currentLocation;

	/**
	 * Private Constructor
	 */
	private LocationMonitor() {

	}

	/**
	 * Method to get the initialized instance of {@link LocationMonitor}
	 * 
	 * @return the initialized instance of {@link LocationMonitor}
	 */
	public static LocationMonitor getInstance() {
		if (_instance == null) {
			_instance = new LocationMonitor();
			_instance._locationManager = (LocationManager) MainApplication.appContext.getSystemService(Context.LOCATION_SERVICE);
		}
		return _instance;
	}

	public void start() {
		stop();
		new LooperThread().start();
	}

	class LooperThread extends Thread {
		public Handler mHandler;

		public void run() {
			Looper.prepare();
			HandlerThread myThread = new HandlerThread("Worker Thread");
			myThread.start();
			Looper mLooper = myThread.getLooper();
			new MyHandler(mLooper).sendEmptyMessage(0);
			Looper.loop();
		}
	}

	static class MyHandler extends Handler {
		public MyHandler(Looper myLooper) {
			super(myLooper);
		}

		public void handleMessage(Message msg) {
			_instance._start();
		}
	}

	private void _start() {
		try {
			_locationListener = new LocationListener();
			_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, _locationListener);
			_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, _locationListener);
			_locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static long _lastLocationUpdatedTimestamp = 0;
	private static final long LOCATION_UPDATE_TIMEOUT = 1 * 1000;

	private class LocationListener implements android.location.LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (_lastLocationUpdatedTimestamp == 0) {
				_lastLocationUpdatedTimestamp = System.currentTimeMillis();
			} else if (System.currentTimeMillis() - _lastLocationUpdatedTimestamp < LOCATION_UPDATE_TIMEOUT) {
				/**
				 * Ignore subsequent location updates if received before the
				 * specified timeout
				 */
				return;
			}
			_currentLocation = location;
			double accuracy = location.getAccuracy();
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			double altitude = location.getAltitude();
			double speed = location.getSpeed();
			String provider = location.getProvider();

			LogUtils.debug(MainApplication.LOG_TAG, "LocationMonitor: onLocationChanged: latitude->" + latitude + " longitude->" + longitude + " altitude->" + altitude + " speed->" + speed
				+ " provider->" + provider + " accuracy->" + accuracy);
		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	}

	/**
	 * Method to check if GPS/Network provider is enabled.
	 * 
	 * @return true if GPS/Network provider is enabled, false otherwise
	 */
	public boolean isGpsEnabled() {
		if (_locationManager != null) {
			return _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}
		return false;
	}

	/**
	 * Method to get the current location.
	 * 
	 * @return the current {@link Location}
	 */
	public Location getCurrentLocation() {
		return _currentLocation;
	}

	public void stop() {
		try {
			if (_locationManager != null && _locationListener != null) {
				_locationManager.removeUpdates(_locationListener);
				_locationListener = null;
				_locationManager = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
