package org.pk.potholedetection.utilities;

import org.pk.potholedetection.MainApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * This is a singleton class used to maintain the application settings. Uses
 * Android {@link SharedPreferences}
 * 
 */
public class AppSettings {
	private static AppSettings _instance;

	private SharedPreferences _sharedPreferences;
	private Editor _sharedPrefEditor;

	private static final String SHARED_PREFERENCE_NAME = "PotholeDetectionPreferences";

	private String KEY_USERNAME = "KEY_USERNAME";
	private String KEY_PASSWORD = "KEY_PASSWORD";
	private String KEY_USER_ID = "KEY_USER_ID";
	private String KEY_USER_LOGINED = "KEY_USER_LOGINED";
	private String KEY_REMEMBER_PASSWORD = "KEY_REMEMBER_PASSWORD";

	/**
	 * Private constructor
	 */
	private AppSettings() {

	}

	/**
	 * Method to get the initialized instance of {@link AppSettings}.
	 * 
	 * @return the initialized instance of {@link AppSettings}
	 */
	public static AppSettings getInstance() {
		if (_instance == null) {
			_instance = new AppSettings();
		}
		return _instance;
	}

	/**
	 * Method to get the {@link SharedPreferences} object
	 * 
	 * @return the {@link SharedPreferences} object
	 */
	private SharedPreferences _getSharedPreferences() {
		if (_sharedPreferences == null) {
			_sharedPreferences = MainApplication.appContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		}
		return _sharedPreferences;
	}

	/**
	 * Method to get the {@link SharedPreferences.Editor} object
	 * 
	 * @return the {@link SharedPreferences.Editor} object
	 */
	private Editor _getEditor() {
		if (_sharedPrefEditor == null) {
			_sharedPrefEditor = _getSharedPreferences().edit();
		}
		return _sharedPrefEditor;
	}

	public String getUsername() {
		return _getSharedPreferences().getString(KEY_USERNAME, null);
	}

	public void setUsername(String username) {
		_getEditor().putString(KEY_USERNAME, username).commit();
	}

	public String getPassword() {
		return _getSharedPreferences().getString(KEY_PASSWORD, null);
	}

	public void setPassword(String password) {
		_getEditor().putString(KEY_PASSWORD, password).commit();
	}

	public String getUserId() {
		return _getSharedPreferences().getString(KEY_USER_ID, null);
	}

	public void setUserId(String id) {
		_getEditor().putString(KEY_USER_ID, id).commit();
	}

	public boolean rememberPassword() {
		return _getSharedPreferences().getBoolean(KEY_REMEMBER_PASSWORD, false);
	}

	public void setRememberPassword(boolean remember) {
		_getEditor().putBoolean(KEY_REMEMBER_PASSWORD, remember).commit();
	}

	public boolean isUserLogined() {
		return _getSharedPreferences().getBoolean(KEY_USER_LOGINED, false);
	}

	public void setUserLogined(boolean logined) {
		_getEditor().putBoolean(KEY_USER_LOGINED, logined).commit();
	}
}
