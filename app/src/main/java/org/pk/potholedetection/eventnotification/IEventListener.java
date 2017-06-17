package org.pk.potholedetection.eventnotification;

public interface IEventListener {
	public int eventNotify(int eventType, Object eventObject);

	public void registerListener();
	
	public void unregisterListener();
}
