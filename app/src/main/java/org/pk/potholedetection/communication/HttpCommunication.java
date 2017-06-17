package org.pk.potholedetection.communication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * @author SHREE
 *
 */
public class HttpCommunication {
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";

	/**
	 * Default Constructor
	 */
	public HttpCommunication() {

	}

	public String makeHttpRequest(String url, String method, ArrayList<NameValuePair> params) throws Exception {

		InputStream inputStream = null;
		String response = "";
		HttpParams httpParameters = new BasicHttpParams();

		/**
		 * Set the timeout in milliseconds until a connection is established.
		 * The default value is zero, that means the timeout is not used.
		 */
		int timeoutConnection = 15000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		/**
		 * Set the default socket timeout (SO_TIMEOUT) in milliseconds which is
		 * the timeout for waiting for data.
		 */
		int timeoutSocket = 15000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		/**
		 * Check for request method
		 */
		if (method == "POST") {
			HttpPost httpPost = new HttpPost(url);

			if (params != null && params.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();

		} else if (method == "GET") {
			if (params != null && params.size() > 0) {
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
			}

			HttpGet httpGet = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		inputStream.close();
		response = sb.toString();

		return response;
	}
}