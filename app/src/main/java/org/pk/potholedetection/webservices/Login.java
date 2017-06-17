package org.pk.potholedetection.webservices;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.pk.potholedetection.communication.CommunicationConstants;
import org.pk.potholedetection.communication.HttpCommunication;
import org.pk.potholedetection.datamodels.CommunicationError;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;
import org.pk.potholedetection.utilities.AppSettings;

/**
 * This is a web service class to login.
 * 
 */
public class Login extends BaseService {

	private final String _emailId;
	private final String _password;

	private final String _loginUrl = CommunicationConstants.SERVER_BASE_URL + "login.php";

	/**
	 * @param userName
	 * @param password
	 * @param deviceId
	 */
	public Login(String userName, String password, String deviceId) {
		_emailId = userName;
		_password = password;
	}

	@Override
	protected String getUrl() {
		LogUtils.debug(LogCategory.SERVICE, "Login: getUrl: url->" + _loginUrl);
		return _loginUrl;
	}

	@Override
	protected ArrayList<NameValuePair> getRequestParams() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("username", _emailId));
		params.add(new BasicNameValuePair("password", _password));
		for (NameValuePair nvp : params) {
			LogUtils.debug(LogCategory.SERVICE, "Login : " + nvp.getName() + "," + nvp.getValue());
		}
		return params;
	}

	@Override
	protected String getHttpMethod() {
		return HttpCommunication.METHOD_POST;
	}

	@Override
	protected void parseResponse(String response) {
		LogUtils.debug(LogCategory.SERVICE, "Login: parseResponse: response->" + response);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.LOGIN_EVENT_NOTIFIER);
		try {
			JSONObject jObject = new JSONObject(response);
			boolean status = jObject.getBoolean("status");
			if (status) {
				String id = jObject.getString("id");
				AppSettings.getInstance().setUserId(id);
				notifier.eventNotify(EventTypes.LOGIN_SUCCESS, null);

			} else {
				String description = jObject.optString("message");
				CommunicationError error = new CommunicationError(CommunicationError.ERROR_OTHER, description, null);
				LogUtils.debug(LogCategory.SERVICE, "Login: parseResponse: error->" + description);
				notifier.eventNotify(EventTypes.LOGIN_FAILED, error);
			}
		} catch (Exception e) {
			LogUtils.error(LogCategory.SERVICE, "Login: parseResponse: Exception->" + e.getMessage());
			e.printStackTrace();
			notifyError(CommunicationError.ERROR_INVALID_RESPONSE);
		}
	}

	@Override
	protected void notifyError(int errorCode) {
		CommunicationError error = new CommunicationError(CommunicationError.ERROR_OTHER, null, null);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.LOGIN_EVENT_NOTIFIER);
		notifier.eventNotify(EventTypes.LOGIN_FAILED, error);
	}
}
