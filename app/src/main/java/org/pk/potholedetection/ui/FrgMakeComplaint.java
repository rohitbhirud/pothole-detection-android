package org.pk.potholedetection.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.pk.potholedetection.MainApplication;
import org.pk.potholedetection.R;
import org.pk.potholedetection.datamodels.Complaint;
import org.pk.potholedetection.locator.LocationMonitor;
import org.pk.potholedetection.logger.LogCategory;
import org.pk.potholedetection.logger.LogUtils;
import org.pk.potholedetection.ui.controllers.MakeComplaintController;
import org.pk.potholedetection.utilities.DialogHelper;

public class FrgMakeComplaint extends Fragment {
	public static final int REQUEST_CHOOSE_IMAGE_FROM_GALLERY = 10001;
	public static final int REQUEST_CAPTURE_IMAGE_FROM_CAMERA = 10002;

	private MakeComplaintController _controller;
	private ImageView _ivImage;
	private TextView _tvUploadPercentage;
	private ProgressBar _pbUpload;
	private EditText _etTitle, _etDes;
	private Spinner _spnrType;

	private Uri _capturedImageURI;
	private String _imgFilePath;
	AddImageOptionsDialog optionsDialog = null;
	private static FrgMakeComplaint _instance;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_instance = this;
	}

	public static synchronized FrgMakeComplaint getInstance() {

		return _instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.make_complaint, container, false);
		_controller = new MakeComplaintController(this);
		_controller.registerListener();

		_tvUploadPercentage = (TextView) view.findViewById(R.id.tv_add_image_percentage);
		_pbUpload = (ProgressBar) view.findViewById(R.id.pb_add_image);
		_ivImage = (ImageView) view.findViewById(R.id.iv_complaint);
		_etTitle = (EditText) view.findViewById(R.id.et_title);
		_spnrType = (Spinner) view.findViewById(R.id.spnr_complaint_type);
		_etDes = (EditText) view.findViewById(R.id.et_description);

		Button btnAddImage = (Button) view.findViewById(R.id.btn_add_image);
		btnAddImage.setOnClickListener(_controller);

		Button btnAddComplaint = (Button) view.findViewById(R.id.btn_add);
		btnAddComplaint.setOnClickListener(_controller);
		view.setOnKeyListener(_controller);
		view.setFocusableInTouchMode(true);
		view.requestFocus();

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((ScrMain) activity).setTitle(R.string.title_add_complaint);
	}

	@Override
	public void onResume() {
		_controller.registerListener();
		super.onResume();
	}

	public Complaint getComplaintDetails() {
		Complaint complaint = new Complaint();

		int type = _spnrType.getSelectedItemPosition();
		if (type == 0) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_select_complaint_type, R.string.dlg_btn_ok, -1, -1);
			return null;
		} else {
			String complaintType = (String) _spnrType.getSelectedItem();
			complaint.setComplaintType(complaintType);
		}

		String title = _etTitle.getText().toString();
		if (TextUtils.isEmpty(title)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_complaint_title, R.string.dlg_btn_ok, -1, -1);
			return null;
		} else {
			complaint.setTitle(title);
		}

		String description = _etDes.getText().toString();
		if (TextUtils.isEmpty(description)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_enter_complaint_description, R.string.dlg_btn_ok, -1, -1);
			return null;
		} else {
			complaint.setDescription(description);
		}

		if (TextUtils.isEmpty(_imgFilePath)) {
			DialogHelper.showDialog(getActivity(), R.string.dlg_title_error, R.string.err_attach_complaint_image, R.string.dlg_btn_ok, -1, -1);
			return null;
		}

		Location location = LocationMonitor.getInstance().getCurrentLocation();
		if (location == null) {
			complaint.setLatitude(21.173680);
			complaint.setLongitude(75.839219);
			// DialogHelper.showDialog(getActivity(), R.string.dlg_title_error,
			// R.string.err_location_service, R.string.dlg_btn_ok, -1, -1);
//			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//			return null;
		} else {
			complaint.setLatitude(location.getLatitude());
			complaint.setLongitude(location.getLongitude());
		}
		return complaint;
	}

	public String getImageFilePath() {
		return _imgFilePath;
	}

	public void gotoHomeScreen() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FrgComplaintList fr = new FrgComplaintList();
		transaction.replace(R.id.container, fr);
		transaction.commit();
	}

	public void displayFileChooser(int request) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		String chooserTitle = null;
		switch (request) {
		case REQUEST_CHOOSE_IMAGE_FROM_GALLERY:
			intent.setType("image/*");
			chooserTitle = "Select Image To Upload";
			break;
		}
		intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		try {
			Activity activity = (Activity) getContext();
//			if (isAdded() && activity != null) {
				FrgMakeComplaint.getInstance().startActivityForResult(Intent.createChooser(intent, chooserTitle), request);
//			}
			LogUtils.debug(LogCategory.GUI, "DisplayFileChooser : Test One");
		} catch (Exception ex) {
			//CustomToast.show(getActivity(), "No Apps can perform this action", CustomToast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtils.debug(LogCategory.GUI, "FrgPropertyDetails: onActivityResult");
		switch (requestCode) {
		case REQUEST_CHOOSE_IMAGE_FROM_GALLERY:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();

				LogUtils.debug(LogCategory.GUI, "FrgMakeComplaint: onActivityResult: REQUEST_CHOOSE_IMAGE_FILES: Uri>" + uri.toString());
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = MainApplication.appContext.getContentResolver().query(uri, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				_imgFilePath = cursor.getString(columnIndex);
				LogUtils.debug(LogCategory.GUI, "FrgMakeComplaint: onActivityResult: _imgFilePath->" + _imgFilePath);

				cursor.close();

				_ivImage.setBackgroundResource(0);
				_ivImage.setImageURI(uri);
				updateUploadPercentage(0);
				// _controller.sendImage(imageFilePath);
			}
			break;

		case REQUEST_CAPTURE_IMAGE_FROM_CAMERA:
			if (resultCode == Activity.RESULT_OK) {
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = MainApplication.appContext.getContentResolver().query(_capturedImageURI, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				_imgFilePath = cursor.getString(columnIndex);
				LogUtils.debug(LogCategory.GUI, "FrgMakeComplaint: onActivityResult: _imgFilePath->" + _imgFilePath);

				cursor.close();
				_ivImage.setBackgroundResource(0);
				_ivImage.setImageURI(_capturedImageURI);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	public void showAddImageOptions() {
		optionsDialog = new AddImageOptionsDialog();
		optionsDialog.show(getActivity().getSupportFragmentManager(), "");
	}

	@SuppressLint("ValidFragment")
	public static class AddImageOptionsDialog extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			String[] items = { getString(R.string.option_gallery), getString(R.string.option_camera) };
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					switch (arg1) {
					case 0:
						FrgMakeComplaint.getInstance().displayFileChooser(FrgMakeComplaint.REQUEST_CHOOSE_IMAGE_FROM_GALLERY);
						break;

					case 1:
						FrgMakeComplaint.getInstance().captureImageUsingCamera();
						break;
					}
				}
			});
			return builder.create();
		}
	}

	private void captureImageUsingCamera() {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis() + ".jpg");
		_capturedImageURI = MainApplication.appContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, _capturedImageURI);
		startActivityForResult(intentPicture, REQUEST_CAPTURE_IMAGE_FROM_CAMERA);
	}

	public void updateUploadPercentage(final int percentage) {
		try {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						if (percentage == 0) {
							_tvUploadPercentage.setText("" + percentage + "%");
							_tvUploadPercentage.setVisibility(View.GONE);
							_pbUpload.setVisibility(View.GONE);

						} else if (percentage > 0 && percentage < 100) {
							_tvUploadPercentage.setText("" + percentage + "%");

						} else {
							_tvUploadPercentage.setVisibility(View.GONE);
							_pbUpload.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						LogUtils.error(LogCategory.GUI, "FrgMakeComplaint: updateUploadPercentage: run: Exception->" + e.getMessage());
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			LogUtils.error(LogCategory.GUI, "FrgMakeComplaint: updateUploadPercentage: Exception->" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onStop() {
		_controller.unregisterListener();
		super.onStop();
	}
}
