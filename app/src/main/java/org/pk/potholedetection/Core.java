package org.pk.potholedetection;

import android.location.Location;
import android.util.Log;

import org.pk.potholedetection.datamodels.Complaint;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventStates;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.IEventListener;
import org.pk.potholedetection.eventnotification.ListenerPriority;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.locator.LocationMonitor;
import org.pk.potholedetection.sensor.AccelerationMonitor;
import org.pk.potholedetection.webservices.JurkComplaint;

/**
 * This class includes the business logic of application.
 */
public class Core implements IEventListener {
	private static Core _instance;

	private long _lastComplaintSentTimestamp;

	private final long POTHOLE_DETECTION_TIMEOUT = 2 * 1000;

	/**
	 * Private constructor.
	 */
	private Core() {
	}

	/**
	 * Method to get the initialized instance of {@link Core}.
	 * 
	 * @return the initialized instance of {@link Core}.
	 */
	public static Core getInstance() {
		if (_instance == null) {
			_instance = new Core();
		}
		return _instance;
	}

	public void init() {
		registerListener();
		LocationMonitor.getInstance().start();
		AccelerationMonitor.getInstance().start();
		_lastComplaintSentTimestamp = System.currentTimeMillis();
	}

	public void stop() {
		LocationMonitor.getInstance().stop();
		AccelerationMonitor.getInstance().stop();
	}

	@Override
	public void registerListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.ACCELEROMETER_EVENT_NOTIFIER);
		notifier.registerListener(Core.this, ListenerPriority.HIGH);
	}

	@Override
	public void unregisterListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.ACCELEROMETER_EVENT_NOTIFIER);
		notifier.unregisterListener(Core.this);
	}

	@Override
	public int eventNotify(int eventType, Object eventObject) {
		switch (eventType) {
		case EventTypes.ACCELEROMETR_VALUES_CHANGED:
			long currentTimestamp = System.currentTimeMillis();
			if (currentTimestamp - _lastComplaintSentTimestamp > POTHOLE_DETECTION_TIMEOUT) {
				Log.d(MainApplication.LOG_TAG, "Core: eventNotify: Sending complaint");
				_lastComplaintSentTimestamp = currentTimestamp;
				Location location = LocationMonitor.getInstance().getCurrentLocation();
				if (location != null) {
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					Complaint complaint = new Complaint("Pothole", "Pothole detected by sensor", "This is automatically detected pothole", latitude, longitude);
					JurkComplaint makeComplaint = new JurkComplaint(complaint);
					makeComplaint.send();
				}
			}
			break;
		}
		return EventStates.PROCESSED;
	}
}
