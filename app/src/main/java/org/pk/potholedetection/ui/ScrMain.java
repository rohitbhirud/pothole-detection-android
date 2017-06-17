package org.pk.potholedetection.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import org.pk.potholedetection.R;

/**
 * The main activity of class.
 * 
 */
public class ScrMain extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startLogin();
	}

	public void startLogin() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		FrgLogin frgLogin = new FrgLogin();
		transaction.replace(R.id.container, frgLogin);
		transaction.commit();
	}

	/**
	 * Method to set the title
	 */
	public void setTitle(int strResId) {
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(strResId);
	}

	/**
	 * Method to set the title
	 * 
	 * @param strTitle
	 *            using string resource
	 */
	public void setTitle(String strTitle) {
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(strTitle);
	}
}
