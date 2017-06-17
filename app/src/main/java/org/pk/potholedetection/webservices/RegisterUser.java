package org.pk.potholedetection.webservices;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.pk.potholedetection.communication.CommunicationConstants;
import org.pk.potholedetection.communication.HttpCommunication;
import org.pk.potholedetection.datamodels.CommunicationError;
import org.pk.potholedetection.datamodels.RegistrationModel;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;

/**
 * This is a web service class to RegisterUser.
 * 
 */
public class RegisterUser extends BaseService {

	private final RegistrationModel _registrationModel;

	private final String _registrationUrl = CommunicationConstants.SERVER_BASE_URL + "user_register.php";

	/**
	 * Parameterized Constructor
	 * 
	 * @param model
	 *            the {@link RegistrationModel} object
	 */
	public RegisterUser(RegistrationModel model) {
		_registrationModel = model;
	}

	@Override
	protected String getUrl() {
		LogUtils.debug(LogCategory.SERVICE, "RegisterUser: getUrl: url->" + _registrationUrl);
		return _registrationUrl;
	}

	@Override
	protected ArrayList<NameValuePair> getRequestParams() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("CustomerName", _registrationModel.getName()));
		params.add(new BasicNameValuePair("Password", _registrationModel.getPassword()));
		params.add(new BasicNameValuePair("Address", _registrationModel.getAddress()));
		params.add(new BasicNameValuePair("Email", _registrationModel.getEmail()));
		params.add(new BasicNameValuePair("Mobile", _registrationModel.getMobileNumber()));
		params.add(new BasicNameValuePair("AdharNo", _registrationModel.getAdharNo()));
		return params;
	}

	@Override
	protected String getHttpMethod() {
		return HttpCommunication.METHOD_POST;
	}

	@Override
	protected void parseResponse(String response) {
		LogUtils.debug(LogCategory.SERVICE, "RegisterUser: parseResponse: response->" + response);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.REGISTRATION_EVENT_NOTIFIER);
		try {
			JSONObject jObject = new JSONObject(response);
			boolean status = jObject.getBoolean("status");
			if (status) {
				notifier.eventNotify(EventTypes.REGISTRATION_SUCCESS, null);

			} else {
				String description = jObject.optString("message");
				CommunicationError error = new CommunicationError(CommunicationError.ERROR_OTHER, description, null);
				LogUtils.debug(LogCategory.SERVICE, "RegisterUser: parseResponse: error->" + description);
				notifier.eventNotify(EventTypes.REGISTRATION_FAILED, error);
			}
		} catch (Exception e) {
			LogUtils.error(LogCategory.SERVICE, "RegisterUser: parseResponse: Exception->" + e.getMessage());
			e.printStackTrace();
			notifyError(CommunicationError.ERROR_INVALID_RESPONSE);
		}
	}

	@Override
	protected void notifyError(int errorCode) {
		CommunicationError error = new CommunicationError(CommunicationError.ERROR_OTHER, null, null);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.REGISTRATION_EVENT_NOTIFIER);
		notifier.eventNotify(EventTypes.REGISTRATION_FAILED, error);
	}
}
