package org.pk.potholedetection.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.pk.potholedetection.Core;
import org.pk.potholedetection.R;
import org.pk.potholedetection.ui.controllers.LoginController;
import org.pk.potholedetection.utilities.AppSettings;
import org.pk.potholedetection.utilities.DeviceUtils;
import org.pk.potholedetection.utilities.DialogHelper;

public class FrgLogin extends Fragment {
	private LoginController _controller;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		Core.getInstance().init();

		View view = inflater.inflate(R.layout.login, container, false);
		if (AppSettings.getInstance().isUserLogined()) {
			gotoHomeScreen();
			return view;
		}
		_controller = new LoginController(this);

		Button btnLogin = (Button) view.findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(_controller);

		Button btnRegister = (Button) view.findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(_controller);

		EditText etLogin = (EditText) view.findViewById(R.id.et_username);
		String username = AppSettings.getInstance().getUsername();
		if (!TextUtils.isEmpty(username)) {
			etLogin.setText(username);
		}
		if (AppSettings.getInstance().rememberPassword()) {
			CheckBox cbRememberPass = (CheckBox) view.findViewById(R.id.cb_remember_password);
			cbRememberPass.setChecked(true);

			EditText etPassword = (EditText) view.findViewById(R.id.et_password);
			String password = AppSettings.getInstance().getPassword();
			if (!TextUtils.isEmpty(password)) {
				etPassword.setText(password);
			}
		}

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((ScrMain) activity).setTitle(R.string.title_login);
	}

	public boolean validateDetails() {
		EditText etLogin = (EditText) getView().findViewById(R.id.et_username);
		String email = etLogin.getText().toString();
		if (TextUtils.isEmpty(email)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_username, R.string.dlg_btn_ok, -1, -1);
			return false;
		}

		EditText etPassword = (EditText) getView().findViewById(R.id.et_password);
		String password = etPassword.getText().toString();
		if (TextUtils.isEmpty(password)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_password, R.string.dlg_btn_ok, -1, -1);
			return false;
		}
		return true;
	}

	public String getEmailId() {
		EditText etLogin = (EditText) getView().findViewById(R.id.et_username);
		return etLogin.getText().toString();
	}

	public String getPassword() {
		EditText etPassword = (EditText) getView().findViewById(R.id.et_password);
		return etPassword.getText().toString();
	}

	public boolean rememberPassword() {
		CheckBox chRememberPassword = (CheckBox) getView().findViewById(R.id.cb_remember_password);
		return chRememberPassword.isChecked();
	}

	public void hideSoftKeyboard() {
		EditText etLogin = (EditText) getView().findViewById(R.id.et_username);
		DeviceUtils.hideSoftKeyboard(etLogin);
	}

	public void gotoHomeScreen() {		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FrgHome fr = new FrgHome();
		transaction.replace(R.id.container, fr);
		transaction.commit();
	}

	public void gotoRegistrationScreen() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FrgUserRegistration fr = new FrgUserRegistration();
		transaction.replace(R.id.container, fr);
		transaction.commit();
	}

	@Override
	public void onStop() {
		super.onStop();
		_controller.unregisterListener();
	}
}
