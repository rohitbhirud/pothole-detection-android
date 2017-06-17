package org.pk.potholedetection.datamodels;

public class Complaint {
	private String complaintType;
	private String title;
	private String description;
	private double latitude;
	private double longitude;

	public Complaint() {

	}

	public Complaint(String complaintType, String title, String description, double latitude, double longitude) {
		this.complaintType = complaintType;
		this.title = title;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getComplaintType() {
		return complaintType;
	}

	public void setComplaintType(String complaintType) {
		this.complaintType = complaintType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
