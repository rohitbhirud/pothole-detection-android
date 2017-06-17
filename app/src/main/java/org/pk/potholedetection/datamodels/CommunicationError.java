package org.pk.potholedetection.datamodels;

public class CommunicationError {

	public static final int ERROR_COMMUNICATION = -1001;
	public static final int ERROR_NO_NETWORK = -1002;
	public static final int ERROR_INVALID_RESPONSE = -1003;
	public static final int ERROR_OTHER = -1004;

	private int errorCode;
	private String description;
	private Object extra;

	/**
	 * Parameterized Constructor
	 * 
	 * @param errorCode
	 *            the error code
	 * @param description
	 *            the error description
	 */
	public CommunicationError(int errorCode, String description) {
		this.errorCode = errorCode;
		this.description = description;
	}

	/**
	 * Parameterized Constructor
	 * 
	 * @param errorCode
	 *            the error code
	 * @param description
	 *            the error description
	 * @param extra
	 *            the optional error extra
	 */
	public CommunicationError(int errorCode, String description, Object extra) {
		this.errorCode = errorCode;
		this.description = description;
		this.extra = extra;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the extra
	 */
	public Object getExtra() {
		return extra;
	}

	/**
	 * @param extra
	 *            the extra to set
	 */
	public void setExtra(String extra) {
		this.extra = extra;
	}

}
