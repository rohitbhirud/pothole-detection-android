package org.pk.potholedetection.eventnotification;

import java.util.ArrayList;

public class EventNotifier {
	public final int notifierType;
	private ArrayList<EventListener> _registerListeners;

	public EventNotifier(int notifierType) {
		this.notifierType = notifierType;
		_registerListeners = new ArrayList<EventNotifier.EventListener>();
	}

	public void registerListener(IEventListener listener, int listenerPriority) {
		EventListener eventListener = new EventListener(listener, listenerPriority);

		int index = _checkAlreadyRegistered(listener);
		if (index == -1) {
			/**
			 * If not already registered add it to the proper position depending
			 * upon the priority.
			 */
			int size = _registerListeners.size();
			if (size == 0) {
				_registerListeners.add(eventListener);
				return;
			}
			int position = 0;
			for (int i = 0; i < size; i++) {
				if (listenerPriority > _registerListeners.get(i).listenerPriority) {
					position = i;
					break;
				}
			}
			if (position == 0) {
				/**
				 * If priority of this listener object is lower than all of the
				 * listener objects in list, add it to the end of the list.
				 */
				_registerListeners.add(eventListener);
			} else {
				/**
				 * If priority of this listener object is greater than any of
				 * the listener object in list, add it to the proper position in
				 * list so that the list will always be in descending order of
				 * priority.
				 */
				_registerListeners.add(position, eventListener);
			}
		} else {
			/**
			 * If already registered, replace the instance.
			 */
			_registerListeners.set(index, eventListener);
		}
	}

	/**
	 * Method to check if the input {@link IEventListener} is already
	 * registered.
	 * 
	 * @param listener
	 *            the {@link IEventListener} object
	 * @return true if the class if input {@link IEventListener} object matches
	 *         with any of the registered listener's class, -1 otherwise
	 */
	private int _checkAlreadyRegistered(IEventListener listener) {
		int res = -1;
		int size = _registerListeners.size();
		for (int i = 0; i < size; i++) {
			if (listener.getClass().equals(_registerListeners.get(i).listenerObject.getClass())) {
				res = i;
				break;
			}
		}
		return res;
	}

	/**
	 * Method to unregister the {@link IEventListener}
	 * 
	 * @param listener
	 *            the {@link IEventListener} object to be unregistered.
	 */
	public void unregisterListener(IEventListener listener) {
		int size = _registerListeners.size();
		for (int i = 0; i < size; i++) {
			if (listener.getClass().equals(_registerListeners.get(i).listenerObject.getClass())) {
				_registerListeners.remove(i);
				break;
			}
		}
	}

	/**
	 * Method to notify event to all the registered listeners.
	 * 
	 * @param eventType
	 *            the eventType
	 * @param eventObject
	 *            the eventObject
	 */
	public void eventNotify(int eventType, Object eventObject) {
		int size = _registerListeners.size();
		for (int i = 0; i < size; i++) {
			int state = _registerListeners.get(i).listenerObject.eventNotify(eventType, eventObject);
			if (state == EventStates.STATE_CONSUMED) {
				/**
				 * If any of the register listener had consumed the event, do
				 * not pass it to remaining listeners.
				 */
				break;
			}
		}
	}

	private class EventListener {
		public final int listenerPriority;
		public final IEventListener listenerObject;

		/**
		 * Parameterized Constructor
		 * 
		 * @param listenerObj
		 *            the {@link IEventListener} object
		 * @param listenerPriority
		 *            the {@link ListenerPriority}
		 */
		public EventListener(IEventListener listenerObj, int listenerPriority) {
			this.listenerObject = listenerObj;
			this.listenerPriority = listenerPriority;
		}
	}
}
