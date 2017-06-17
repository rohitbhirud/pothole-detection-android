package org.pk.potholedetection.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * This class is used to display dialog.
 * 
 */
public class DialogHelper {
	private static AlertDialog.Builder _builder;
	private static AlertDialog _dialog;

	/**
	 * Method to display dialog.
	 * 
	 * @param activity
	 *            the activity context
	 * @param title
	 *            the string resource id of dialog title
	 * @param mesage
	 *            the string resource id of dialog message
	 * @param btnNegative
	 *            the string resource id of dialog negative button
	 * @param btnNeutral
	 *            the string resource id of dialog neutral button
	 * @param btnPositive
	 *            the string resource id of dialog positive button
	 */
	public static void showDialog(final Activity activity, final int title, final int mesage, final int btnNegative, final int btnNeutral, final int btnPositive) {
		final DialogInterface.OnClickListener dialogButtonClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (_dialog != null && _dialog.isShowing()) {
						_dialog.dismiss();
					}

					_builder = new AlertDialog.Builder(activity);
					_builder.setTitle(title);
					_builder.setMessage(mesage);
					if (btnNegative != -1) {
						_builder.setNegativeButton(btnNegative, dialogButtonClickListener);
					}

					if (btnNeutral != -1) {
						_builder.setNeutralButton(btnNeutral, dialogButtonClickListener);
					}

					if (btnPositive != -1) {
						_builder.setPositiveButton(btnPositive, dialogButtonClickListener);
					}

					_builder.setCancelable(false);

					_dialog = _builder.create();
					_dialog.setCanceledOnTouchOutside(false);
					_dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Method to display dialog.
	 * 
	 * @param activity
	 *            the activity context
	 * @param title
	 *            the string resource id of dialog title
	 * @param mesage
	 *            the string message
	 * @param btnNegative
	 *            the string resource id of dialog negative button
	 * @param btnNeutral
	 *            the string resource id of dialog neutral button
	 * @param btnPositive
	 *            the string resource id of dialog positive button
	 */
	public static void showDialog(final Activity activity, final int title, final String mesage, final int btnNegative, final int btnNeutral, final int btnPositive) {
		final DialogInterface.OnClickListener dialogButtonClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (_dialog != null && _dialog.isShowing()) {
						_dialog.dismiss();
					}

					_builder = new AlertDialog.Builder(activity);
					_builder.setTitle(title);
					_builder.setMessage(mesage);
					if (btnNegative != -1) {
						_builder.setNegativeButton(btnNegative, dialogButtonClickListener);
					}

					if (btnNeutral != -1) {
						_builder.setNeutralButton(btnNeutral, dialogButtonClickListener);
					}

					if (btnPositive != -1) {
						_builder.setPositiveButton(btnPositive, dialogButtonClickListener);
					}

					_builder.setCancelable(false);

					_dialog = _builder.create();
					_dialog.setCanceledOnTouchOutside(false);
					_dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Method to display dialog.
	 * 
	 * @param activity
	 *            the activity context
	 * @param title
	 *            the string resource id of dialog title
	 * @param mesage
	 *            the string resource id of dialog message
	 * @param btnNegative
	 *            the string resource id of dialog negative button
	 * @param btnNeutral
	 *            the string resource id of dialog neutral button
	 * @param btnPositive
	 *            the string resource id of dialog positive button
	 * @param onClickListener
	 *            the {@link OnClickListener} to handle dialog button click
	 *            events.
	 */
	public static void showDialog(final Activity activity, final int title, final int mesage, final int btnNegative, final int btnNeutral, final int btnPositive,
		final DialogInterface.OnClickListener onClickListener) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (_dialog != null && _dialog.isShowing()) {
						_dialog.dismiss();
					}

					_builder = new AlertDialog.Builder(activity);
					_builder.setTitle(title);
					_builder.setMessage(mesage);
					if (btnNegative != -1) {
						_builder.setNegativeButton(btnNegative, onClickListener);
					}

					if (btnNeutral != -1) {
						_builder.setNeutralButton(btnNeutral, onClickListener);
					}

					if (btnPositive != -1) {
						_builder.setPositiveButton(btnPositive, onClickListener);
					}

					_builder.setCancelable(false);

					_dialog = _builder.create();
					_dialog.setCanceledOnTouchOutside(false);
					_dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Method to display dialog.
	 * 
	 * @param activity
	 *            the activity context
	 * @param title
	 *            the string resource id of dialog title
	 * @param mesage
	 *            the string message
	 * @param btnNegative
	 *            the string resource id of dialog negative button
	 * @param btnNeutral
	 *            the string resource id of dialog neutral button
	 * @param btnPositive
	 *            the string resource id of dialog positive button
	 * @param onClickListener
	 *            the {@link OnClickListener} to handle dialog button click
	 *            events.
	 */
	public static void showDialog(final Activity activity, final int title, final String mesage, final int btnNegative, final int btnNeutral, final int btnPositive,
		final DialogInterface.OnClickListener onClickListener) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (_dialog != null && _dialog.isShowing()) {
						_dialog.dismiss();
					}

					_builder = new AlertDialog.Builder(activity);
					_builder.setTitle(title);
					_builder.setMessage(mesage);
					if (btnNegative != -1) {
						_builder.setNegativeButton(btnNegative, onClickListener);
					}

					if (btnNeutral != -1) {
						_builder.setNeutralButton(btnNeutral, onClickListener);
					}

					if (btnPositive != -1) {
						_builder.setPositiveButton(btnPositive, onClickListener);
					}

					_builder.setCancelable(false);

					_dialog = _builder.create();
					_dialog.setCanceledOnTouchOutside(false);
					_dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
