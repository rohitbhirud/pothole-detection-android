package org.pk.potholedetection.ui.controllers;

import org.pk.potholedetection.datamodels.CommunicationError;
import org.pk.potholedetection.datamodels.RegistrationModel;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventStates;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.IEventListener;
import org.pk.potholedetection.eventnotification.ListenerPriority;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;
import org.pk.potholedetection.ui.FrgUserRegistration;
import org.pk.potholedetection.utilities.CustomProgressDialog;
import org.pk.potholedetection.utilities.DialogHelper;
import org.pk.potholedetection.webservices.RegisterUser;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

import org.pk.potholedetection.R;

public class UserRegistrationController implements OnClickListener, IEventListener, OnKeyListener {

	private final FrgUserRegistration _fragment;

	public UserRegistrationController(FrgUserRegistration fragment) {
		_fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			RegistrationModel model = _fragment.validateDetails();
			if (model != null) {
				registerListener();
				_fragment.hideSoftKeyboard();
				CustomProgressDialog.show(_fragment.getActivity(), R.string.msg_registering);
				RegisterUser registerUser = new RegisterUser(model);
				registerUser.send();
			}
			break;
		}
	}

	@Override
	public void registerListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.REGISTRATION_EVENT_NOTIFIER);
		notifier.registerListener(UserRegistrationController.this, ListenerPriority.HIGH);
	}

	@Override
	public void unregisterListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.REGISTRATION_EVENT_NOTIFIER);
		notifier.unregisterListener(UserRegistrationController.this);
	}

	@Override
	public int eventNotify(int eventType, Object eventObject) {
		LogUtils.debug(LogCategory.CONTROLLER, "FarmerRegistrtionController: eventNotify: eventType->" + eventType);
		int eventState = EventStates.IGNORED;
		switch (eventType) {
		case EventTypes.REGISTRATION_SUCCESS:
			CustomProgressDialog.dismiss(_fragment.getActivity());
			String message = _fragment.getString(R.string.msg_registration_successful);
			DialogHelper.showDialog(_fragment.getActivity(), R.string.dlg_title_info, message, R.string.dlg_btn_ok, -1, -1, _dialogButtonClickListener);
			break;

		case EventTypes.REGISTRATION_FAILED:
			CustomProgressDialog.dismiss(_fragment.getActivity());
			CommunicationError error = (CommunicationError) eventObject;
			int errorCode = error.getErrorCode();
			String errorMessage = _fragment.getString(R.string.msg_registration_failed);
			if (errorCode == CommunicationError.ERROR_OTHER) {
				String description = error.getDescription();
				if (!TextUtils.isEmpty(description)) {
					errorMessage += " " + description;
				}
			}
			DialogHelper.showDialog(_fragment.getActivity(), R.string.dlg_title_error, errorMessage, R.string.dlg_btn_ok, -1, -1);
			break;
		}
		return eventState;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				_fragment.gotoLoginScreen();
				return true;
			}
		}
		return false;
	}

	DialogInterface.OnClickListener _dialogButtonClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			_fragment.gotoLoginScreen();
		}
	};
}
