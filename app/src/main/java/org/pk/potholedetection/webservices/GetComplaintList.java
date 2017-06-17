package org.pk.potholedetection.webservices;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pk.potholedetection.communication.CommunicationConstants;
import org.pk.potholedetection.communication.HttpCommunication;
import org.pk.potholedetection.datamodels.CommunicationError;
import org.pk.potholedetection.datamodels.ComplaintList;
import org.pk.potholedetection.datamodels.ComplaintReply;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;

import java.util.ArrayList;


/**
 * This is a web service class to get complaint list
 * 
 */
public class GetComplaintList extends BaseService {

	private String _getComplaintListUrl = CommunicationConstants.SERVER_BASE_URL + "get_complain_reply.php";
	private String _id;

	public GetComplaintList(String id) {
		_id = id;
	}

	@Override
	protected String getUrl() {
		return _getComplaintListUrl;
	}

	@Override
	protected ArrayList<NameValuePair> getRequestParams() {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", _id));
		return params;
	}

	@Override
	protected String getHttpMethod() {
		return HttpCommunication.METHOD_POST;
	}

	@Override
	protected void parseResponse(String response) {
		LogUtils.debug(LogCategory.SERVICE, "GetComplaintList: parseResponse: response->" + response);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.COMPLAINT_NOTIFIER);
		try {
			JSONObject jObject = new JSONObject(response);
			boolean status = jObject.getBoolean("status");
			if (status) {
				ArrayList<ComplaintReply> replyList = new ArrayList<ComplaintReply>();
				JSONArray jArrUsers = jObject.getJSONArray("details");
				int length = jArrUsers.length();
				for (int i = 0; i < length; i++) {
					JSONObject jObjUser = jArrUsers.getJSONObject(i);
					String type = jObjUser.optString("comp_type");
					String title = jObjUser.optString("comp_title");
					String desc = jObjUser.optString("description");
					String reply = jObjUser.optString("reply");
					String complaintStatus = jObjUser.optString("status");
					ComplaintReply cr = new ComplaintReply(type, title, desc, reply, complaintStatus);
					replyList.add(cr);
				}
				ComplaintList.getInstance().setList(replyList);
				notifier.eventNotify(EventTypes.COMPLAINT_LIST_SUCCESS, replyList);
			} else {

				CommunicationError error = new CommunicationError(CommunicationError.ERROR_OTHER, "", null);
				notifier.eventNotify(EventTypes.COMPLAINT_LIST_FAILED, error);
			}
		} catch (Exception e) {
			LogUtils.error(LogCategory.SERVICE, "GetComplaintList: parseResponse: Exception->" + e.getMessage());
			e.printStackTrace();
			notifyError(CommunicationError.ERROR_INVALID_RESPONSE);
		}
	}

	@Override
	protected void notifyError(int errorCode) {
		CommunicationError error = new CommunicationError(CommunicationError.ERROR_OTHER, null, null);
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.COMPLAINT_NOTIFIER);
		notifier.eventNotify(EventTypes.COMPLAINT_LIST_FAILED, error);
	}
}
