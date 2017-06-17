package org.pk.potholedetection.ui.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Toast;

import org.pk.potholedetection.R;
import org.pk.potholedetection.datamodels.CommunicationError;
import org.pk.potholedetection.datamodels.Complaint;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.IEventListener;
import org.pk.potholedetection.eventnotification.ListenerPriority;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;
import org.pk.potholedetection.ui.FrgMakeComplaint;
import org.pk.potholedetection.utilities.CustomProgressDialog;
import org.pk.potholedetection.utilities.CustomToast;
import org.pk.potholedetection.utilities.DialogHelper;
import org.pk.potholedetection.webservices.ComplaintUploader;

import java.util.ArrayList;
import java.util.List;

public class MakeComplaintController implements OnClickListener, IEventListener, OnKeyListener {
	private final FrgMakeComplaint _fragment;

	public MakeComplaintController(FrgMakeComplaint fragment) {
		_fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_image:
			_fragment.showAddImageOptions();
			break;

		case R.id.btn_add:
			Complaint complaint = _fragment.getComplaintDetails();
			if (complaint != null) {
				CustomProgressDialog.show(_fragment.getActivity(), R.string.msg_send_complaint);
				_fragment.updateUploadPercentage(0);
				String imagePath = _fragment.getImageFilePath();
				ComplaintUploader complaintUploader = new ComplaintUploader(complaint, imagePath);
				complaintUploader.startUpload();
			}
			break;
		}
	}

	@Override
	public void registerListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.COMPLAINT_NOTIFIER);
		notifier.registerListener(MakeComplaintController.this, ListenerPriority.HIGH);

		notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.UPLOAD_FILE_NOTIFIER);
		notifier.registerListener(MakeComplaintController.this, ListenerPriority.HIGH);
	}

	@Override
	public void unregisterListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.COMPLAINT_NOTIFIER);
		notifier.unregisterListener(MakeComplaintController.this);

		notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.UPLOAD_FILE_NOTIFIER);
		notifier.unregisterListener(MakeComplaintController.this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int eventNotify(int eventType, Object eventObject) {
		LogUtils.debug(LogCategory.CONTROLLER, "MakeComplaintController: eventNotify: eventType->" + eventType);
		List<String> data = null;
		String localFilePath = null;
		switch (eventType) {

		case EventTypes.COMPLAINT_SUCCESS:
			_fragment.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					CustomProgressDialog.dismiss(_fragment.getActivity());
					Toast.makeText(_fragment.getActivity(), "Complaint send successfully", Toast.LENGTH_LONG).show();
				}
			});

			break;

		case EventTypes.COMPLAINT_FAILED:
			_fragment.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					CustomProgressDialog.dismiss(_fragment.getActivity());
					Toast.makeText(_fragment.getActivity(), "Failed to send complaint.", Toast.LENGTH_LONG).show();

				}
			});
			break;

		case EventTypes.UPLOAD_FILE_PERCENTAGE_UPDATED:
			_fragment.updateUploadPercentage((Integer) eventObject);
			break;

		case EventTypes.UPLOAD_FILE_SUCCESS:
			CustomProgressDialog.dismiss(_fragment.getActivity());
			data = (ArrayList<String>) eventObject;
			localFilePath = (String) data.get(0);
			String imageUrl = (String) data.get(1);
			LogUtils.debug(LogCategory.CONTROLLER, "MakeComplaintController: eventNotify: UPLOAD_FILE_SUCCESS: localFilePath->" + localFilePath + " imageUrl->" + imageUrl);
			_fragment.updateUploadPercentage(100);
			CustomToast.show(_fragment.getActivity(), "Complaint sent successfully.", CustomToast.LENGTH_LONG);
			break;

		case EventTypes.UPLOAD_FILE_FAILED:
			CustomProgressDialog.dismiss(_fragment.getActivity());
			CommunicationError error = (CommunicationError) eventObject;
			String description = error.getDescription();
			String message = "Failed to send complaint.";
			if (!TextUtils.isEmpty(description)) {
				message += " " + description;
			}
			_fragment.updateUploadPercentage(100);
			DialogHelper.showDialog(_fragment.getActivity(), R.string.dlg_title_error, message, R.string.dlg_btn_ok, -1, -1);
			break;
		}
		return 0;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				_fragment.gotoHomeScreen();
				return true;
			}
		}
		return false;
	}

	public Bitmap getResizedBitmap(String filePath, int newHeight, int newWidth) {

		Bitmap bm = BitmapFactory.decodeFile(filePath);

		int width = bm.getWidth();

		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;

		float scaleHeight = ((float) newHeight) / height;

		// CREATE A MATRIX FOR THE MANIPULATION

		Matrix matrix = new Matrix();

		// RESIZE THE BIT MAP

		matrix.postScale(scaleWidth, scaleHeight);

		// RECREATE THE NEW BITMAP

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		return resizedBitmap;

	}

}
