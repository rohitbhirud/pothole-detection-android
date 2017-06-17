package org.pk.potholedetection.webservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.pk.potholedetection.communication.CommunicationConstants;
import org.pk.potholedetection.datamodels.CommunicationError;
import org.pk.potholedetection.datamodels.Complaint;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;
import org.pk.potholedetection.utilities.AppSettings;


/**
 * @author SHREE
 *
 */
public class ComplaintUploader extends FileUploader {
	private final Complaint _complaint;

	private final String _complaintUrl = CommunicationConstants.SERVER_BASE_URL + "make_complain.php";
	private final String _localFilePath;

	public ComplaintUploader(Complaint complaint, String imagePath) {
		_complaint = complaint;
		_localFilePath = imagePath;
	}

	@Override
	protected HashMap<String, String> getRequestParams() {
		HashMap<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("user_id", AppSettings.getInstance().getUserId());
		requestParams.put("user_name", AppSettings.getInstance().getUsername());
		requestParams.put("compType", _complaint.getComplaintType());
		requestParams.put("compTitle", _complaint.getTitle());
		requestParams.put("compDesc", _complaint.getDescription());
		requestParams.put("latitude", "" + _complaint.getLatitude());
		requestParams.put("longitude", "" + _complaint.getLongitude());
		return requestParams;
	}

	@Override
	protected String getUrl() {
		return _complaintUrl;
	}

	@Override
	protected String getLocalFilePath() {
		return _localFilePath;
	}

	@Override
	protected void notifyProgress(int percentage) {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.UPLOAD_FILE_NOTIFIER);
		notifier.eventNotify(EventTypes.UPLOAD_FILE_PERCENTAGE_UPDATED, percentage);
	}

	@Override
	protected void parseResponse(String response) {
		LogUtils.debug(LogCategory.SERVICE, "ComplaintUploader: parseResponse: response->" + response);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.UPLOAD_FILE_NOTIFIER);
		try {
			JSONObject jObject = new JSONObject(response.substring(response.lastIndexOf("{")));
			boolean status = jObject.getBoolean("status");
			if (status) {
				String imageUrl = jObject.optString("path");
				List<String> data = new ArrayList<String>();
				data.add(_localFilePath);
				data.add(imageUrl);
				notifier.eventNotify(EventTypes.UPLOAD_FILE_SUCCESS, data);
			} else {
				String errorMessage = jObject.getString("message");
				CommunicationError error = new CommunicationError(CommunicationError.ERROR_OTHER, errorMessage, _localFilePath);
				notifier.eventNotify(EventTypes.UPLOAD_FILE_FAILED, error);
			}
		} catch (Exception e) {
			LogUtils.error(LogCategory.SERVICE, "ComplaintUploader: parseResponse: Exception->" + e.getMessage());
			e.printStackTrace();
			notifyError(CommunicationError.ERROR_INVALID_RESPONSE);
		}
	}

	@Override
	protected void notifyError(int errorCode) {
		CommunicationError error = new CommunicationError(CommunicationError.ERROR_COMMUNICATION, "", _localFilePath);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.UPLOAD_FILE_NOTIFIER);
		notifier.eventNotify(EventTypes.UPLOAD_FILE_FAILED, error);
	}
}
