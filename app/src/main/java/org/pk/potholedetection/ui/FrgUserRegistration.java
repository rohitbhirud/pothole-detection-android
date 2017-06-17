package org.pk.potholedetection.ui;

import org.pk.potholedetection.datamodels.RegistrationModel;
import org.pk.potholedetection.ui.controllers.UserRegistrationController;
import org.pk.potholedetection.utilities.DeviceUtils;
import org.pk.potholedetection.utilities.DialogHelper;
import org.pk.potholedetection.utilities.EmailValidator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.pk.potholedetection.R;

public class FrgUserRegistration extends Fragment {
	private UserRegistrationController _controller;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.register_user, container, false);

		_controller = new UserRegistrationController(this);

		Button btnRegister = (Button) view.findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(_controller);

		EditText etEmail = (EditText) view.findViewById(R.id.et_email);
		etEmail.setOnKeyListener(_controller);
		view.setOnKeyListener(_controller);
		view.setFocusableInTouchMode(true);
		view.requestFocus();

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((ScrMain) activity).setTitle(R.string.title_registration);
	}

	public RegistrationModel validateDetails() {
		RegistrationModel model = new RegistrationModel();

		EditText etName = (EditText) getView().findViewById(R.id.et_name);
		String name = etName.getText().toString();
		if (TextUtils.isEmpty(name)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_name, R.string.dlg_btn_ok, -1, -1);
			return null;
		} else {
			model.setName(name);
		}

		EditText etPassword = (EditText) getView().findViewById(R.id.et_password);
		String password = etPassword.getText().toString();
		if (TextUtils.isEmpty(password)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_password, R.string.dlg_btn_ok, -1, -1);
			return null;
		} else {
			model.setPassword(password);
		}

		EditText etAddress = (EditText) getView().findViewById(R.id.et_address);
		String address = etAddress.getText().toString();
		if (TextUtils.isEmpty(address)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_address, R.string.dlg_btn_ok, -1, -1);
			return null;
		} else {
			model.setAddress(address);
		}

		EditText etEmail = (EditText) getView().findViewById(R.id.et_email);
		String email = etEmail.getText().toString();
		if (TextUtils.isEmpty(email) || !EmailValidator.isEmailValid(email)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_valid_email, R.string.dlg_btn_ok, -1, -1);
			return null;
		} else {
			model.setEmail(email);
		}

		EditText etMobileNumber = (EditText) getView().findViewById(R.id.et_mobile);
		String mobileNumber = etMobileNumber.getText().toString();
		if (TextUtils.isEmpty(mobileNumber) || !PhoneNumberUtils.isGlobalPhoneNumber(mobileNumber)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_valid_mobile, R.string.dlg_btn_ok, -1, -1);
			return null;
		} else {
			model.setMobileNumber(mobileNumber);
		}

		EditText etAdharNumber = (EditText) getView().findViewById(R.id.et_adhar_no);
		String adharNumber = etAdharNumber.getText().toString();
		if (TextUtils.isEmpty(adharNumber)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_valid_adhar_no, R.string.dlg_btn_ok, -1, -1);
			return null;
		} else {
			model.setAdharNo(adharNumber);
		}
		return model;
	}

	public void hideSoftKeyboard() {
		EditText etLogin = (EditText) getView().findViewById(R.id.et_name);
		DeviceUtils.hideSoftKeyboard(etLogin);
	}

	public void gotoLoginScreen() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FrgLogin login = new FrgLogin();
		transaction.replace(R.id.container, login);
		transaction.commit();
	}

	@Override
	public void onStop() {
		super.onStop();
		_controller.unregisterListener();
	}
}
