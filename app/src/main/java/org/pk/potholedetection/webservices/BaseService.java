package org.pk.potholedetection.webservices;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.pk.potholedetection.communication.HttpCommunication;
import org.pk.potholedetection.datamodels.CommunicationError;

import android.os.AsyncTask;



/**
 * This class is an abstract base class for all web services.
 * 
 */
public abstract class BaseService {
	protected abstract String getUrl();

	protected abstract ArrayList<NameValuePair> getRequestParams();

	protected abstract String getHttpMethod();

	protected abstract void parseResponse(String response);

	protected abstract void notifyError(int errorCode);

	public void send() {
		SendRequestTask sendRequestTask = new SendRequestTask();
		sendRequestTask.execute();
	}

	private class SendRequestTask extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				HttpCommunication httpCommunication = new HttpCommunication();
				ArrayList<NameValuePair> httpParams = getRequestParams();
				String response = httpCommunication.makeHttpRequest(getUrl(), getHttpMethod(), httpParams);
				parseResponse(response);
			} catch (Exception ex) {
				notifyError(CommunicationError.ERROR_COMMUNICATION);
			}
			return 0;
		}
	}
}
