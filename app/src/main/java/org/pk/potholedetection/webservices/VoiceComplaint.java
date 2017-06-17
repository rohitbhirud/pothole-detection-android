package org.pk.potholedetection.webservices;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.pk.potholedetection.communication.CommunicationConstants;
import org.pk.potholedetection.communication.HttpCommunication;
import org.pk.potholedetection.datamodels.CommunicationError;
import org.pk.potholedetection.datamodels.Complaint;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;
import org.pk.potholedetection.utilities.AppSettings;


/**
 * This is a web service class to make voice complaint.
 * 
 */
public class VoiceComplaint extends BaseService {

	private final Complaint _complaint;

	private final String _complaintUrl = CommunicationConstants.SERVER_BASE_URL + "voice_complain.php";

	/**
	 * Parameterized Constructor
	 * 
	 * @param complaint
	 *            the {@link Complaint} object
	 */
	public VoiceComplaint(Complaint complaint) {
		_complaint = complaint;
	}

	@Override
	protected String getUrl() {
		return _complaintUrl;
	}

	@Override
	protected ArrayList<NameValuePair> getRequestParams() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_name", AppSettings.getInstance().getUsername()));
		params.add(new BasicNameValuePair("compType", _complaint.getComplaintType()));
		params.add(new BasicNameValuePair("compTitle", _complaint.getTitle()));
		params.add(new BasicNameValuePair("compDesc", _complaint.getDescription()));
		params.add(new BasicNameValuePair("user_id", AppSettings.getInstance().getUserId()));
		params.add(new BasicNameValuePair("latitude", "" + _complaint.getLatitude()));
		params.add(new BasicNameValuePair("longitude", "" + _complaint.getLongitude()));
		return params;
	}

	@Override
	protected String getHttpMethod() {
		return HttpCommunication.METHOD_POST;
	}

	@Override
	protected void parseResponse(String response) {
		LogUtils.debug(LogCategory.SERVICE, "VoiceComplaint: parseResponse: response->" + response);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.COMPLAINT_NOTIFIER);
		try {
			JSONObject jObject = new JSONObject(response);
			boolean status = jObject.getBoolean("status");
			if (status) {
				notifier.eventNotify(EventTypes.COMPLAINT_SUCCESS, null);

			} else {
				String description = jObject.optString("message");
				CommunicationError error = new CommunicationError(CommunicationError.ERROR_OTHER, description, null);
				LogUtils.debug(LogCategory.SERVICE, "JurkComplaint: parseResponse: error->" + description);
				notifier.eventNotify(EventTypes.COMPLAINT_FAILED, error);
			}
		} catch (Exception e) {
			LogUtils.error(LogCategory.SERVICE, "JurkComplaint: parseResponse: Exception->" + e.getMessage());
			e.printStackTrace();
			notifyError(CommunicationError.ERROR_INVALID_RESPONSE);
		}
	}

	@Override
	protected void notifyError(int errorCode) {
		CommunicationError error = new CommunicationError(CommunicationError.ERROR_OTHER, null, null);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.COMPLAINT_NOTIFIER);
		notifier.eventNotify(EventTypes.COMPLAINT_FAILED, error);
	}
}
