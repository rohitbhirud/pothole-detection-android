package org.pk.potholedetection.eventnotification;

import java.util.ArrayList;

public class NotifierFactory {
	private static NotifierFactory _instance;
	private ArrayList<EventNotifier> _notifiers;

	public static int LOGIN_EVENT_NOTIFIER = 1;
	public static int UPLOAD_FILE_NOTIFIER = 2;
	public static int COMPLAINT_NOTIFIER = 3;
	public static int REGISTRATION_EVENT_NOTIFIER = 4;
	public static int ACCELEROMETER_EVENT_NOTIFIER = 5;

	private NotifierFactory() {
		_notifiers = new ArrayList<EventNotifier>();
	}

	public static NotifierFactory getInstance() {
		if (_instance == null) {
			_instance = new NotifierFactory();
		}
		return _instance;
	}

	/**
	 * Method to get the {@link EventNotifier} of input notifierType
	 * 
	 * @param notifierType
	 *            the {@link NotifierFactory}.notifierType
	 * @return the {@link EventNotifier} for input notifierType
	 */
	public EventNotifier getNotifier(int notifierType) {
		EventNotifier notifier = null;
		int index = _checkAlreadyPresent(notifierType);
		if (index != -1) {
			/**
			 * If notifier is already present in list.
			 */
			notifier = _notifiers.get(index);
		} else {
			/**
			 * If notifier is not already present in list, add it to list and
			 * return
			 */
			notifier = new EventNotifier(notifierType);
			_notifiers.add(notifier);
		}
		return notifier;
	}

	/**
	 * Method to check if {@link EventNotifier} with input notifierType is
	 * already present in list.
	 * 
	 * @param notifierType
	 *            the notifierType
	 * @return position >= 0 if {@link EventNotifier} of input notifierType is
	 *         found in list, -1 otherwise.
	 */
	private int _checkAlreadyPresent(int notifierType) {
		int index = -1;
		int size = _notifiers.size();
		for (int i = 0; i < size; i++) {
			if (notifierType == _notifiers.get(i).notifierType) {
				index = i;
				break;
			}
		}
		return index;
	}
}
