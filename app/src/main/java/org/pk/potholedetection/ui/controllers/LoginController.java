package org.pk.potholedetection.ui.controllers;

import org.pk.potholedetection.Core;
import org.pk.potholedetection.datamodels.CommunicationError;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventStates;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.IEventListener;
import org.pk.potholedetection.eventnotification.ListenerPriority;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;
import org.pk.potholedetection.ui.FrgLogin;
import org.pk.potholedetection.utilities.AppSettings;
import org.pk.potholedetection.utilities.CustomProgressDialog;
import org.pk.potholedetection.utilities.DialogHelper;
import org.pk.potholedetection.webservices.Login;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import org.pk.potholedetection.R;

public class LoginController implements OnClickListener, IEventListener {

	private final FrgLogin _fragment;

	public LoginController(FrgLogin fragment) {
		_fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			if (_fragment.validateDetails()) {
				registerListener();
				_fragment.hideSoftKeyboard();
				CustomProgressDialog.show(_fragment.getActivity(), R.string.msg_login_in);
				String emailId = _fragment.getEmailId();
				String password = _fragment.getPassword();
				boolean remember = _fragment.rememberPassword();

				AppSettings.getInstance().setUsername(emailId);
				AppSettings.getInstance().setPassword(password);

				if (remember) {
					AppSettings.getInstance().setRememberPassword(remember);
				}

				Login login = new Login(emailId, password, null);
				login.send();
			}
			break;

		case R.id.btn_register:
			_fragment.gotoRegistrationScreen();
			break;
		}
	}

	@Override
	public void registerListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.LOGIN_EVENT_NOTIFIER);
		notifier.registerListener(LoginController.this, ListenerPriority.HIGH);
	}

	@Override
	public void unregisterListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.LOGIN_EVENT_NOTIFIER);
		notifier.unregisterListener(LoginController.this);
	}

	@Override
	public int eventNotify(int eventType, Object eventObject) {
		LogUtils.debug(LogCategory.CONTROLLER, "LoginController: eventNotify: eventType->" + eventType);
		int eventState = EventStates.IGNORED;
		switch (eventType) {
		case EventTypes.LOGIN_SUCCESS:			
			CustomProgressDialog.dismiss(_fragment.getActivity());
			_fragment.gotoHomeScreen();
			AppSettings.getInstance().setUserLogined(true);
			Core.getInstance().init();
			break;

		case EventTypes.LOGIN_FAILED:
			CustomProgressDialog.dismiss(_fragment.getActivity());
			CommunicationError error = (CommunicationError) eventObject;
			int errorCode = error.getErrorCode();
			if (errorCode == CommunicationError.ERROR_COMMUNICATION || errorCode == CommunicationError.ERROR_NO_NETWORK) {

			} else if (errorCode == CommunicationError.ERROR_OTHER) {
				String errorMessage = _fragment.getString(R.string.msg_login_failed);
				String description = error.getDescription();
				if (!TextUtils.isEmpty(description)) {
					errorMessage += " " + description;
				}
				DialogHelper.showDialog(_fragment.getActivity(), R.string.dlg_title_error, errorMessage, R.string.dlg_btn_ok, -1, -1);
			}
			break;
		}
		return eventState;
	}

}
