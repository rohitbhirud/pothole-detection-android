package org.pk.potholedetection.datamodels;

public class RegistrationModel {
	private String name;
	private String password;
	private String address;
	private String email;
	private String mobileNumber;
	private String adharNo;

	public RegistrationModel() {

	}

	/**
	 * @param name
	 * @param password
	 * @param address
	 * @param email
	 * @param mobileNumber
	 */
	public RegistrationModel(String name, String password, String address, String email, String mobileNumber, String adharNo) {
		this.name = name;
		this.password = password;
		this.address = address;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.adharNo = adharNo;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the adharNo
	 */
	public String getAdharNo() {
		return adharNo;
	}

	/**
	 * @param adharNo
	 *            the adharNo to set
	 */
	public void setAdharNo(String adharNo) {
		this.adharNo = adharNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegistrationModel [name=" + name + ", password=" + password + ", address=" + address + ", email=" + email + ", mobileNumber=" + mobileNumber + ", adharNo=" + adharNo + "]";
	}

}
