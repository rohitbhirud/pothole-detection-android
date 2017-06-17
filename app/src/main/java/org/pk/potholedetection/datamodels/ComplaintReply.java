package org.pk.potholedetection.datamodels;

public class ComplaintReply {
	private String _complaintType;
	private String _complaintTitle;
	private String _complaintDescription;
	private String _expertComment;
	private String status;

	public ComplaintReply(String complaintType, String title, String description, String comment, String status) {
		this._complaintType = complaintType;
		this._complaintTitle = title;
		this._complaintDescription = description;
		this._expertComment = comment;
		this.status = status;
	}

	public String getComplaintType() {
		return _complaintType;
	}

	public void setComplaintType(String complaintType) {
		this._complaintType = complaintType;
	}

	public String getTitle() {
		return _complaintTitle;
	}

	public void setTitle(String title) {
		this._complaintTitle = title;
	}

	public String getDescription() {
		return _complaintDescription;
	}

	public void setDescription(String description) {
		this._complaintDescription = description;
	}

	public String getComment() {
		return _expertComment;
	}

	public void setComment(String comm) {
		this._expertComment = comm;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
