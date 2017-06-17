package org.pk.potholedetection;

import android.app.Application;
import android.content.Context;

/**
 * @author SHREE
 *
 */
public class MainApplication extends Application {

	public static final String LOG_TAG = "PotholeDetection";
	public static Context appContext;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this;
	}
}
