package org.pk.potholedetection.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import org.pk.potholedetection.MainApplication;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.NotifierFactory;


public class AccelerationMonitor implements SensorEventListener {
	private static AccelerationMonitor _instance;
	private SensorManager _sensorManager;
	private Sensor _sensor;

	private final double ACCELERATION_THRESHOLD = 25.0000;

	/**
	 * Acceleration toward Z-Axis
	 */
	private float _prevAcclZ;

	private boolean _initialized;

	private AccelerationMonitor() {

	}

	public static AccelerationMonitor getInstance() {
		if (_instance == null) {
			_instance = new AccelerationMonitor();
		}
		return _instance;
	}

	public void start() {
		try {
			_sensorManager = (SensorManager) MainApplication.appContext.getSystemService(Context.SENSOR_SERVICE);
			_sensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			if (_sensor != null) {
				_sensorManager.registerListener(this, _sensor, SensorManager.SENSOR_DELAY_NORMAL);
				Log.d(MainApplication.LOG_TAG, "AccelerationMonitor: start: Acceleration sensor initialized");

			} else {
				Log.d(MainApplication.LOG_TAG, "Acceleration Sensor not found");
				Toast.makeText(MainApplication.appContext, "Acceleration Sensor not found", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Log.d(MainApplication.LOG_TAG, "AccelerationMonitor: start: Exception->" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float[] values = event.values;
			// Movement
			// float currentAcclX = values[0];
			// float currentAcclY = values[1];
			float currentAcclZ = values[2];

			if (!_initialized) {
				_prevAcclZ = currentAcclZ;
				_initialized = true;
				return;
			}
			// _handleAcceleratorChangedEvent(currentAcclX, currentAcclY,
			// currentAcclZ);
			float deltaZ = Math.abs(_prevAcclZ - currentAcclZ);
			if (deltaZ > ACCELERATION_THRESHOLD) {
				Log.d(MainApplication.LOG_TAG, "AccelerationMonitor: onSensorChanged: deltaZ->" + deltaZ);
				EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.ACCELEROMETER_EVENT_NOTIFIER);
				notifier.eventNotify(EventTypes.ACCELEROMETR_VALUES_CHANGED, null);
			}
		}
	}

	public void stop() {
		try {
			if (_sensor != null && _sensorManager != null) {
				_sensorManager.unregisterListener(_instance);
				_sensorManager = null;
				_sensor = null;
			}
		} catch (Exception e) {
			Log.e(MainApplication.LOG_TAG, "AccelerationMonitor: stop: Exception->" + e.getMessage());
			e.printStackTrace();
		}
	}
}
