package org.pk.potholedetection.ui.controllers;

import org.pk.potholedetection.Core;
import org.pk.potholedetection.ui.FrgHome;
import org.pk.potholedetection.utilities.AppSettings;
import org.pk.potholedetection.utilities.DialogHelper;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

import org.pk.potholedetection.R;

public class HomeController implements OnClickListener, OnKeyListener {
	private final FrgHome _fragment;

	public HomeController(FrgHome fragment) {
		_fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_complaints:
			_fragment.gotoComplaintScreen();
			break;

		case R.id.btn_voice_complaint:
			_fragment.promptSpeechInput();
			break;

		case R.id.btn_logout:
			AppSettings.getInstance().setUserLogined(false);
			_fragment.gotoLoginScreen();
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				DialogHelper.showDialog(_fragment.getActivity(), R.string.dlg_title_confirmation, R.string.msg_confirm_exit, R.string.dlg_btn_yes, R.string.dlg_btn_no, -1, dialogButtonClickListener);
				return true;
			}
		}
		return false;
	}

	private DialogInterface.OnClickListener dialogButtonClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_NEGATIVE:
				_fragment.getActivity().finish();
				Core.getInstance().stop();
				break;
			}
		}
	};
}
