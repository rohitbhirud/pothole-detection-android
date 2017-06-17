package org.pk.potholedetection.ui;

import java.util.ArrayList;
import java.util.Locale;

import org.pk.potholedetection.datamodels.Complaint;
import org.pk.potholedetection.locator.LocationMonitor;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;
import org.pk.potholedetection.ui.controllers.HomeController;
import org.pk.potholedetection.utilities.CustomToast;
import org.pk.potholedetection.webservices.VoiceComplaint;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.pk.potholedetection.R;

public class FrgHome extends Fragment {
	private HomeController _controller;

	public static final int REQ_CODE_SPEECH_INPUT = 10001;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home, container, false);
		_controller = new HomeController(this);

		Button btnComplaints = (Button) view.findViewById(R.id.btn_complaints);
		btnComplaints.setOnClickListener(_controller);

		Button btnVoiceComplaint = (Button) view.findViewById(R.id.btn_voice_complaint);
		btnVoiceComplaint.setOnClickListener(_controller);

		Button btnLogout = (Button) view.findViewById(R.id.btn_logout);
		btnLogout.setOnClickListener(_controller);

		view.requestFocus();
		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(_controller);

		return view;
	}

	@Override
	public void onResume() {
		((ScrMain) getActivity()).setTitle(R.string.app_name);
		super.onResume();
	}

	public void gotoComplaintScreen() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FrgComplaintList fr = new FrgComplaintList();
		transaction.replace(R.id.container, fr);
		transaction.commit();
	}

	public void gotoLoginScreen() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FrgLogin fr = new FrgLogin();
		transaction.replace(R.id.container, fr);
		transaction.commit();
	}

	/**
	 * Showing google speech input dialog
	 * */
	public void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.lbl_say_something));
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			CustomToast.show(getActivity(), R.string.err_device_does_not_support_stt, CustomToast.LENGTH_LONG);
		}
	}

	/**
	 * Receiving speech input
	 * */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == Activity.RESULT_OK && null != data) {
				ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				LogUtils.debug(LogCategory.GUI, "FrgHome: onActivityResult: result->" + result.toString());
				Location location = LocationMonitor.getInstance().getCurrentLocation();
				double latitude = 0.0;
				double longitude = 0.0;
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				}
				Complaint complaint = new Complaint(null, "Voice Complaint", "This is a voice complaint", latitude, longitude);
				VoiceComplaint voiceComplaint = null;
				if (result.contains("traffic")) {
					complaint.setComplaintType("Traffic");
					voiceComplaint = new VoiceComplaint(complaint);
				} else if (result.contains("accident")) {
					complaint.setComplaintType("Accident");
					voiceComplaint = new VoiceComplaint(complaint);
				} else if (result.contains("pothole")) {
					complaint.setComplaintType("Pothole");
					voiceComplaint = new VoiceComplaint(complaint);
				}
				if (voiceComplaint != null) {
					voiceComplaint.send();
				}
			}
			break;
		}

		}
	}
}
